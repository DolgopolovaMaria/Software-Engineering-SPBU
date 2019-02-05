package Test;

import Server.Server;
import sample.ClientTester;

import java.util.concurrent.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        int amountOfClient = 10;
        int amountOfIteration = 10;

        File imageFile = new File("image1.bmp");

        List<Long> timeList = new ArrayList<>();
        for(int j = 0; j < amountOfIteration; j++) {
            List<ClientTester> listOfClient = new ArrayList<>();
            for (int i = 0; i < amountOfClient; i++) {
                BufferedImage image;
                try {
                    image = ImageIO.read(imageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                listOfClient.add(new ClientTester(1000, Server.serverIP, image, "average"));
            }
            ExecutorService executor = Executors.newFixedThreadPool(amountOfClient);

            List<Future<Long>> futureList = new ArrayList<Future<Long>>();
            for (int i = 0; i < amountOfClient; i++) {
                Future<Long> future = executor.submit(listOfClient.get(i));
                futureList.add(future);
            }

            List<Long> timeListIteration = new ArrayList<>();
            for (Future<Long> future : futureList) {
                try {
                    timeListIteration.add(future.get(200000, TimeUnit.MILLISECONDS));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    System.out.println("Timeout exception");
                    executor.shutdown();
                    return;
                }
            }
            executor.shutdown();

            for (int i = 0; i < amountOfClient; i++) {
                listOfClient.get(i).disconnect();
            }
            timeList.add(timeListIteration.get(0));
        }
        for (int i = 0; i < amountOfIteration; i++){
            System.out.println(timeList.get(i));
        }
        long median = timeList.get(amountOfIteration / 2);
        long average = average(timeList);
        System.out.println("median = " + median);
        System.out.println("average = " + average);
    }

    static long average(List<Long> list){
        int sum = 0;
        for(int i = 0; i < list.size(); i++){
            sum += list.get(i);
        }
        return sum / list.size();
    }
}
