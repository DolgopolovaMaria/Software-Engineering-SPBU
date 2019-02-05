public class ListElement {

    private long studentId;

    private long courseId;

    private ListElement next;

    public ListElement(long student, long course, ListElement next) {
        studentId = student;
        courseId = course;
        this.next = next;
    }

    public ListElement(long student, long course) {
        studentId = student;
        courseId = course;
        next = null;
    }

    public long getStudent() {
        return studentId;
    }

    public long getCourse() {
        return courseId;
    }

    public ListElement getNext() {
        return next;
    }

    public void setNext(ListElement next) {
        this.next = next;
    }

    public boolean equals(long student, long course) {
        return ((studentId == student) && (courseId == course));
    }

    @Override
    public boolean equals(Object obj) {
        ListElement element = (ListElement) obj;
        return ((studentId == element.getStudent()) && (courseId == element.getCourse()));
    }

    public ListElement copy() {
        return new ListElement(studentId, courseId);
    }
}
