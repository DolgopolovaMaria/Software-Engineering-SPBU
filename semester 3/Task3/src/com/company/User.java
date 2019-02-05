package com.company;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class User implements Runnable {

    private Queue<Date> queue;
    private volatile boolean active;
    private AtomicBoolean atomicBoolean;

    public User(Queue<Date> queue, AtomicBoolean atomicBoolean) {
        this.queue = queue;
        this.atomicBoolean = atomicBoolean;
        active = true;
    }

    public void stop() {
        active = false;
    }

    public void run() {
        while (active) {
            try {
                while (!atomicBoolean.compareAndSet(false, true)) {}
                Date tmp = queue.poll();
                if (tmp != null) {
                    System.out.println("Thread #" + Thread.currentThread().getId() + ": " + tmp);
                }
                atomicBoolean.set(false);
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
