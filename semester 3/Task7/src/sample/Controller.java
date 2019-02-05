package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.io.File;

public class Controller {

    private Client client;

    private boolean start;

    private boolean loadedImage;

    @FXML
    private ComboBox<String> comboboxFilters;

    @FXML
    private ProgressBar progressBarFilter;

    @FXML
    private ProgressBar progressBarSend;

    @FXML
    private ProgressBar progressBarReceive;

    @FXML
    private ImageView imageView;

    private FileChooser fileChooser = new FileChooser();

    public Controller() {
    }

    public void setStart(boolean start){
        this.start = start;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ComboBox<String> getComboboxFilters() {
        return comboboxFilters;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ProgressBar getProgressBarFilter() {
        return progressBarFilter;
    }

    public ProgressBar getProgressBarSend() {
        return progressBarSend;
    }

    public ProgressBar getProgressBarReceive() {
        return progressBarReceive;
    }

    public void buttonLoadClick(ActionEvent event) {
        if(!start) {
            try {
                File file = fileChooser.showOpenDialog(Main.primaryStage);
                Image image = SwingFXUtils.toFXImage(ImageIO.read(file), null);
                imageView.setImage(image);
                client.buttonLoad(file);
                loadedImage = true;
            }
            catch (Exception e) {
            }
        }
    }

    public void buttonStartClick(ActionEvent event) {
        if((!start) && loadedImage && (comboboxFilters.getSelectionModel().getSelectedItem() != null)) {
            client.buttonStart();
            start = true;
        }
    }

    public void buttonBreakClick(ActionEvent event) {
        if(start) {
            client.buttonBreak();
            start = false;
            progressBarFilter.setProgress(0);
            progressBarSend.setProgress(0);
            progressBarReceive.setProgress(0);
        }
    }

    public void buttonExitClick(ActionEvent event) {
        client.buttonExit();
        System.exit(0);
    }

    public void buttonUndoClick(ActionEvent event) {
        if(start){
            return;
        }
        client.buttonUndo();
    }

    public void buttonSaveClick(ActionEvent event) {
        if(start || !loadedImage){
            return;
        }
        File file = fileChooser.showSaveDialog(Main.primaryStage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imageView.getImage(),
                        null), "png", file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
