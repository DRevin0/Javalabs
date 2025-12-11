package main.java.com.taxi.model;

import main.java.com.taxi.dispather.Dispather;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class Taxi implements Runnable{
    private final int id;
    private Location currentLocation;
    private volatile boolean available = true;
    private final BlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(1);
    private final Dispather dispather;
    
    public Taxi(int id, Location startLocation, Dispather dispather){
        this.id = id;
        this.currentLocation = startLocation;
        this.dispather = dispather;
    }
    @Override
    public void run(){//Основной цикл
        System.out.println("Такси " + id + " запущено и готово принимать заказы.");
    }

    public int getId(){
        return id;
    }
    public boolean isAvailable(){
        return available;
    }
    public Location getCurrentLocation(){
        return currentLocation;
    }
}