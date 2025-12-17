package main.java.com.taxi.model;

import main.java.com.taxi.dispatcher.Dispatcher;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class Taxi implements Runnable {
    private final int id;
    private final AtomicReference<Location> currentLocation;
    private final BlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(1);
    private final ReentrantLock assignmentLock = new ReentrantLock();
    private final AtomicBoolean isAvailable = new AtomicBoolean(true);
    private volatile boolean shutdownRequested = false;
    private final Dispatcher dispatcher;
    
    private static final Order SHUTDOWN_ORDER = new Order(
        new Location(-1, -1), 
        new Location(-1, -1), 
        java.time.Instant.EPOCH, 
        -1
    );

    public Taxi(int id, Location startLocation, Dispatcher dispatcher) {
        this.id = id;
        this.currentLocation = new AtomicReference<>(startLocation);
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (!shutdownRequested) {
            try {
                Order order = orderQueue.take();
                if (order == SHUTDOWN_ORDER) {
                    break;
                }
                processOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log("Завершил работу");
    }

    private void processOrder(Order order) {
        if (order.getOrderId() == -1) {
            return;
        }
        
        Location pickup = order.getFrom();
        Location destination = order.getTo();

        double distanceToClient = currentLocation.get().distanceTo(pickup);
        log(String.format("Еду к клиенту %s (расстояние: %.1f)", pickup, distanceToClient));
        simulateTravel(distanceToClient);
        currentLocation.set(pickup);

        double distanceToDestination = pickup.distanceTo(destination);
        log(String.format("Перевожу клиента до %s (расстояние: %.1f)", destination, distanceToDestination));
        simulateTravel(distanceToDestination);
        currentLocation.set(destination);

        log("Поездка завершена. Возвращаю такси в свободные");
        isAvailable.set(true);
        dispatcher.reportTripCompletion(this, order);
    }

    private void simulateTravel(double distance) {
        try {
            Thread.sleep((long) (distance * 10));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean assignOrder(Order order) {
        if (isAvailable.compareAndSet(true, false)) {
            boolean offered = orderQueue.offer(order);
            if (!offered) {
                isAvailable.set(true);
                return false;
            }
            return true;
        }
        return false;
    }
    
    public boolean tryAcquireForAssignment() {
        if (assignmentLock.tryLock()) {
            if (isAvailable.get()) {
                return true;
            } else {
                assignmentLock.unlock();
            }
        }
        return false;
    }
    
    public void releaseFromAssignment() {
        if (assignmentLock.isHeldByCurrentThread()) {
            assignmentLock.unlock();
        }
    }

    public boolean isAvailable() {
        return isAvailable.get();
    }

    public Location getCurrentLocation() {
        return currentLocation.get();
    }

    public int getId() {
        return id;
    }

    public void shutdown() {
        shutdownRequested = true;
        try {
            orderQueue.offer(SHUTDOWN_ORDER);
        } catch (Exception e) {
        }
    }

    private void log(String message) {
        System.out.printf("[Taxi-%d] %s%n", id, message);
    }
}