import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CopyOnWriteList {

    private volatile ListElement head;

    private Lock lock;

    public CopyOnWriteList(){
        head = null;
        lock = new ReentrantLock();
    }

    public CopyOnWriteList(ListElement head){
        this.head = head;
        lock = new ReentrantLock();
    }

    public ListElement getHead() {
        return head;
    }

    public void setHead(ListElement element) {
        head = element;
    }

    private CopyOnWriteList copy() {
        if(head == null) {
            return new CopyOnWriteList();
        }
        ListElement newHead = head.copy();
        CopyOnWriteList newList = new CopyOnWriteList(newHead);
        ListElement temp = head.getNext();
        ListElement prev = newHead;
        while (temp != null) {
            ListElement newElement = temp.copy();
            prev.setNext(newElement);
            temp = temp.getNext();
            prev = newElement;
        }
        return newList;
    }

    public void add(long student, long course) {
        lock.lock();
        CopyOnWriteList newList = copy();
        if(!newList.contain(student, course)) {
            ListElement newHead = new ListElement(student, course, newList.getHead());
            newList.setHead(newHead);
            head = newHead;
        }
        lock.unlock();
    }

    private void deleteElement(long student, long course) {
        if (head.equals(student, course)) {
            head = head.getNext();
        }
        else {
            ListElement prev = head;
            ListElement temp = head.getNext();
            while (temp != null) {
                if(temp.equals(student, course)) {
                    prev.setNext(temp.getNext());
                }
                temp = temp.getNext();
                prev = prev.getNext();
            }
        }
    }

    public void delete(long student, long course) {
        lock.lock();
        CopyOnWriteList newList = copy();
        if(newList.contain(student, course)) {
            newList.deleteElement(student, course);
            head = newList.getHead();
        }
        lock.unlock();
    }

    public boolean contain(long student, long course) {
        if(head == null){
            return false;
        }
        ListElement temp = head;
        while (temp != null) {
            if(temp.equals(student, course)) {
                return true;
            }
            temp = temp.getNext();
        }
        return false;
    }
}
