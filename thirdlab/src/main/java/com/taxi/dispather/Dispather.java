package main.java.com.taxi.dispather;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.ArrayList;

import main.java.com.taxi.model.Location;
import main.java.com.taxi.model.Order;
import main.java.com.taxi.model.Taxi;

public class Dispather implements Runnable {
    private final BlockingQueue<Order> incomingOrders = new LinkedBlockingDeque<>();
    
    private final List<Taxi> taxis = new ArrayList<>();
    private final ReentrantLock taxisLock = new ReentrantLock();

    public Dispather(){}

    public void registerTaxi(Taxi taxi){
        taxisLock.lock();
        try{
            taxis.add(taxi);
        }
        finally{
            taxisLock.unlock();
        }
    }

    public void submitOrder(Order order){
        try{
            incomingOrders.put(order);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run(){}//Реализация основного цикла

    private Taxi findClosestAvaibleTaxi(Location pickup){//Реализация поиска свободного такси

    };
    public void reportTripCompletion(Taxi taxi, Order order){//Функция после завершения поездки

    };
}