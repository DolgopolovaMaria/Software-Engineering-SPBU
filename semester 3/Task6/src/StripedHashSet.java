import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class StripedHashSet implements IExamSystem {

    private ArrayList<ListElement>[] lists;

    private final static int startSize = 100;

    private ReentrantLock[] locks;

    private int amountOfElements;

    public StripedHashSet() {
        lists = new ArrayList[startSize];
        for(int i = 0; i < startSize; i++) {
            lists[i] = new ArrayList<ListElement>();
        }
        amountOfElements = 0;
        locks = new ReentrantLock[startSize];
        for(int i = 0; i < startSize; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    private int getHash(long student) {
        return (int)Math.abs(student % lists.length);
    }

    private int getHash(ListElement element) {
        return getHash(element.getStudent());
    }

    private void acquire(long student) {
        locks[getHash(student) % locks.length].lock();
    }

    private void release(long student) {
        locks[getHash(student) % locks.length].unlock();
    }

    public boolean policy() {
        return amountOfElements / lists.length > 3;
        }

    public void resize() {
        int oldCapacity = lists.length;
        try {
            for (int i = 0; i < locks.length; i++) {
                locks[i].lock();
            }
            if (oldCapacity != lists.length) {
                return;
                }
            int newCapacity = 2 * oldCapacity;
            ArrayList<ListElement>[] oldTable = lists;
            lists = new ArrayList[newCapacity];
            for (int i = 0; i < newCapacity; i++)
                lists[i] = new ArrayList<ListElement>();
            for (int i = 0; i < oldTable.length; i++) {
                for (ListElement temp : oldTable[i]) {
                    lists[getHash(temp)].add(temp);
                    }
                }
        }
        finally {
            for (int i = 0; i < locks.length; i++) {
                locks[i].unlock();
            }
        }
    }

    public void add(long studentId, long courseId) {
        if(contains(studentId, courseId)) {
            return;
        }

        acquire(studentId);
        try {
            lists[getHash(studentId)].add(new ListElement(studentId, courseId));
            amountOfElements++;
        }
        finally {
            release(studentId);
        }
        if (policy())
            resize();
    }

    public void remove(long studentId, long courseId) {
        if(!contains(studentId, courseId)) {
            return;
        }

        acquire(studentId);
        try {
            lists[getHash(studentId)].remove(new ListElement(studentId, courseId));
            amountOfElements--;
        }
        finally {
            release(studentId);
        }
    }

    public boolean contains(long studentId, long courseId) {
        acquire(studentId);
        try {
            return lists[getHash(studentId)].contains(new ListElement(studentId, courseId));
        }
        finally {
            release(studentId);
        }
    }
}