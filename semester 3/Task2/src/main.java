import mpi.MPI;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class main {
    public static void main(String[] args) {
        MPI.Init(args);
        final int myRank = MPI.COMM_WORLD.Rank();
        final int numOfThreads = MPI.COMM_WORLD.Size();

        int[] numberOfVertex = new int[1];
        GraphMatrix graph = new GraphMatrix();

        String input = "";
        String output = "";

        if (myRank == 0) {
            if(args.length < 5) {
                System.out.println("Error: Not enough arguments");
                System.exit(0);
            }
            input = args[3];
            output = args[4];
            graph.createMatrix(input);;
            numberOfVertex[0] = graph.getSize();
        }

        MPI.COMM_WORLD.Bcast(numberOfVertex, 0, 1, MPI.INT, 0);

        graph = dataDistribution(graph, numOfThreads);
        MPI.COMM_WORLD.Barrier();

        try {
            Floyd(graph, numOfThreads);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        MPI.COMM_WORLD.Barrier();

        int lowerRow = getLowerRow(numOfThreads, myRank, numberOfVertex[0]);
        int upperRow = getUpperRow(numOfThreads, myRank, numberOfVertex[0]);

        if(myRank == 0) {
            try {
                FileWriter writer = new FileWriter(output, true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                for (int i = lowerRow; i <= upperRow; i++) {
                    StringBuilder data = new StringBuilder();
                    for (int j = 0; j < numberOfVertex[0]; j++) {
                        data.append(graph.elementToString(i, j));
                    }
                    data.append(System.lineSeparator());
                    bufferWriter.write(data.toString());
                }
                bufferWriter.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        MPI.COMM_WORLD.Barrier();

        if (myRank != 0) {
            int[] numberOfStr = new int[1];
            numberOfStr[0] = upperRow - lowerRow + 1;
            MPI.COMM_WORLD.Send(numberOfStr, 0, 1, MPI.INT, 0, 0);

            for (int i = lowerRow; i <= upperRow; i++) {
                StringBuilder data = new StringBuilder();
                for (int j = 0; j < numberOfVertex[0]; j++) {
                    data.append(graph.elementToString(i, j));
                }
                data.append(System.lineSeparator());

                int[] len = new int[1];
                String dataStr = data.toString();
                len[0] = dataStr.length();
                MPI.COMM_WORLD.Send(len, 0, 1, MPI.INT, 0, 0);
                MPI.COMM_WORLD.Send(dataStr.toCharArray(), 0, dataStr.length(), MPI.CHAR, 0, 0);
            }

        }
        else{
            try {
                FileWriter writer = new FileWriter(output, true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);

                for (int i = 1; i < numOfThreads; i++) {
                    int[] numberOfStr = new int[1];
                    MPI.COMM_WORLD.Recv(numberOfStr, 0, 1, MPI.INT, i, 0);

                    for (int j = 0; j < numberOfStr[0]; j++) {
                        int[] len = new int[1];
                        MPI.COMM_WORLD.Recv(len, 0, 1, MPI.INT, i, 0);
                        char[] data = new char[len[0]];
                        MPI.COMM_WORLD.Recv(data, 0, len[0], MPI.CHAR, i, 0);
                        bufferWriter.write(data);
                    }
                }
                bufferWriter.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        MPI.Finalize();
    }

    private static int add(int x, int y) {
        if ((x + y < x) || (x + y < y)) {
            return Integer.MAX_VALUE;
        }
        return x + y;
    }

    private static int getLowerRow(int numberOfThreads, int myRank, int numberOfRows) {
        return myRank * numberOfRows / numberOfThreads;
    }

    private static int getUpperRow(int numberOfThreads, int myRank, int numberOfRows) {
        return (myRank + 1) * numberOfRows / numberOfThreads - 1;
    }

    //distribute rows to threads
    private static GraphMatrix dataDistribution(GraphMatrix graph, int numberOfThreads) {
        int[] numberOfVertex = {0};
        int[] arr;

        if (MPI.COMM_WORLD.Rank() == 0) {
            numberOfVertex[0] = graph.getSize();
        }
        MPI.COMM_WORLD.Bcast(numberOfVertex, 0, 1, MPI.INT, 0);

        int myRank = MPI.COMM_WORLD.Rank();
        if (myRank == 0) {

            for (int i = 1; i < numberOfThreads; i++) {
                int lowerRow = getLowerRow(numberOfThreads, i, numberOfVertex[0]);
                int upperRow = getUpperRow(numberOfThreads, i, numberOfVertex[0]);

                int[] ind = new int[2];
                ind[0] = lowerRow;
                ind[1] = upperRow;
                MPI.COMM_WORLD.Send(ind, 0, 2, MPI.INT, i, 0);
                int lengthOfArr = (upperRow - lowerRow + 1) * numberOfVertex[0];
                arr = new int[lengthOfArr];
                graph.toArray(arr, lowerRow, upperRow);
                MPI.COMM_WORLD.Send(arr, 0, lengthOfArr, MPI.INT, i, 0);
            }
        }

        if (MPI.COMM_WORLD.Rank() != 0) {
            int[] ind = new int[2];
            MPI.COMM_WORLD.Recv(ind, 0, 2, MPI.INT, 0, 0);
            int lowerRow = ind[0];
            int upperRow = ind[1];
            int lengthOfArr = (upperRow - lowerRow + 1) * numberOfVertex[0];
            arr = new int[lengthOfArr];
            MPI.COMM_WORLD.Recv(arr, 0, lengthOfArr, MPI.INT, 0, 0);
            graph = new GraphMatrix(numberOfVertex[0], lowerRow, upperRow);
            graph.fromArray(arr, 0, upperRow - lowerRow);
        }

        return graph;
    }

    public static void Floyd(GraphMatrix graph, int numberOfThreads) {
        int myRank = MPI.COMM_WORLD.Rank();
        int numberOfVertex = graph.getSize();

        int[] temp = new int[numberOfVertex];

        for (int k = 0; k < numberOfVertex; k++) {
            int rankOfThisRow = (numberOfThreads * (k + 1) - 1) / numberOfVertex;;

            if (myRank == rankOfThisRow) {
                for (int j = 0; j < numberOfVertex; j++) {
                    temp[j] = graph.getElement(k,j);
                }
            }

            MPI.COMM_WORLD.Bcast(temp, 0, numberOfVertex, MPI.INT, rankOfThisRow);

            int lowerRow = getLowerRow(numberOfThreads, myRank, numberOfVertex);
            int upperRow = getUpperRow(numberOfThreads, myRank, numberOfVertex);

            for (int i = lowerRow; i <= upperRow; i++) {
                for (int j = 0; j < numberOfVertex; j++) {
                    int value = Math.min(graph.getElement(i,j),
                            add(graph.getElement(i,k), temp[j]));
                    graph.setElement(i, j, value);
                }
            }

            MPI.COMM_WORLD.Barrier();
        }
    }
}

