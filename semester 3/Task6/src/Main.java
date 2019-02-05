public class Main {
    public static void main(String[] args) {
        IExamSystem striped = new StripedHashSet();
        IExamSystem copy = new CopyHashTable();

        int numberOfIterations = 1000000;
        Test testStriped = new Test(striped, numberOfIterations);
        Test testCopy = new Test(copy, numberOfIterations);
        long timeStriped = testStriped.test(striped, numberOfIterations);
        long timeCopy = testCopy.test(copy, numberOfIterations);

        System.out.println("Amount of iterations = " + numberOfIterations);
        System.out.println("StripedHashSet: " + timeStriped + " milliseconds");
        System.out.println("CopyHashTable: " + timeCopy + " milliseconds");
    }
}


