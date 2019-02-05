
public class Main {

    public static void main(String[] args) {
        ProcessManagerPriority priority = new ProcessManagerPriority();

        Test(priority);
    }

    public static void Test(IProcessManager processManager) {
        Process process1 = new Process(processManager);
        Process process2 = new Process(processManager);
        Process process3 = new Process(processManager);

        processManager.setFiber(process1);
        processManager.setFiber(process2);
        processManager.setFiber(process3);

        processManager.start();
        processManager.close();
    }
}
