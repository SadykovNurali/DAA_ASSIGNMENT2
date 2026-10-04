package org.example;

import org.example.ds.MyLinkedList;

public class MyLinkedListTest {
    public static void main(String[] args) {
        System.out.println("--- Running MyLinkedList Tests ---");
        MyLinkedList list = new MyLinkedList();

        list.add(100);
        list.add(200);
        list.add(0, 50);

        assert list.size() == 3 : "Test Failed: Size should be 3";
        assert list.get(0) == 50 : "Test Failed: Index 0 should be 50";
        assert list.contains(200) : "Test Failed: List should contain 200";

        int removed = list.remove(1);
        assert removed == 100 : "Test Failed: Removed element should be 100";

        boolean thrown = false;
        try {
            list.get(5);
        } catch (IndexOutOfBoundsException e) {
            thrown = true;
        }
        assert thrown : "Test Failed: IndexOutOfBoundsException expected";

        System.out.println("MyLinkedList Tests: ALL PASSED!\n");
    }
}