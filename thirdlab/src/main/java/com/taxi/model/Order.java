package main.java.com.taxi.model;
import java.time.Instant;

public class Order{
    private final Location from;
    private final Location to;
    private final Instant timestamp;
    private final long orderId;
    public Order(Location from, Location to, Instant timestamp, long orderId){
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
        this.orderId = orderId;
    }
    
    public Location getFrom(){
        return from;
    }
    public Location getTo(){
        return to;
    }
    public Instant getTimestamp(){
        return timestamp;
    }
    public long getorderId(){
        return orderId;
    }

    @Override
    public String toString(){
        return "Order ("+"id = "+orderId+", from = "+from+", to = "+to+", timestamp = "+timestamp+")";
    }
}