import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {
    IExamSystem examSystem;

    int numberOfIterations;

    public Test(IExamSystem examSystem, int numberOfIterations){
        this.examSystem = examSystem;
        this.numberOfIterations = numberOfIterations;
    }

    public long test(IExamSystem examSystem, int numberOfIterations) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Random random = new Random();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < (0.9 * numberOfIterations); i++) {
            executor.execute(new ContainsTask(examSystem, random));
        }
        for (int i = 0; i < (0.09 * numberOfIterations); i++) {
            executor.execute(new AddTask(examSystem, random));
        }
        for (int i = 0; i < (0.01 * numberOfIterations); i++) {
            executor.execute(new RemoveTask(examSystem, random));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private class AddTask implements Runnable {
        IExamSystem system;

        Random random;

        public AddTask(IExamSystem system, Random random) {
            this.system = system;
            this.random = random;
        }

        public void run() {
            long student = random.nextLong();
            long course = random.nextLong();
            system.add(student, course);
        }
    }

    private class RemoveTask implements Runnable {
        IExamSystem system;

        Random random;

        public RemoveTask(IExamSystem system, Random random) {
            this.system = system;
            this.random = random;
        }

        public void run() {
            long student = random.nextLong();
            long course = random.nextLong();
            system.remove(student, course);
        }
    }

    private class ContainsTask implements Runnable {
        IExamSystem system;

        Random random;

        public ContainsTask(IExamSystem system, Random random) {
            this.system = system;
            this.random = random;
        }

        public void run() {
            long student = random.nextLong();
            long course = random.nextLong();
            system.contains(student, course);
        }
    }
}