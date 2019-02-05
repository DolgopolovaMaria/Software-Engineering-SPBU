package sample;

import java.awt.image.BufferedImage;

public interface IClient {

    void setProgressBarFilter(double value);
    void setProgressBarSend(double value);
    void setProgressBarReceive(double value);

    void filterFinish(BufferedImage image);

    BufferedImage getBufferedImage();

    String getSelectedFilter();
}