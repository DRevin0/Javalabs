package main.java.com.taxi.model;

public class Location {
    private final int x;
    private final int y;
    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public double distanceTo(Location other){
        return Math.abs(this.x - other.x)+Math.abs(this.y - other.y);
    }

    @Override
    public String toString(){
        return "("+x+", "+y+")";
    }
}   
