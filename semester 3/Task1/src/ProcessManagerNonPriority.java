import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProcessManagerNonPriority implements IProcessManager {
    private Random random = new Random();
    private List<Fiber> fibers = new ArrayList<>();
    private List<Fiber> deletedFibers = new ArrayList<>();
    private Fiber current;
    private boolean started = false;

    public void ProcessManagerNonPriority() {

    }

    public void start() {
        if(started || fibers.isEmpty()) {
            return;
        }
        started = true;
        current = fibers.get(0);
        switchProcess(false);
    }

    private void stop() {
        started = false;
        Fiber.switchTo(Fiber.getPrimaryID());
    }

    public void close() {
        for (int i = 0; i < deletedFibers.size(); i++) {
            if(!deletedFibers.get(i).isPrimary()) {
                Fiber.delete(deletedFibers.get(i).getID());
            }
        }
    }

    public void setFiber(Process process) {
        fibers.add(new Fiber(process::run));
    }

    private Fiber getNext() {
        if(fibers.size() == 1) {
            return fibers.get(0);
        }
        int randomID = random.nextInt(fibers.size());
        int currentFiber = fibers.indexOf(current);
        while (randomID == currentFiber) {
            randomID = random.nextInt(fibers.size());
        }
        return fibers.get(randomID);
    }

    public void switchProcess(boolean finished) {
        if(finished) {
            fibers.remove(current);
            if(!current.isPrimary()) {
                deletedFibers.add(current);
            }
            if(fibers.isEmpty()) {
                stop();
            }
            else {
                current = getNext();
                Fiber.switchTo(current.getID());
            }
        }
        else {
            current = getNext();
            Fiber.switchTo(current.getID());
        }
    }
}
