package com.company;


import java.util.concurrent.atomic.AtomicBoolean;

public class MyThreadPool implements AutoCloseable {
    private final MyConcurrentQueue<Runnable> queue = new MyConcurrentQueue<Runnable>();
    private volatile AtomicBoolean flag;
    private static final int NUM_OF_THREADS = 2;
    private Sync[] syncs;

    public MyThreadPool() {
        syncs = new Sync[NUM_OF_THREADS];
        for (int i = 0; i < syncs.length; i++) {
            syncs[i] = new Sync();
        }
        flag = new AtomicBoolean();
        flag.set(true);
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            new Thread(new Runner(queue, flag, syncs[i])).start();
        }
    }

    public void enqueue(Runnable task) {
        if (flag.get()) {

            queue.offer(task);
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {

            if (!syncs[i].isFlag()) {
                synchronized (syncs[i].getSync()) {
                    syncs[i].getSync().notify();
                }
                break;
            }
        }
    }

    public void close() {
        flag.set(false);
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            if (!syncs[i].isFlag()) {
                synchronized (syncs[i].getSync()) {
                    syncs[i].getSync().notify();
                }
            }
        }
    }
}
