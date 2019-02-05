package Server;

import SenderReciever.SenderReciever;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerSendReceive implements Runnable{
    private Server server;
    private Filters filtersWorker;
    private DataInputStream in;
    private DataOutputStream out;

    private volatile boolean workClient;
    private volatile boolean workFilter;

    public ServerSendReceive(Socket socket, Server server) {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.server = server;
        this.filtersWorker = new Filters();
        workClient = true;
    }

    private void sendFilters() {
        String str = "";
        for (int i = 0; i < server.getFilters().size(); i++) {
            String filter = server.getFilters().get(i).toString();
            str += filter + " ";

        }
        SenderReciever.send(out, str.trim());
    }

    public void run() {
        sendFilters();
        while (workClient) {
            clientProcess();
        }
        System.out.println("stop processClient");
    }

    private void clientProcess(){
        if (!workFilter) {
            String messageFromClient;
            System.out.println("want to receive");
            messageFromClient = SenderReciever.receive(in);
            System.out.println("received");
            if(!checkBreak(messageFromClient)){
                if(messageFromClient.equals("start")) {
                    SenderReciever.received(out);
                    startFilterProcess();
                }
            }
               else {
                   System.out.println("workClient = " + workClient);
                   return;
               }
        }
        else {
            filterProcess();
        }
    }

    private void filterProcess() {
        while (workFilter) {
            SenderReciever.send(out, "work");

            String messageFromClient = SenderReciever.receive(in);
            if(checkBreak(messageFromClient)){
                filtersWorker.stop();
                return;
            }

            SenderReciever.send(out, Double.toString(filtersWorker.getProgress()));
            messageFromClient = SenderReciever.receive(in);
            if(checkBreak(messageFromClient)){
                filtersWorker.stop();
                return;
            }

            if (filtersWorker.isFinish()) {
                SenderReciever.send(out,"finish");
                SenderReciever.receive(in);
                sendImage(filtersWorker.getResultImage());
                SenderReciever.received(out);
                workFilter = false;
            }
        }

    }

    private boolean checkBreak(String message){
        if (message.equals("disconnect")) {
            workFilter = false;
            workClient = false;
            System.out.println("disconnect");
            return true;
        }
        if (message.equals("break")) {
            workFilter = false;
            System.out.println("break");
            return true;
        }
        return false;
    }

    private void startFilterProcess() {
        BufferedImage image = receiveImage();
        if(image == null){
            return;
        }
        SenderReciever.received(out);
        String filter = SenderReciever.receive(in);
        SenderReciever.received(out);
        Filters.FilterType type = Filters.FilterType.valueOf(filter);
        if(type != null) {
            filtersWorker.start(image, type);
            new Thread(filtersWorker).start();
            workFilter = true;
        }
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
            if(checkBreak(receivedLine)){
                return null;
            }
            String[] line = receivedLine.split(" ");

            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, Integer.parseInt(line[j]));
            }
            SenderReciever.received(out);
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
            SenderReciever.send(out, imageString.trim());
            String message = SenderReciever.receive(in);
            if(checkBreak(message)){
                return;
            }
        }
    }
}
