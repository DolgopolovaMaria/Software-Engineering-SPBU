package com.company;

import java.util.Random;

public class RandomPrint implements Runnable {
    private Random random;
    private int ID;

    public RandomPrint(int ID) {
        random = new Random();
        this.ID = ID;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Thread #" + ID + ": " + random.nextInt(100));
        }
    }
}
