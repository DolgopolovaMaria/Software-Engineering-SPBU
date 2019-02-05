import java.util.Random;

public class Main {
    public static void main(String[] args) {
        IVectorLengthComputer nonparallel = new NonParallelAlgorithm();
        IVectorLengthComputer parts = new PartsAlgorithm();
        IVectorLengthComputer binary = new BinaryAlgorithm();

        System.out.println("Test on a short vector");
        int[] vector = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int answer = 16;
        if((nonparallel.computeLength(vector) == answer) &&
                (parts.computeLength(vector) == answer) &&
                (answer == binary.computeLength(vector))) {
            System.out.println("Correct answer");
        }
        else {
            System.out.println("Wrong answer");
        }
        System.out.println();

        System.out.println("Test on a long vector");
        int length = 100000;
        int numberOfIterations = 10000;
        Random random = new Random();
        vector = new int[length];
        for (int i = 0; i < length; i++) {
            vector[i] = random.nextInt(10);
        }
        long timeNonparallel = testIVectorLength(nonparallel, vector, numberOfIterations);
        long timeParts = testIVectorLength(parts, vector, numberOfIterations);
        long timeBinary = testIVectorLength(binary, vector, numberOfIterations);
        System.out.println("Average times: ");
        System.out.println("NonParallelAlgorithm: " + timeNonparallel / (double) numberOfIterations);
        System.out.println("PartsAlgorithm: " + timeParts / (double) numberOfIterations);
        System.out.println("BinaryAlgorithm: " + timeBinary / (double) numberOfIterations);
    }

    private static long testIVectorLength(IVectorLengthComputer computer, int[] vector, int numOfIt)
    {
        long time = 0;
        long startTime;
        long stopTime;
        for (int i = 0; i < numOfIt; i++) {
            startTime = System.currentTimeMillis();
            computer.computeLength(vector);
            stopTime = System.currentTimeMillis();
            time += stopTime - startTime;
        }
        return time;
    }
}
