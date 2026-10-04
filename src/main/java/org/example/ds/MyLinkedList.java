package org.example.ds;

import org.example.metrics.OperationCounter;

public class MyLinkedList {
    private static class Node {
        int value;
        Node next;
        Node prev;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    public final OperationCounter counter = new OperationCounter();

    public int size() {
        return size;
    }

    public void add(int x) {
        Node newNode = new Node(x);
        counter.moves++;
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            counter.moves++;
            newNode.prev = tail;
            counter.moves++;
            tail = newNode;
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node newNode = new Node(x);
        counter.moves++;
        if (index == 0) {
            newNode.next = head;
            counter.moves++;
            if (head != null) {
                head.prev = newNode;
                counter.moves++;
            }
            head = newNode;
            if (tail == null) tail = head;
        } else {
            Node curr = getNode(index);
            Node prevNode = curr.prev;

            newNode.next = curr;
            counter.moves++;
            newNode.prev = prevNode;
            counter.moves++;
            prevNode.next = newNode;
            counter.moves++;
            curr.prev = newNode;
            counter.moves++;
        }
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node target = getNode(index);
        int val = target.value;

        if (target.prev != null) {
            target.prev.next = target.next;
            counter.moves++;
        } else {
            head = target.next;
        }

        if (target.next != null) {
            target.next.prev = target.prev;
            counter.moves++;
        } else {
            tail = target.prev;
        }

        size--;
        return val;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).value;
    }

    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            counter.steps++;
            counter.comparisons++;
            if (curr.value == x) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    private Node getNode(int index) {
        Node curr;
        if (index < (size >> 1)) {
            curr = head;
            for (int i = 0; i < index; i++) {
                counter.steps++;
                curr = curr.next;
            }
        } else {
            curr = tail;
            for (int i = size - 1; i > index; i--) {
                counter.steps++;
                curr = curr.prev;
            }
        }
        counter.steps++;
        return curr;
    }
}