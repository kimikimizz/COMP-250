public class Student {
    public int id;
    public String name;
    public SLinkedList<String> courseCodes;

    public final int COURSE_CAP = 3;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.courseCodes = new SLinkedList<String>();
    }

    public boolean isRegisteredOrWaitlisted(String course) {
        return this.courseCodes.getIndexOf(course) > -1;
    }

    public void addCourse(String course) {
        this.courseCodes.addLast(course);
    }

    public void dropCourse(String course) {
        this.courseCodes.remove(course);
    }

    public String toString(){
        return "#" + this.id + " : " + this.name;
    }

}
