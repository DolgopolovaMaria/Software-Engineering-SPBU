package sample;

import SenderReciever.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ClientSendReceive implements Runnable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private IClient client;
    private Lock lock;

    private volatile boolean workClient;
    private volatile boolean startFilter;
    private volatile boolean breakFilter;
    private volatile boolean stopWork;
    private volatile boolean workFilter;

    public ClientSendReceive(int serverPort, String serverIP, IClient client){
        try {
            socket = new Socket(InetAddress.getByName(serverIP), serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            this.client = client;
            lock = new ReentrantLock();
            workClient = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonStart(){
        startFilter = true;
        client.setProgressBarSend(0);
        client.setProgressBarFilter(0);
        client.setProgressBarReceive(0);
    }

    public void buttonBreak(){
        startFilter = false;
        breakFilter = true;
        workFilter = false;
    }

    public void buttonExit(){
        if(!workClient) {
            sendDisconnect();
            System.out.println("disconnect sended");
        }
        stopWork = true;
    }

    public void run() {
        startWaiting();
        while (workClient) {
            lock.lock();
            clientProcess();
            lock.unlock();
        }
    }

    private void clientProcess(){
        if(stopWork) {
            System.out.println("disconnect");
            sendDisconnect();
            workClient = false;
        }
        else if(breakFilter) {
            sendBreak();
            breakFilter = false;
            workFilter = false;
            client.setProgressBarFilter(0);
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(startFilter) {
            startFilter = false;
            workFilter = true;
            sendStartFilterProcess();
        }
        else if(workFilter) {
            filterProcess();
        }
    }

    private void filterProcess() {
        while (workFilter){
            String messageFromServer = SenderReciever.receive(in);
            if(messageFromServer.equals("finish")) {
                SenderReciever.received(out);
                BufferedImage image = receiveImage();
                if(image == null){
                    breakFilter = false;
                    workFilter = false;
                    return;
                }
                SenderReciever.received(out);
                client.filterFinish(image);
                workFilter = false;
            }
            else if(messageFromServer.equals("work")){
                if(stopWork){
                    sendDisconnect();
                    return;
                }
                //SenderReciever.received(out);
                if(stopWork){
                    sendDisconnect();
                    return;
                }
                else {
                    SenderReciever.received(out);
                }
                double newProgress = Double.parseDouble(SenderReciever.receive(in));
                client.setProgressBarFilter(newProgress);
                if(stopWork){
                    sendDisconnect();
                    return;
                }
                SenderReciever.received(out);
            }
        }
    }

    private void sendStartFilterProcess(){
        SenderReciever.send(out,"start");
        SenderReciever.receive(in);
        sendImage(client.getBufferedImage());
            if(breakFilter){
                breakFilter = false;
                workFilter = false;
                return;
            }
        SenderReciever.receive(in);
        SenderReciever.send(out, client.getSelectedFilter());
    }

    private void sendBreak(){
        SenderReciever.send(out,"break");
    }

    private void sendDisconnect(){
        SenderReciever.send(out,"disconnect");
    }

    public LinkedList<String> receiveFilters() {
        LinkedList<String> list = new LinkedList<>();
        try {
            String str = SenderReciever.receive(in);
            String[] filters = str.split(" ");
            for (int i = 0; i < filters.length; i++) {
                list.add(filters[i]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private BufferedImage receiveImage(){
        String widthHeightType = SenderReciever.receive(in);
        String[] str = widthHeightType.split(" ");
        int width = Integer.parseInt(str[0]);
        int height = Integer.parseInt(str[1]);
        int type = Integer.parseInt(str[2]);

        SenderReciever.received(out);
        BufferedImage image = new BufferedImage(width, height, type);
        for (int i = 0; i < width; i++) {
            String receivedLine = SenderReciever.receive(in);
            String[] line = receivedLine.split(" ");

            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, Integer.parseInt(line[j]));
            }

            if(breakFilter || stopWork){
                sendBreak();
                client.setProgressBarReceive(0);
                workFilter = false;
                //breakFilter = false;
                if(!stopWork){
                    try {
                        synchronized (this) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    sendDisconnect();
                }
                return null;
            }
            else{
                client.setProgressBarReceive((double)i / width);
                SenderReciever.received(out);
            }
        }
        return image;
    }

    private void sendImage(BufferedImage bufferedImage) {
        if(bufferedImage == null) {
            return;
        }
        String widthHeightType = String.valueOf(bufferedImage.getWidth()) + " " + String.valueOf(bufferedImage.getHeight())
                + " " + String.valueOf(bufferedImage.getType());
        SenderReciever.send(out, widthHeightType);
        SenderReciever.receive(in);
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            String imageString = "";
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                imageString += String.valueOf(bufferedImage.getRGB(i, j)) + " ";

            }
            if(breakFilter || stopWork){
                sendBreak();
                client.setProgressBarSend(0);
                workFilter = false;
                //breakFilter = false;
                if(!stopWork){
                    try {
                        synchronized (this) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    sendDisconnect();
                }
                return;
            }
            else{
                SenderReciever.send(out, imageString.trim());
                SenderReciever.receive(in);
                client.setProgressBarSend((double)i / bufferedImage.getWidth());
            }
        }
    }

    private void startWaiting(){
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
