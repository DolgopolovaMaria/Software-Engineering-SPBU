public class NonParallelAlgorithm implements IVectorLengthComputer {
    public int computeLength(int[] a) {
        int result = 0;
        int length = a.length;
        for(int i = 0; i < length; i++) {
            result += a[i] * a[i];
        }
        result = (int)Math.sqrt(result);
        return result;
    }
}
