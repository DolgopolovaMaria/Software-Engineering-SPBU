package Server;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class Filters implements Runnable {

    public enum FilterType {average, gauss, sobelX, sobelY, grey};

    private static double[][] averageMatrix = {
            {0.1111, 0.1111, 0.1111},
            {0.1111, 0.1111, 0.1111},
            {0.1111, 0.1111, 0.1111}};

    private static double[][] gaussMatrix = {
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}};

    private static double[][] sobelXMatrix = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}};

    private static double[][] sobelYMatrix = {
            { -1, -2, -1 },
            { 0, 0, 0 },
            { 1, 2, 1 }};

    private static int getColor(double value) {
        if (value >= 255) {
            return 255;
        }
        if (value <= 0) {
            return 0;
        }
        return (int)value;
    }

    private double progress;
    private volatile boolean work;
    private volatile boolean finish;
    private BufferedImage resultImage;
    private BufferedImage startImage;
    private FilterType filterType;

    public void start(BufferedImage image, FilterType type){
        progress = 0;
        work = true;
        finish = false;
        startImage = image;
        filterType = type;
    }

    public double getProgress(){
        return progress;
    }

    public void stop(){
        work = false;
        progress = 0;
        finish = false;
    }

    public boolean isFinish(){
        return finish;
    }

    public BufferedImage getResultImage(){
        return resultImage;
    }

    private BufferedImage applyFilterMatrix(BufferedImage image, FilterType filter) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        double[][] matrix;

        switch (filter) {
            case average:
                matrix = averageMatrix;
                break;
            case gauss:
                matrix = gaussMatrix;
                break;
            case sobelX:
                matrix = sobelXMatrix;
                break;
            case sobelY:
                matrix = sobelYMatrix;
                break;
            default:
                return image;
        }

        for (int i = 1; i < image.getWidth() - 1; i++)
        {
            for (int j = 1; j < image.getHeight() - 1; j++)
            {
                double rgbRed = 0;
                double rgbGreen = 0;
                double rgbBlue = 0;

                for (int k = 0; k < 3; k++)
                {
                    for (int m = 0; m < 3; m++)
                    {
                        Color c = new Color(image.getRGB(i + k - 1, j + m - 1));
                        rgbRed += c.getRed() * matrix[k][m];
                        rgbGreen += c.getGreen() * matrix[k][m];
                        rgbBlue += c.getBlue() * matrix[k][m];
                    }
                }

                int rgbRedINT = getColor(rgbRed);
                int rgbBlueINT = getColor(rgbBlue);
                int rgbGreenINT = getColor(rgbGreen);

                newImage.setRGB(i, j, new Color(rgbRedINT, rgbGreenINT, rgbBlueINT).getRGB());
            }
            progress = (double)i / image.getWidth();
            if(!work) {
                return null;
            }
        }
        return newImage;
    }

    private BufferedImage applyFilterGrey(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getWidth(); i++)
        {
            for (int j = 0; j < image.getHeight(); j++)
            {
                Color c = new Color(image.getRGB(i, j));
                double newColor = 0.072 * c.getBlue() + 0.213 * c.getRed() + 0.0715 * c.getGreen();
                int newBlue = getColor(newColor);
                int newRed = getColor(newColor);
                int newGreen = getColor(newColor);
                newImage.setRGB(i, j, new Color(newRed, newGreen, newBlue).getRGB());
            }
            progress = (double)i / image.getWidth();
            if(!work) {
                return null;
            }
        }

        return newImage;
    }

    private BufferedImage apply(BufferedImage image, FilterType filterType) {
        if(!work) {
            return null;
        }
        if(filterType == FilterType.grey) {
            return applyFilterGrey(image);
        }
        return applyFilterMatrix(image, filterType);
    }

    public void run(){
        resultImage = apply(startImage, filterType);
        finish = true;
    }

    public static List<FilterType> getAvailableFilters() {
        List<FilterType> filters = new LinkedList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("filters"));
            String strLine;
            while ((strLine = br.readLine()) != null){
                FilterType filterType = FilterType.valueOf(strLine);
                if(filterType != null) {
                    filters.add(filterType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filters;
    }
}