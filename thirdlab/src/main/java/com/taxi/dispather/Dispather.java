package main.java.com.taxi.dispather;

import main.java.com.taxi.model.Order;
import main.java.com.taxi.model.Taxi;
import main.java.com.taxi.model.Location; 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue; 
import java.util.concurrent.locks.ReentrantLock;

public class Dispather implements Runnable {
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final List<Taxi> taxis = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public void registerTaxi(Taxi taxi) {
        lock.lock();
        try {
            taxis.add(taxi);
        } finally {
            lock.unlock();
        }
    }

    public void submitOrder(Order order) {
        try {
            orderQueue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
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
        Taxi bestTaxi = findClosestAvailableTaxi(order.getFrom());
        if (bestTaxi != null && bestTaxi.assignOrder(order)) {
            System.out.printf("[Dispather] Заказ #%d назначен такси %d%n", 
                order.getOrderId(), bestTaxi.getId());
        } else {
            try {
                orderQueue.put(order);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Taxi findClosestAvailableTaxi(Location pickup) {
        lock.lock();
        try {
            Taxi best = null;
            double minDistance = Double.MAX_VALUE;

            for (Taxi taxi : taxis) {
                if (taxi.isAvailable()) {
                    double distance = taxi.getCurrentLocation().distanceTo(pickup);
                    if (distance < minDistance) {
                        minDistance = distance;
                        best = taxi;
                    }
                }
            }
            return best;
        } finally {
            lock.unlock();
        }
    }

    public void reportTripCompletion(Taxi taxi, Order order) {
        System.out.printf("[Dispather] Такси %d завершило заказ #%d%n", taxi.getId(), order.getOrderId());
    }
}