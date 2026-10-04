package org.example.ds;

import org.example.metrics.OperationCounter;

public class DynamicArray {
    private int[] data;
    private int size;
    public final OperationCounter counter = new OperationCounter();

    public DynamicArray() {
        this(10);
    }

    public DynamicArray(int capacity) {
        this.data = new int[capacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void add(int x) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[size] = x;
        counter.moves++;
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (size == data.length) {
            resize(data.length * 2);
        }
        for (int i = size; i > index; i--) {
            counter.steps++;
            data[i] = data[i - 1];
            counter.moves++;
        }
        data[index] = x;
        counter.moves++;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int removedValue = data[index];
        counter.steps++;
        for (int i = index; i < size - 1; i++) {
            counter.steps++;
            data[i] = data[i + 1];
            counter.moves++;
        }
        size--;
        return removedValue;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        counter.steps++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            counter.steps++;
            counter.comparisons++;
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    private void resize(int newCapacity) {
        int[] newData = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            counter.steps++;
            newData[i] = data[i];
            counter.moves++;
        }
        data = newData;
    }
}