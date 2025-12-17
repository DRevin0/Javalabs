package main.java.com.taxi.dispatcher;

import main.java.com.taxi.model.Order;
import main.java.com.taxi.model.Taxi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher implements Runnable {
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final CopyOnWriteArrayList<Taxi> taxis = new CopyOnWriteArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition taxiAvailableCondition = lock.newCondition();
    private volatile boolean shutdownRequested = false;
    private final AtomicInteger totalOrders = new AtomicInteger(0);
    private final AtomicInteger completedOrders = new AtomicInteger(0);
    
    private static final int MAX_RETRY_ATTEMPTS = 3;

    public void registerTaxi(Taxi taxi) {
        taxis.add(taxi);
    }

    public void submitOrder(Order order) {
        try {
            if (!shutdownRequested) {
                orderQueue.put(order);
                totalOrders.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (!shutdownRequested || !orderQueue.isEmpty()) {
            try {
                Order order = orderQueue.take();
                assignOrderToClosestTaxi(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void assignOrderToClosestTaxi(Order order) {
        int attempts = 0;
        boolean orderAssigned = false;
        
        while (!orderAssigned && attempts < MAX_RETRY_ATTEMPTS && !shutdownRequested) {
            Taxi bestTaxi = null;
            double minDistance = Double.MAX_VALUE;
            
            for (Taxi taxi : taxis) {
                if (taxi.tryAcquireForAssignment()) {
                    try {
                        double distance = taxi.getCurrentLocation().distanceTo(order.getFrom());
                        if (distance < minDistance) {
                            if (bestTaxi != null) {
                                bestTaxi.releaseFromAssignment();
                            }
                            bestTaxi = taxi;
                            minDistance = distance;
                        } else {
                            taxi.releaseFromAssignment();
                        }
                    } catch (Exception e) {
                        taxi.releaseFromAssignment();
                    }
                }
            }
            
            if (bestTaxi != null) {
                try {
                    if (bestTaxi.assignOrder(order)) {
                        System.out.printf("[Dispatcher] Заказ #%d (расстояние: %.1f) назначен такси %d%n", 
                            order.getOrderId(), minDistance, bestTaxi.getId());
                        orderAssigned = true;
                    } else {
                        bestTaxi.releaseFromAssignment();
                        attempts++;
                    }
                } finally {
                    bestTaxi.releaseFromAssignment();
                }
            } else {
                lock.lock();
                try {
                    System.out.printf("[Dispatcher] Нет свободных такси для заказа #%d. Ожидание...%n", 
                        order.getOrderId());
                    taxiAvailableCondition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } finally {
                    lock.unlock();
                }
                attempts = 0; 
            }
        }
        
        if (!orderAssigned && !shutdownRequested) {
            System.out.printf("[Dispatcher] Не удалось назначить заказ #%d после %d попыток%n", 
                order.getOrderId(), MAX_RETRY_ATTEMPTS);
            try {
                orderQueue.put(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void reportTripCompletion(Taxi taxi, Order order) {
        int completed = completedOrders.incrementAndGet();
        System.out.printf("[Dispatcher] Такси %d завершило заказ #%d (всего завершено: %d)%n", 
            taxi.getId(), order.getOrderId(), completed);
        
        lock.lock();
        try {
            taxiAvailableCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        shutdownRequested = true;
        lock.lock();
        try {
            taxiAvailableCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void printFinalStats() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ФИНАЛЬНАЯ СТАТИСТИКА РАБОТЫ СИСТЕМЫ");
        System.out.println("=".repeat(60));
        
        System.out.printf("Всего заказов: %d%n", totalOrders.get());
        System.out.printf("Обработано заказов: %d%n", completedOrders.get());
        System.out.printf("В очереди: %d%n", orderQueue.size());
        
        System.out.println("\n🚕 Статус такси:");
        for (Taxi taxi : taxis) {
            System.out.printf("  • Такси %d - %s%n", 
                taxi.getId(), 
                taxi.isAvailable() ? "свободно" : "занято");
        }
        
        System.out.println("=".repeat(60));
    }
    
    public void clearQueue() {
        orderQueue.clear();
    }
}