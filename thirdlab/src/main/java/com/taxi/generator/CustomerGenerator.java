package main.java.com.taxi.generator;

import main.java.com.taxi.dispather.Dispather;
import main.java.com.taxi.model.Location;
import main.java.com.taxi.model.Order;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerGenerator implements Runnable {
    private final Dispather dispatcher;
    private final Random random = new Random();
    private final AtomicLong orderIdCounter = new AtomicLong(0);
    private static final int MAX_COORD = 100;

    public CustomerGenerator(Dispather dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Location from = new Location(random.nextInt(MAX_COORD), random.nextInt(MAX_COORD));
                Location to = new Location(random.nextInt(MAX_COORD), random.nextInt(MAX_COORD));
                Order order = new Order(from, to, Instant.now(), orderIdCounter.incrementAndGet());
                System.out.println("[Generator] Создан заказ: " + order);
                dispatcher.submitOrder(order);

                Thread.sleep(500 + random.nextInt(1500)); 
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}