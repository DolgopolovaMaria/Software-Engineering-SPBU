import java.io.Closeable;

public interface IProcessManager extends Closeable {
    void switchProcess(boolean fiberFinished);

    void setFiber(Process process);

    void start();

    void close();
}
