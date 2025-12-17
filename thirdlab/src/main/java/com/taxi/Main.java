package main.java.com.taxi;

import main.java.com.taxi.dispatcher.Dispatcher;
import main.java.com.taxi.generator.CustomerGenerator;
import main.java.com.taxi.model.Location;
import main.java.com.taxi.model.Taxi;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dispatcher dispatcher = new Dispatcher();

        System.out.print("Введите длительность работы системы в секундах (рекомендуется 30-60): ");
        int durationSeconds = scanner.nextInt();
        while (durationSeconds <= 0) {
            System.out.print("Некорректное значение. Повторите ввод: ");
            durationSeconds = scanner.nextInt();
        }
        scanner.close();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Запуск системы управления беспилотными такси");
        System.out.println("=".repeat(50));

        Thread dispatcherThread = new Thread(dispatcher, "Dispatcher");
        dispatcherThread.start();

        int taxiCount = 3;
        List<Taxi> taxis = new ArrayList<>();
        List<Thread> taxiThreads = new ArrayList<>();
        
        for (int i = 0; i < taxiCount; i++) {
            Location startLocation = new Location(i * 30 % 100, i * 25 % 100);
            Taxi taxi = new Taxi(i + 1, startLocation, dispatcher);
            taxis.add(taxi);
            dispatcher.registerTaxi(taxi);
            Thread taxiThread = new Thread(taxi, "Taxi-" + (i + 1));
            taxiThreads.add(taxiThread);
            taxiThread.start();
        }

        CustomerGenerator generator = new CustomerGenerator(dispatcher);
        Thread generatorThread = new Thread(generator, "CustomerGenerator");
        generatorThread.start();

        try {
            Thread.sleep(durationSeconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Начало graceful shutdown системы...");
        System.out.println("=".repeat(50));
        
        generator.shutdown();
        generatorThread.interrupt();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        dispatcher.shutdown();
        dispatcher.clearQueue();
        dispatcherThread.interrupt();
        
        for (Taxi taxi : taxis) {
            taxi.shutdown();
        }
        try {
            generatorThread.join(1000);
            dispatcherThread.join(2000);
            for (Thread taxiThread : taxiThreads) {
                taxiThread.join(3000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        dispatcher.printFinalStats();
        
        System.out.println("Система полностью остановлена");
    }
}