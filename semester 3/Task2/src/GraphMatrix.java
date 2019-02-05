import java.io.BufferedReader;
import java.io.FileReader;

public class GraphMatrix {

    private int size;

    private int[][] matrix;

    private int startIndex;

    private int endIndex;

    public GraphMatrix() {
    }

    public GraphMatrix(int size) {
        this.size = size;
        this.matrix = new int[size][size];
        startIndex = 0;
        endIndex = size - 1;
    }

    //It is only part of some graph
    public GraphMatrix(int size, int start, int end) {
        this.size = size;
        this.matrix = new int[end - start + 1][size];
        startIndex = start;
        endIndex = end;
    }

    public int getSize() {
        return size;
    }

    public int getElement(int i, int j) {
        return matrix[i - startIndex][j];
    }

    public void setElement(int i, int j, int value) {
        matrix[i - startIndex][j] = value;
    }

    public String elementToString(int i, int j)
    {
        if (getElement(i, j) == Integer.MAX_VALUE) {
            return "inf ";
        }
        else {
            return getElement(i, j) + " ";
        }
    }

    //write values ​​from array to graph
    public void fromArray(int[] arr, int start, int end) {
        for (int i = start; i <= end; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = arr[(i - start) * size + j];
            }
        }
    }

    //write values ​​from graph to array
    public void toArray(int[] arr, int start, int end) {
        for (int i = start; i <= end; i++) {
            for (int j = 0; j < size; j++) {
                arr[(i - start) * size + j] = matrix[i][j];
            }
        }
    }

    //read data from file and write it to graph
    public void createMatrix(String fileName) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String sizeStr = in.readLine();
            int size = Integer.parseInt(sizeStr);
            this.size = size;
            this.matrix = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = Integer.MAX_VALUE;
                }
            }
            for (int i = 0; i < size; i++) {
                matrix[i][i] = 0;
            }
            String line;
            while ((line = in.readLine()) != null) {
                String[] input = line.split(" ");
                int i = Integer.parseInt(input[0]);
                int j = Integer.parseInt(input[1]);
                int weight = Integer.parseInt(input[2]);
                matrix[i][j] = weight;
                matrix[j][i] = weight;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}