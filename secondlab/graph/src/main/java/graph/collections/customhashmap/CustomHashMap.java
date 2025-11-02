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
        for (int i = 0; i < capacity; i++) {
            buckets.add(null);
        }
    }
    public int hash(K key){
        if(key == null){
            return 0;
        }
        else{
            return Math.abs(key.hashCode())%capacity;
        }
    }
    public void rehash() {
        int oldCapacity = this.capacity;
        this.capacity *= 2;

        DynamicArray<Entry<K, V>> newBuckets = new DynamicArray<Entry<K, V>>(this.capacity);
        for (int i = 0; i < this.capacity; i++) {
            newBuckets.add(null);
        }
        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> current = this.buckets.get(i);
            while (current != null) {
                Entry<K, V> next = current.getNext();
                
                int newIndex = hash(current.getKey());

                Entry<K, V> existingHead = newBuckets.get(newIndex);
                current.setNext(existingHead);
                newBuckets.set(newIndex, current);
                
                current = next;
            }
        }
        
        this.buckets = newBuckets;
    }
    public void put(K key, V value){
        if(key == null){
            return;
        }
        if(size >= capacity*LOAD_FACTOR){
            rehash();
        }
        int index = hash(key);
        Entry<K, V> newEntry = new Entry<>(key, value);
        if(buckets.get(index) == null){
            buckets.set(index, newEntry);
            size++;
        }
        else{
            Entry<K, V> current = buckets.get(index);
            Entry<K, V> prev = null;
            while(current != null){
                if(current.getKey().equals(key)){
                    current.setValue(value);
                    return;
                }
                prev = current;
                current = current.getNext();
            }
            prev.setNext(newEntry);
            size++;
        }

    }
    public V get(K key){
        if(key==null){
            return null;
        }
        int index = hash(key);
        Entry<K, V> current = buckets.get(index);
        while(current != null){
            if(current.getKey().equals(key)){
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }
    public boolean containsKey(K key){
        if (key == null){
            return false;
        }
        int index = hash(key);
        Entry<K, V> current = buckets.get(index);
        while (current != null) {
            if (current.getKey().equals(key)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
    public int getSize(){
        return size;
    }
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }
    public V remove(K key) {
        if (key == null){
            return null;
        }
        int index = hash(key);
        Entry<K, V> current = buckets.get(index);
        Entry<K, V> prev = null;
        
        while (current != null) {
            if (current.getKey().equals(key)) {
                if (prev == null) {
                    buckets.set(index, current.getNext());
                } 
                else {
                    prev.setNext(current.getNext());
                }
                size--;
                return current.getValue();
            }
            prev = current;
            current = current.getNext();
        }
        return null;
    }
}
