package collections.customhashmap;
import collections.DynamicArray;
public class CustomHashMap<K, V>{
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private DynamicArray<Entry<K, V>> buckets;
    private int capacity;
    private int size;
    public CustomHashMap(){
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.buckets = new DynamicArray<Entry<K, V>>(capacity);
    }
    private int hash(K key){
        if(key == null){
            return 0;
        }
        else{
            return Math.abs(key.hashCode())%capacity;
        }
    }
    
}