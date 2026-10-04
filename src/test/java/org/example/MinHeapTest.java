package org.example;

import org.example.ds.MinHeap;
import java.util.Random;

public class MinHeapTest {
    public static void main(String[] args) {
        System.out.println("--- Running MinHeap Tests ---");
        MinHeap heap = new MinHeap();
        Random rand = new Random(42);

        for (int i = 0; i < 100; i++) {
            heap.insert(rand.nextInt(1000));
        }

        assert heap.size() == 100 : "Test Failed: Size should be 100";

        int prev = Integer.MIN_VALUE;
        while (heap.size() > 0) {
            int current = heap.extractMin();
            assert current >= prev : "Test Failed: Heap property violated, elements not sorted";
            prev = current;
        }

        boolean thrown = false;
        try {
            heap.extractMin();
        } catch (IllegalStateException e) {
            thrown = true;
        }
        assert thrown : "Test Failed: IllegalStateException expected on empty heap";

        System.out.println("MinHeap Tests: ALL PASSED!\n");
    }
}