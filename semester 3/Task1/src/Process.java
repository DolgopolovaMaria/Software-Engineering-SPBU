import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Process {

    private static Random Rng = new Random();

    private final int LongPauseBoundary = 10000;

    private final int ShortPauseBoundary = 100;

    private final int WorkBoundary = 1000;

    private final int IntervalsAmountBoundary = 10;

    private final int PriorityLevelsNumber = 10;

    private IProcessManager processManager;

    private int priority;

    private List<Integer> _workIntervals = new ArrayList<Integer>();
    private List<Integer> _pauseIntervals = new ArrayList<Integer>();

    public Process(IProcessManager processManager)
    {
        int amount = Rng.nextInt(IntervalsAmountBoundary);

        for (int i = 0; i < amount; i++)
        {
            _workIntervals.add(Rng.nextInt(WorkBoundary));
            _pauseIntervals.add(Rng.nextInt(
                    Rng.nextDouble() > 0.9
                            ? LongPauseBoundary
                            : ShortPauseBoundary));
        }
        this.processManager = processManager;
        priority = Rng.nextInt(PriorityLevelsNumber);
    }

    public void run() throws InterruptedException
    {
        for (int i = 0; i < _workIntervals.size(); i++)
        {
            Thread.sleep(_workIntervals.get(i)); // work emulation
            long pauseBeginTime = System.currentTimeMillis();
            do {
                processManager.switchProcess(false);
            } while (System.currentTimeMillis() - pauseBeginTime < _pauseIntervals.get(i)); // I/O emulation
        }
        processManager.switchProcess(true);
    }

    public int getPriority() {
        return priority;
    }


    public int getTotalDuration() {
        return getActiveDuration() + _pauseIntervals.stream().mapToInt(Integer::intValue).sum();
    }

    public int getActiveDuration() {
        return _workIntervals.stream().mapToInt(Integer::intValue).sum();
    }
}
