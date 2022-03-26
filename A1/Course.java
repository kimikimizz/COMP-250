public class Course {
    public String code;
    public int capacity;
    public SLinkedList<Student>[] studentTable;
    public int size;
    public SLinkedList<Student> waitlist;


    public Course(String code) {
        this.code = code;
        this.studentTable = new SLinkedList[10];
        this.size = 0;
        this.waitlist = new SLinkedList<Student>();
        this.capacity = 10;
    }

    public Course(String code, int capacity) {
        this.code = code;
        this.studentTable = new SLinkedList[capacity];
        this.size = 0;
        this.waitlist = new SLinkedList<>();
        this.capacity = capacity;
    }

    public void changeArrayLength(int m) {
        // Create a new table of length m
        SLinkedList<Student>[] newTable = new SLinkedList[m];
        
        // Sanity Checks
        // If the current table is null, set table to new table of length m.
        if (studentTable == null) {
            studentTable = newTable;
            return;
        }
        // If the current length is equivalent to m, nothing changes. 
        if (m == studentTable.length) return;

        int[] registeredIDs = getRegisteredIDs();
        Student[] registedStudents = getRegisteredStudents();
        
        capacity = m;
        for (int i = 0; i < capacity; i++) {
            if (i < registeredIDs.length) {
                int index = registeredIDs[i] % capacity;
                if (newTable[index] == null) {
                    SLinkedList<Student> newList = new SLinkedList();
                    newTable[index] = newList;
                }
                newTable[index].addLast(registedStudents[i]);
            }
        }
        studentTable = newTable;
    }

    public boolean put(Student s) {
        // If the student has reached their maximum course limit
        if (s.courseCodes.size() >= s.COURSE_CAP) {
            return false;
        }
        // If the student is registered or in the waitlist
        if (s.isRegisteredOrWaitlisted(code) == true) {
            return false;
        }
        // Calculate the index where to add the student
        int index = s.id % capacity;
        // If there are enough capacity to add the student
        if (capacity > size ) {
            if (studentTable[index] == null) {
                SLinkedList<Student> newList = new SLinkedList();
                studentTable[index] = newList;
            }
            studentTable[index].addLast(s); 
            s.addCourse(code);
            size++;
            return true;
        } 
        // Else if there is enough space in the waitlist
        else if (waitlist.size() < (capacity / 2) ) {
            waitlist.addLast(s);
            s.addCourse(code);
            return true;
        }
        // Otherwise, increase the registration capacity by 50%
        else {
            // Calculate the amount to increase
            int increase = capacity/2;
            // Resize the table to accommodate 50% more students
            changeArrayLength(increase + capacity);
            // Add the students in waitlist to the table
            for (int i = 0; i < waitlist.size(); i++) {
                index = waitlist.getFirst().id % capacity;
                if (studentTable[index] == null) {
                    SLinkedList<Student> newList = new SLinkedList();
                    studentTable[index] = newList;
                }
                studentTable[index].addLast(waitlist.getFirst());
                waitlist.removeFirst();
                i--;
                size++;
            }
            // Add the new student to the end of waitlist
            waitlist.addLast(s);
            s.addCourse(code);
        }
        return true;
    }

    public Student get(int id) {
        // Calculate the index for the student id
        int index = id % capacity;
        // If the linked list is null, go to the waitlist instead
        if (studentTable[index] != null) {
            // Loop through the linked list at the index to find the student
            for (int i = 0; i < studentTable[index].size(); i++) {
                if (studentTable[index].get(i).id == id) {
                    return studentTable[index].get(i);
                }
            }
        }
        // Loop through the waitlist
        for (int i = 0; i < waitlist.size(); i++) {
            // Return the student if we found them
            if (waitlist.get(i).id == id) {
                return waitlist.get(i);
            } 
            // Else if we are at the end of the waitlist
            else if (i == (waitlist.size() - 1)) {
                return null;
            }
        }
        return null;
    }


    public Student remove(int id) {
        // If the student is not found in the table, nor in the waitlist
        if (get(id) == null) {
            // Return null
            return null;
        }
        // If the student is registered, get their Student object
        Student studentToRemove = get(id);
        
        // If the student is in the waitlist, remove them. 
        if (waitlist != null) {
            waitlist.remove(studentToRemove);
            studentToRemove.dropCourse(code);
            if (get(id) == null) {
                return studentToRemove;
            }
        }
        // Otherwise, remove them from the table. 
        int index = id % studentTable.length;
        studentTable[index].remove(studentToRemove);
        studentToRemove.dropCourse(code);
        size--;
        // Add a student from the waitlist if it is not empty
        if (waitlist.getFirst() != null) {
            index = waitlist.getFirst().id % studentTable.length;
            if (studentTable[index] == null) {
                SLinkedList<Student> newList = new SLinkedList();
                studentTable[index] = newList;
            }
            studentTable[index].addLast(waitlist.getFirst());
            size++;
        }
        return studentToRemove;
    }

    public int getCourseSize() {
        return size;
    }


    public int[] getRegisteredIDs() {
        // If the student table is null, return null
        if (studentTable == null) {
            return null;
        }
        // Create a new linked list for all the Students
        SLinkedList<Student> newList = new SLinkedList();
        // Create a new array of length size
        int[] arrayRegisteredIDs = new int[size];

        // Looping through the table to get all the students
        for (int i = 0; i < studentTable.length; i++) {
            if (studentTable[i] == null) {
                continue;
            }
            for (int j = 0; j < studentTable[i].size(); j++) {
                newList.addLast(studentTable[i].get(j));
            }
        }

        for (int i = 0; i < newList.size(); i++) {
            arrayRegisteredIDs[i] = newList.get(i).id;
        }
        return arrayRegisteredIDs;
    }

    public Student[] getRegisteredStudents() {
        // If the student table is null, return null
        if (studentTable == null) {
            return null;
        }
        // Using the precious method to get an array of IDs
        int[] arrayRegisteredIDs = getRegisteredIDs();
        // Create an new array of length size
        Student[] arrayRegistered = new Student[size];

        for (int i = 0; i < arrayRegisteredIDs.length; i++) {
            arrayRegistered[i] = get(arrayRegisteredIDs[i]);
        }
        
        return arrayRegistered;
    }

    public int[] getWaitlistedIDs() {
        // If the waitlist is empty, return null
        if (waitlist.isEmpty() == true) {
            return null;
        }
        int[] arrayWaitlistID = new int[waitlist.size()];

        for (int i = 0; i < waitlist.size(); i++) {
            arrayWaitlistID[i] = waitlist.get(i).id;
        }

        return arrayWaitlistID;
    }

    public Student[] getWaitlistedStudents() {
        // If the waitlist is empty, return null
        if (waitlist.isEmpty() == true) {
            return null;
        }
        Student[] arrayWaitlist = new Student[waitlist.size()];

        for (int i = 0; i < waitlist.size(); i++) {
            arrayWaitlist[i] = waitlist.get(i);
        }

        return arrayWaitlist;
    }
    

    public String toString() {
        String s = "Course: "+ this.code +"\n";
        s += "--------\n";
        for (int i = 0; i < this.studentTable.length; i++) {
            s += "|"+i+"     |\n";
            s += "|  ------> ";
            SLinkedList<Student> list = this.studentTable[i];
            if (list != null) {
                for (int j = 0; j < list.size(); j++) {
                    Student student = list.get(j);
                    s +=  student.id + ": "+ student.name +" --> ";
                }
            }
            s += "\n--------\n\n";
        }

        return s;
    }
    // public static void main(String[] args){
    //     Course IntroToComputerScience = new Course("COMP250");
    //     IntroToComputerScience.capacity=4;
    //     IntroToComputerScience.code = "ecse202";
    //     Student s1= new Student(1, "s1");
    //     Student s2= new Student(2, "s2");
    //     Student s3= new Student(3, "s3");
    //     Student s4= new Student(4, "s4");
    //     Student s5= new Student(5, "s5");
    //     Student s6= new Student(6, "s6");
    //     Student s7= new Student(7, "s7");


    //     System.out.println("The size is " + IntroToComputerScience.size + " expected 0");
    //     System.out.println("Now we will fill up the course");
    //     IntroToComputerScience.put(s1);
    //     IntroToComputerScience.put(s2);
    //     IntroToComputerScience.put(s3);
    //     IntroToComputerScience.put(s4);
    //     System.out.println("The size is " + IntroToComputerScience.size + " expected 4");
    //     System.out.println("The capacity is " + IntroToComputerScience.capacity + " expected 4");
    //     System.out.println("Now I will try to add two more students, who should go to the waitlist");
    //     IntroToComputerScience.put(s5);
    //     IntroToComputerScience.put(s6);
    //     System.out.println("The size is still: " + IntroToComputerScience.size + " expected 4");
    //     System.out.println("The waitlist is size: " + IntroToComputerScience.waitlist.size() + " expected 2");
    //     System.out.println("The capacity should not have increased yet, it is: " + IntroToComputerScience.capacity + "expected to be 4");
    //     System.out.println("Now we will add someone else to the course, and hopefully trigger the expansion of the class");
    //     IntroToComputerScience.put(s7);
    //     System.out.println("Capacity should have increasted to 6, it is: " + IntroToComputerScience.capacity);
    //     System.out.println("Size should have increasted to 6, it is: " + IntroToComputerScience.size);
    //     System.out.println("because of the student who triggered the class to size up, waitlist should be 1 and it is: " + IntroToComputerScience.waitlist.size());

    // }
}
