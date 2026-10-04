package org.example.ds;

import org.example.metrics.OperationCounter;

public class MinHeap {
    private int[] heap;
    private int size;
    public final OperationCounter counter = new OperationCounter();

    public MinHeap() {
        this(10);
    }

    public MinHeap(int capacity) {
        this.heap = new int[capacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void insert(int x) {
        if (size == heap.length) {
            resize(heap.length * 2);
        }
        heap[size] = x;
        counter.moves++;
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        counter.steps++;
        return heap[0];
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int minVal = heap[0];
        counter.steps++;

        heap[0] = heap[size - 1];
        counter.moves++;
        size--;

        if (size > 0) {
            bubbleDown(0);
        }
        return minVal;
    }

    // Бонус +5%: Построение кучи за O(n) по алгоритму Флойда
    public static MinHeap buildHeap(int[] array) {
        MinHeap h = new MinHeap(array.length);
        System.arraycopy(array, 0, h.heap, 0, array.length);
        h.size = array.length;
        h.counter.moves += array.length;

        for (int i = (h.size / 2) - 1; i >= 0; i--) {
            h.bubbleDown(i);
        }
        return h;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            counter.steps += 2;
            counter.comparisons++;
            if (heap[index] < heap[parentIndex]) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size) {
                counter.steps += 2;
                counter.comparisons++;
                if (heap[left] < heap[smallest]) {
                    smallest = left;
                }
            }

            if (right < size) {
                counter.steps += 2;
                counter.comparisons++;
                if (heap[right] < heap[smallest]) {
                    smallest = right;
                }
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        counter.moves += 3;
    }

    private void resize(int newCapacity) {
        int[] newHeap = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            counter.steps++;
            newHeap[i] = heap[i];
            counter.moves++;
        }
        heap = newHeap;
    }
}