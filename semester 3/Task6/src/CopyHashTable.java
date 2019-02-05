public class CopyHashTable implements IExamSystem {

    private CopyOnWriteList[] lists;

    private final static int size = 100;

    public CopyHashTable(){
        lists = new CopyOnWriteList[size];
        for(int i = 0; i < size; i++) {
            lists[i] = new CopyOnWriteList();
        }
    }

    private int getHash(long student) {
        return (int)Math.abs(student % size);
    }

    public void add(long studentId, long courseId) {
        lists[getHash(studentId)].add(studentId, courseId);
    }

    public void remove(long studentId, long courseId) {
        lists[getHash(studentId)].delete(studentId, courseId);
    }

    public boolean contains(long studentId, long courseId) {
        return lists[getHash(studentId)].contain(studentId, courseId);
    }
}