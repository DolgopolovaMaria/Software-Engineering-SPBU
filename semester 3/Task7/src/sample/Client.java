package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Client implements IClient {
    private Controller controller;
    private BufferedImage bufferedImage;
    private BufferedImage oldImage;
    private String selectedFilter;
    private ClientSendReceive clientSendReceive;

    public Client(int serverPort, String serverIP, Controller controller) {
        this.controller = controller;
        controller.setClient(this);
        clientSendReceive = new ClientSendReceive(serverPort, serverIP, this);
        setFiltersList();
        new Thread(clientSendReceive).start();
    }

    public String getSelectedFilter() {
        return selectedFilter;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }


    public void buttonLoad(File image) {
        try {
            bufferedImage = ImageIO.read(image);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonStart() {
        synchronized (clientSendReceive) {
            clientSendReceive.notify();
        }
        selectedFilter = controller.getComboboxFilters().getSelectionModel().getSelectedItem();
        setProgressBarFilter(0);
        clientSendReceive.buttonStart();
    }

    public void buttonBreak() {
        clientSendReceive.buttonBreak();
    }

    public void buttonExit() {
        synchronized (clientSendReceive) {
            clientSendReceive.notify();
        }
        clientSendReceive.buttonExit();
    }

    public void buttonUndo(){
        if(oldImage == null){
            return;
        }
        bufferedImage = oldImage;
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        controller.getImageView().setImage(image);
    }

    private void setFiltersList() {
        List<String> listFilters = clientSendReceive.receiveFilters();
        controller.getComboboxFilters().getItems().addAll(listFilters);
    }

    private double checkProgressValue(double value){
        if(value < 0) {
            return 0;
        } else if (value > 1) {
            return 1;
        }
        return value;
    }

    public void setProgressBarFilter(double value) {
        controller.getProgressBarFilter().setProgress(checkProgressValue(value));
    }

    public void setProgressBarSend(double value) {
        controller.getProgressBarSend().setProgress(checkProgressValue(value));
    }

    public void setProgressBarReceive(double value) {
        controller.getProgressBarReceive().setProgress(checkProgressValue(value));
    }


    public void filterFinish(BufferedImage newBufferedImage) {
        Image image = SwingFXUtils.toFXImage(newBufferedImage, null);
        controller.getImageView().setImage(image);
        oldImage = bufferedImage;
        bufferedImage = newBufferedImage;
        controller.getProgressBarFilter().setProgress(1);
        controller.getProgressBarSend().setProgress(1);
        controller.getProgressBarReceive().setProgress(1);
        controller.setStart(false);
    }
}
