package basic.alg.array;

public class ResizableArray<T> {
    private int size;
    private T[] data;
    private int capacity;
    public ResizableArray(int capacity) {
        this.capacity = capacity;
        data = (T[]) new Object[capacity];
    }

    public void add(T item) {
        if (size >= capacity) {
            resize(2 * capacity);
        }
        data[size] = item;
        size ++;
    }

    public void remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("指定的数组不存在！");
        }
        data[index] = null;
        for (int i=index; i<size; i++) {
            data[index] = data[index + 1];
        }
        size --;
        if (size <= capacity / 4 && capacity / 2 != 0) {
            resize(capacity / 2);
        }
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return data.length;
    }



    private void resize(int newCapacity) {
        if (newCapacity > 0) {
            T[] newData = (T[]) new Object[newCapacity];
            for (int i=0; i<size; i++) {
                newData[i] = data[i];
            }
            data = newData;
            capacity = newCapacity;
        } else {
            throw new IllegalArgumentException("参数错误！");
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i=0; i<size; i++) {
            if (i < size - 1) {
                sb.append(data[i] + ",");
            } else {
                sb.append(data[i]);
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
