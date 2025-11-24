package collections;
public class Queue<T>{
    private static final int DEFAULT_CAPACITY = 10;
    private static final int RESIZE_FACTOR = 2;

    private T[] array;
    private int head;
    private int tail;
    private int size;
    private int capacity;
    @SuppressWarnings("unchecked")
    public Queue(){
        this.head = 0;
        this.tail = 0;
        this.size = 0;
        this.capacity = DEFAULT_CAPACITY;
        this.array = (T[]) new Object[capacity];
    }
    @SuppressWarnings("unchecked")
    public void enqueue(T element){
        if(size == capacity){
            T[] newArray = (T[]) new Object[capacity*RESIZE_FACTOR];
            for(int i = 0; i< size;i++){
                newArray[i] = array[(head+i)%capacity];
            }
            array = newArray;
            head = 0;
            tail = size;
            capacity = capacity * RESIZE_FACTOR;
        }
        array[tail] = element;
        tail = (tail+1)%capacity;
        size++;
    }
    public T dequeue(){
        if(size == 0){
            throw new IllegalStateException("Queue is empty");
        }
        T element = array[head];
        array[head] = null;
        head = (head + 1)% capacity;
        size--;
        return element;
    }
    public T peek(){
        if(size == 0){
            throw new IllegalStateException("Queue is empty");
        }
        return array[head];
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
}