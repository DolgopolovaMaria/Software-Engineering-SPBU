public class Fiber {

    private Action action;
    private int ID;
    private static int primaryID = 0;
    private boolean isPrimary;

    public int getID() {
        return ID;
    }

    public static int getPrimaryID() {
        return primaryID;
    }

    public boolean isPrimary()
    {
        return isPrimary;
    }

    public Fiber(Action action) {
        innerCreate(action);
    }

    public void delete() {
        UnmanagedFiberAPI.deleteFiber(ID);
    }

    public static void delete(int fiberId) {
        UnmanagedFiberAPI.deleteFiber(fiberId);
    }

    public static void switchTo(int fiberId) {
        System.out.println("Fiber [" + fiberId + "] Switch");
        UnmanagedFiberAPI.switchToFiber(fiberId);
    }

    private void innerCreate(Action action)
    {
        this.action = action;

        if (primaryID == 0)
        {
            primaryID = UnmanagedFiberAPI.convertThreadToFiber(0);
            isPrimary = true;
        }

        ID = UnmanagedFiberAPI.createFiber(100500, this::fiberRunnerProc, 0);
    }

    private int fiberRunnerProc(int lpParam) throws InterruptedException {
        int status = 0;
        try {
            action.run();
        } catch (Exception e) {
            status = 1;
            throw e;
        } finally {
            if (status == 1) {
                UnmanagedFiberAPI.deleteFiber(ID);
            }
        }
        return status;
    }
}
