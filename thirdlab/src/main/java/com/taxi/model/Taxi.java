package main.java.com.taxi.model;

import main.java.com.taxi.dispather.Dispather;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Taxi implements Runnable {
    private final int id;
    private Location currentLocation;
    private final BlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(1);
    private final ReentrantLock lock = new ReentrantLock();
    private boolean isAvailable = true;
    private final Dispather dispatcher;

    public Taxi(int id, Location startLocation, Dispather dispatcher) {
        this.id = id;
        this.currentLocation = startLocation;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Order order = orderQueue.take();
                processOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log("Завершил работу");
    }

    private void processOrder(Order order) {
        double distanceToClient = currentLocation.distanceTo(order.getFrom());
        log("Еду к клиенту " + order.getFrom() + " за " + (int) distanceToClient + " мс");
        simulateTravel(distanceToClient);
        currentLocation = order.getFrom();

        double distanceToDestination = order.getFrom().distanceTo(order.getTo());
        log("Перевожу до " + order.getTo() + " за " + (int) distanceToDestination + " мс");
        simulateTravel(distanceToDestination);
        currentLocation = order.getTo();

        log("Поездка завершена. Возвращаю такси в свободные");
        setAvailable(true);
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
        lock.lock();
        try {
            if (!isAvailable) return false;
            setAvailable(false);
            orderQueue.offer(order);
            return true;
        } finally {
            lock.unlock();
        }
    }

    private void setAvailable(boolean available) {
        lock.lock();
        try {
            this.isAvailable = available;
        } finally {
            lock.unlock();
        }
    }

    public boolean isAvailable() {
        lock.lock();
        try {
            return isAvailable;
        } finally {
            lock.unlock();
        }
    }

    public Location getCurrentLocation() {
        lock.lock();
        try {
            return currentLocation;
        } finally {
            lock.unlock();
        }
    }

    public int getId() {
        return id;
    }

    private void log(String message) {
        System.out.printf("[Taxi-%d] %s%n", id, message);
    }
}