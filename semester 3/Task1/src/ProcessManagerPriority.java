import java.util.List;
import java.util.ArrayList;

public class ProcessManagerPriority implements IProcessManager {
    private List<FiberPriority> fibers = new ArrayList<>();
    private List<Fiber> deletedFibers = new ArrayList<>();
    private Fiber currentFiber;
    private boolean started = false;
    private int averagePriority;

    public void ProcessManagerNonPriority() {
    }

    public void start() {
        if(started || fibers.isEmpty()) {
            return;
        }
        int sumPriorities = 0;
        for (int i = 0; i < fibers.size(); i++) {
            sumPriorities += fibers.get(i).priority;
        }
        averagePriority = sumPriorities / fibers.size();
        started = true;
        currentFiber = fibers.get(0).fiber;
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
        int priority = process.getPriority();
        fibers.add(new FiberPriority(new Fiber(process::run), priority));
    }

    private void checkPriorities() {
        int lowValue = averagePriority - 2 * fibers.size();
        int highValue = averagePriority + 2 * fibers.size();
        for (int i = 0; i < fibers.size(); i++) {
            if(fibers.get(i).priority > highValue) {
                fibers.get(i).priority = highValue - 1;
            }
            if(fibers.get(i).priority < lowValue) {
                fibers.get(i).priority = lowValue + 1;
            }
        }
    }

    private Fiber getNext() {
        if(fibers.size() == 1) {
            return fibers.get(0).fiber;
        }
        checkPriorities();
        int priority = Integer.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < fibers.size(); i++) {
            fibers.get(i).priority++;
            if((fibers.get(i).priority >= priority) && (fibers.get(i).fiber != currentFiber)) {
                priority = fibers.get(i).priority;
                index = i;
            }
        }
        fibers.get(index).priority -= 2;
        return fibers.get(index).fiber;
    }

    public int getInd(Fiber f) {
        for (int i = 0; i < fibers.size(); i++) {
            if(fibers.get(i).fiber == f) {
                return i;
            }
        }
        return -1;
    }

    public void switchProcess(boolean finished) {
        if(finished) {
            fibers.remove(getInd(currentFiber));
            if(!currentFiber.isPrimary()) {
                deletedFibers.add(currentFiber);
            }
            if(fibers.isEmpty()) {
                stop();
            }
            else {
                currentFiber = getNext();
                Fiber.switchTo(currentFiber.getID());
            }
        }
        else {
            currentFiber = getNext();
            Fiber.switchTo(currentFiber.getID());
        }

    }
}
