package com.company;


import java.util.concurrent.atomic.AtomicBoolean;

public class Runner implements Runnable {

    private MyConcurrentQueue<Runnable> queue;
    private volatile AtomicBoolean flag;
    private Sync sync;

    public Runner(MyConcurrentQueue<Runnable> queue, AtomicBoolean flag, Sync sync) {
        this.flag = flag;
        this.sync = sync;
        this.queue = queue;
    }

    public void run() {
        while (flag.get()) {
            Runnable nextTask = queue.poll();
            if (nextTask != null) {
                synchronized (sync.getSync()){
                    sync.setFlag(true);
                }
                nextTask.run();
            } else {
                synchronized (sync.getSync()) {
                    try {
                        sync.setFlag(false);
                        sync.getSync().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
