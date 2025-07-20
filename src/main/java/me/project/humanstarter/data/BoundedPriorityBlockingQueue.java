package me.project.humanstarter.data;


import java.util.concurrent.PriorityBlockingQueue;

public class BoundedPriorityBlockingQueue<E> {
    private final PriorityBlockingQueue<E> queue;
    private final int capacity;

    public BoundedPriorityBlockingQueue(int capacity) {
        this.queue = new PriorityBlockingQueue<>();
        this.capacity = capacity;
    }

    public synchronized boolean offer(E e) {
        if (queue.size() >= capacity) {
            return false;
        }
        return queue.offer(e);
    }

    public E take() throws InterruptedException {
        return queue.take();
    }
}