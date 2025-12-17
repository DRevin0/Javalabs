package main.java.com.taxi;

import main.java.com.taxi.dispather.Dispather;
import main.java.com.taxi.generator.CustomerGenerator;
import main.java.com.taxi.model.Location;
import main.java.com.taxi.model.Taxi;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Dispather dispather = new Dispather();
        Thread dispatcherThread = new Thread(dispather, "Dispather");
        dispatcherThread.start();

        for (int i = 1; i <= 3; i++) {
            Taxi taxi = new Taxi(i, new Location(i * 20, i * 20), dispather);
            dispather.registerTaxi(taxi);
            new Thread(taxi, "Taxi-" + i).start();
        }

        CustomerGenerator generator = new CustomerGenerator(dispather);
        Thread generatorThread = new Thread(generator, "CustomerGenerator");
        generatorThread.start();

        Thread.sleep(30_000);

        generatorThread.interrupt();
        dispatcherThread.interrupt();
        System.out.println("Система завершает работу...");
    }
}