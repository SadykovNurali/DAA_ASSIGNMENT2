package org.example;

import org.example.ds.DynamicArray;

public class DynamicArrayTest {
    public static void main(String[] args) {
        System.out.println("--- Running DynamicArray Tests ---");
        DynamicArray da = new DynamicArray(2);

        da.add(10);
        da.add(20);
        da.add(30); // Проверка динамического расширения

        assert da.size() == 3 : "Test Failed: Size should be 3";
        assert da.get(1) == 20 : "Test Failed: Index 1 should be 20";

        da.add(1, 15);
        assert da.get(1) == 15 : "Test Failed: Index 1 should be 15";

        int removed = da.remove(2);
        assert removed == 20 : "Test Failed: Removed value should be 20";

        boolean thrown = false;
        try {
            da.get(10);
        } catch (IndexOutOfBoundsException e) {
            thrown = true;
        }
        assert thrown : "Test Failed: IndexOutOfBoundsException expected";

        System.out.println("DynamicArray Tests: ALL PASSED!\n");
    }
}