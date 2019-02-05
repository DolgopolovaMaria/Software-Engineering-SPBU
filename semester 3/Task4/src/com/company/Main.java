package com.company;

public class Main {

    public static void main(String[] args) {
	    MyThreadPool pool = new MyThreadPool();
        for (int i = 0; i < 4; i++) {
            pool.enqueue(new RandomPrint(i));
        }
        Object obj = new Object();
        synchronized (obj) {
            try {
                obj.wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pool.close();
    }
}
