package sample;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;


public class ClientTester implements IClient, Callable<Long> {
    private BufferedImage bufferedImage;
    private String selectedFilter;
    private ClientSendReceive clientSendReceive;
    private volatile boolean finish;
    private long timeStart;
    private long timeFinish;

    public ClientTester(int serverPort, String serverIP, BufferedImage image, String selectedFilter){
        bufferedImage = image;
        this.selectedFilter = selectedFilter;
        clientSendReceive = new ClientSendReceive(serverPort, serverIP, this);
        clientSendReceive.receiveFilters();
        new Thread(clientSendReceive).start();
        finish = false;
    }

    public Long call(){
        finish = false;
        timeStart = System.currentTimeMillis();
        clientSendReceive.buttonStart();
        synchronized (clientSendReceive) {
            clientSendReceive.notify();
        }
        startWait();
        return timeFinish - timeStart;
    }
    public void disconnect(){
        clientSendReceive.buttonExit();
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public String getSelectedFilter() {
        return selectedFilter;
    }

    public void setProgressBarFilter(double value) {
    }

    public void setProgressBarSend(double value) {
    }

    public void setProgressBarReceive(double value) {
    }

    public void filterFinish(BufferedImage newBufferedImage) {
        bufferedImage = newBufferedImage;
        finish = true;
        timeFinish = System.currentTimeMillis();
        stopWait();
    }

    public synchronized void startWait() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
    }

    public synchronized void stopWait() {
        notify();
    }
}
