package com.company;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable {

    private Queue<Date> queue;
    private AtomicBoolean atomicBoolean;
    private volatile boolean active;

    public Producer(Queue<Date> queue, AtomicBoolean atomicBoolean) {
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
                while (!atomicBoolean.compareAndSet(false, true)) {
                }
                queue.add(new Date());
                atomicBoolean.set(false);
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
