import java.util.concurrent.*;

public class BinaryAlgorithm implements IVectorLengthComputer {

    private int lengthOfPart;

    private int numberOfThreads;

    private ExecutorService executor;

    public BinaryAlgorithm() {
        executor = Executors.newCachedThreadPool();
        numberOfThreads = 4;
    }

    private Future<Integer> createFuture(int start, int end, int[] a) {
        Compute task = new Compute(start, end, a);
        return executor.submit(task);
    }

    public int computeLength(int[] a) {
        lengthOfPart = a.length / numberOfThreads;

        int result = 0;
        try {
            result = createFuture(0, a.length - 1, a).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        result = (int)Math.sqrt(result);
        return result;
    }

    private class Compute implements Callable<Integer> {
        private int startIndex;
        private int endIndex;
        private int[] a;

        public Compute(int start, int end, int[] a) {
            this.startIndex = start;
            this.endIndex = end;
            this.a = a;
        }

        public Integer call() {
            int result = 0;
            if (endIndex - startIndex + 1 <= lengthOfPart) {
                for (int i = startIndex; i <= endIndex; i++) {
                    result += a[i] * a[i];
                }
                return result;
            }
            else {
                int startLeft = startIndex;
                int endLeft = startIndex + (endIndex - startIndex) / 2;
                int startRight = startIndex + (endIndex - startIndex) / 2 + 1;
                int endRight = endIndex;

                Future<Integer> left = createFuture(startLeft, endLeft, a);
                Future<Integer> right = createFuture(startRight, endRight, a);
                try{
                    result = left.get() + right.get();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
    }
}
