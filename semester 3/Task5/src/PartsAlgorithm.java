import java.util.concurrent.*;

public class PartsAlgorithm implements IVectorLengthComputer {

    private int lengthOfPart;

    private int numberOfThreads;

    private ExecutorService executor;

    public PartsAlgorithm() {
        executor = Executors.newCachedThreadPool();
        numberOfThreads = 4;
    }

    private Future<Integer> createFuture(int start, int end, int[] a) {
        Compute task = new Compute(start, end, a);
        return executor.submit(task);
    }

    public int computeLength(int[] a) {
        lengthOfPart = a.length / numberOfThreads;

        Future<Integer>[] parts = new Future[numberOfThreads];
        for (int i = 0; i < numberOfThreads - 1; i++) {
             parts[i] = createFuture(i * lengthOfPart, i * lengthOfPart + lengthOfPart - 1, a);
        }
        parts[numberOfThreads - 1] = createFuture((numberOfThreads - 1) * lengthOfPart, a.length - 1, a);

        int result = 0;
        try {
            for (int i = 0; i < numberOfThreads; i++) {
                result += parts[i].get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = (int)Math.sqrt(result);
        return result;
    }

    private class Compute implements Callable<Integer> {
        private int startIndex;
        private int endIndex;
        private int[] a;

        public Compute(int startIndex, int endIndex, int[] a) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.a = a;
        }

        public Integer call() {
            int result = 0;
            for(int i = startIndex; i <= endIndex; i++) {
                result += a[i] * a[i];
            }
            return result;
        }
    }
}
