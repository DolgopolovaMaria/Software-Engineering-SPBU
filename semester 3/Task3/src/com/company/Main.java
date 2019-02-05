package com.company;

import java.io.IOException;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static final int NUM_OF_THREADS = 2;

    public static void main(String[] args) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Queue<Date> queue = new PriorityQueue<>();
        ExecutorService producers = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService users = Executors.newFixedThreadPool(NUM_OF_THREADS);
        Producer[] producersArray = new Producer[NUM_OF_THREADS];
        User[] usersArray = new User[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            producersArray[i] = new Producer(queue, atomicBoolean);
            producers.submit(producersArray[i]);
            usersArray[i] = new User(queue, atomicBoolean);
            users.submit(usersArray[i]);
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            producersArray[i].stop();
            usersArray[i].stop();
        }
        producers.shutdown();
        users.shutdown();
    }
}
