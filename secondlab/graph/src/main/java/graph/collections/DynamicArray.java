package collections;

public class DynamicArray<T>{
    private int size;
    private int capacity;
    private T[] array;

    public DynamicArray(int initialCapacity){
        this.capacity = initialCapacity;
        this.size = 0;
        this.array = (T[]) new Object[capacity];
    }

    public T get(int index){
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return array[index];
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
    public boolean contains(T element){
        for(int i = 0;i<size;i++){
            if(element == null){
                if(array[i] == null){
                    return true;
                }
            }
            else{
                if(element.equals(array[i])){
                    return true;
                }
            }
        }
        return false;
    }
    public int getIndexOf(T element){
        for(int i = 0;i<size;i++){
            if(element == null){
                if(array[i] == null){
                    return i;
                }
            }
            else{
                if(element.equals(array[i])){
                    return i;
                }
            }
        }
        return -1;
    }
    public T getLast(){
        if(isEmpty()){
            throw new IllegalStateException("Array is empty");
        }
        return array[size-1];
    }
    public T getFirst(){
        if(isEmpty()){
            throw new IllegalStateException("Array is empty");
        }
        return array[0];
    }

    public void add(T element){
        if(size == capacity){
            T[] newArray = (T[]) new Object[capacity*2];
            for(int i = 0;i<size;i++){
                newArray[i] = array[i];
            }
            array = newArray;
        }
        array[size] = element;
        size++;
    }

    public void add(int index, T element){
        if(index > size || index < 0){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if(size == capacity){
            T[] newArray = (T[]) new Object[capacity*2];
            for(int i = 0;i<size;i++){
                newArray[i] = array[i];
            }
            array = newArray;
        }
        for(int i = size;i>index;i--){
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++;
    }

    public void set(int index, T element){
        if(index > size || index < 0){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        array[index] = element;
    }
    public T remove(int index){
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T removed = array[index];
        for(int i = index;i<size-1;i++){
            array[i] = array[i+ 1];
        }
        array[size - 1] = null;
        size--;
        return removed;
    }
    public boolean remove(T element){
        int index = getIndexOf(element);
        if(index != -1){
            remove(index);
            return true;
        }
        return false;
    }
    public void clear(){
        for(int i = 0;i<size;i++){
            array[i] = null;
        }
        size = 0;
    }
}