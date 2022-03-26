import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ExposedTests {
    Course c1; Course c2; Course c3; Course c4; Course c5; Course c6; Course c7; Course c8; Course c9;
    Student s1; Student s2; Student s3; Student s4; Student s5; Student s6; Student s7; Student s8; Student s12;

    @BeforeEach
    void setUp() {

        //These are all great classes I would recommend to anyone :)

        this.c1 = new Course("RUSS 224",3);
        this.c2 = new Course("COMP 230");
        this.c3 = new Course("COMP 302");
        this.c4 = new Course("MATH 235", 4);
        this.c5 = new Course("PHIL 242", 5);
        this.c6 = new Course("COMP 250", 4);
        this.c7 = new Course("Math 338",4);
        this.c8 = new Course("PHYS 224",3);
        this.c9 = new Course("SOCI 377",2);
        this.s1 = new Student(1,"Phoebe");
        this.s2 = new Student(2,"Toledo");
        this.s3 = new Student(3,"Silberman");
        this.s4 = new Student(4,"Ponyo");
        this.s5 = new Student(5,"Adrianne");
        this.s6 = new Student(6,"Dorian");
        this.s7 = new Student(7,"Keeley");
        this.s8 = new Student(8,"Fonda");
        this.s12 = new Student(12,"Fleabag");
    }

    //Basic add check
    @Test
    @Tag("score:2")
    @DisplayName("Put Test 1")
    void testPut1() {
        assertTrue(this.c1.put(s1));
        assertEquals(1, this.c1.size);
        assertNotNull(this.c1.studentTable[1]);
        assertEquals(s1, this.c1.studentTable[1].getFirst());
    }

    //Adding student twice
    @Test
    @Tag("score:2")
    @DisplayName("Put Test 2")
    void testPut2() {
        assertTrue(this.c1.put(s1));
        assertFalse(this.c1.put(s1));
        assertEquals(1, this.c1.size);
    }

    //Adding student to waitlist
    @Test
    @Tag("score:3")
    @DisplayName("Put Test 3")
    void testPut3() {
        assertTrue(this.c1.put(s1));
        assertTrue(this.c1.put(s2));
        assertTrue(this.c1.put(s3));
        assertTrue(this.c1.put(s4));
        assertEquals(3, this.c1.size);
        assertEquals(1, this.c1.waitlist.size());
        assertEquals(s4, this.c1.waitlist.getFirst());
    }

    //Adding student to same bucket
    @Test
    @Tag("score:3")
    @DisplayName("Put Test 4")
    void testPut4() {
        assertTrue(this.c1.put(s1));
        assertTrue(this.c1.put(s4));
        assertEquals(2, this.c1.size);
        assertNotNull(this.c1.studentTable[1]);
        assertEquals(2, this.c1.studentTable[1].size());
        assertEquals(0, this.c1.waitlist.size());
        assertNotEquals(-1, this.c1.studentTable[1].getIndexOf(s1));
        assertNotEquals(-1, this.c1.studentTable[1].getIndexOf(s4));
    }

    //Basic remove check
    @Test
    @Tag("score:3")
    @DisplayName("Remove Test 1")
    void testRemove1() {
        this.c7.put(s1);
        assertEquals(s1, this.c7.remove(1));
        assertEquals(0, this.c1.size);
    }

    //Student to remove not in course
    @Test
    @Tag("score:3")
    @DisplayName("Remove Test 2")
    void testRemove2() {
        this.c7.put(s1);
        assertNull(this.c7.remove(0));
        assertEquals(1, this.c7.size);
    }

    //Student to remove in bucket
    @Test
    @Tag("score:4")
    @DisplayName("Remove Test 3")
    void testRemove3() {
        this.c7.put(new Student(9, "a"));
        this.c7.put(s5);
        this.c7.put(s1);
        assertEquals(s5, this.c7.remove(5));
        assertEquals(2, this.c7.size);
    }
    //Basic change check
    @Test
    @Tag("score:6")
    @DisplayName("ChangeArrayLength Test 1")
    void testChangeArrayLength1() {
        this.c9.put(s2);
        this.c9.put(s4);
        c9.changeArrayLength(3);
        assertEquals(3, this.c9.studentTable.length);
        assertNotNull(this.c9.studentTable[1]);
        assertEquals(s4, this.c9.studentTable[1].getFirst());
        assertNotNull(this.c9.studentTable[2]);
        assertEquals(s2, this.c9.studentTable[2].getFirst());
    }

    //Basic get check
    @Test
    @Tag("score:2")
    @DisplayName("Get Test 1")
    void testGet1() {
        this.c7.put(s1);
        this.c7.put(s2);
        assertEquals(s1, this.c7.get(1));
    }

    //Student not in table
    @Test
    @Tag("score:2")
    @DisplayName("Get Test 2")
    void testGet2() {
        this.c7.put(s1);
        assertNull(this.c7.get(2));
    }

    //Student in bucket
    @Test
    @Tag("score:3")
    @DisplayName("Get Test 3")
    void testGet3() {
        this.c7.put(s1);
        this.c7.put(s4);
        this.c7.put(s8);
        this.c7.put(s12);
        assertEquals(s4, this.c7.get(4));
        assertEquals(s8, this.c7.get(8));
        assertEquals(s12, this.c7.get(12));
    }

    //Basic size check
    @Test
    @Tag("score:1")
    @DisplayName("GetCourseSize Test 1")
    void testGetCourseSize1() {
        assertEquals(0, this.c1.getCourseSize());
        assertTrue(this.c1.put(s1));
        assertEquals(1, this.c1.getCourseSize());
    }


    //With remove
    @Test
    @Tag("score:1")
    @DisplayName("GetCourseSize Test 2")
    void testGetCourseSize2() {
        assertEquals(0, this.c1.getCourseSize());
        assertTrue(this.c1.put(s1));
        assertEquals(1, this.c1.getCourseSize());
        this.c1.remove(1);
        assertEquals(0, this.c1.getCourseSize());
    }

    //Do not include waitlist
    @Test
    @Tag("score:1")
    @DisplayName("GetCourseSize Test 3")
    void testGetCourseSize3() {
        assertEquals(0, this.c4.getCourseSize());
        assertTrue(this.c4.put(s1));
        assertEquals(1, this.c4.getCourseSize());
        assertTrue(this.c4.put(s2));
        assertEquals(2, this.c4.getCourseSize());
        assertTrue(this.c4.put(s3));
        assertEquals(3, this.c4.getCourseSize());
        assertTrue(this.c4.put(s5));
        assertEquals(4, this.c4.getCourseSize());
        assertTrue(this.c4.put(s4));
        assertEquals(4, this.c4.getCourseSize());
    }

    @Test
    @Tag("score:2")
    @DisplayName("GetCourseSize Test 4")
    void testGetCourseSize4() {
        assertEquals(0, this.c4.getCourseSize());
        assertTrue(this.c4.put(s1));
        assertEquals(1, this.c4.getCourseSize());
        assertTrue(this.c4.put(s2));
        assertEquals(2, this.c4.getCourseSize());
        assertTrue(this.c4.put(s3));
        assertEquals(3, this.c4.getCourseSize());
        assertTrue(this.c4.put(s5));
        assertEquals(4, this.c4.getCourseSize());
        assertTrue(this.c4.put(s4));
        assertEquals(4, this.c4.getCourseSize());

        c4.remove(4);
        assertEquals(4, this.c4.getCourseSize());
        c4.remove(3); c4.remove(2); c4.remove(1); c4.remove(5);
        assertEquals(0, this.c4.getCourseSize());
        c4.remove(5);
        assertEquals(0, this.c4.getCourseSize());
    }

    //Basic
    @Test
    @Tag("score:3")
    @DisplayName("GetRegisteredStudents Test 1")
    void testGetRegisteredStudents1() {
        this.c7.put(s2);
        this.c7.put(s1);
        this.c7.put(s3);
        this.c7.put(s4);
        Set<Student> expected = Set.of(s1,s2,s3,s4);
        Set<Student> actual = new HashSet<>(Arrays.asList(this.c7.getRegisteredStudents()));
        assertEquals(expected, actual);
    }

    //Basic
    @Test
    @Tag("score:3")
    @DisplayName("GetRegisteredIDs Test 1")
    void testGetRegisteredIDs1() {
        this.c7.put(s2);
        this.c7.put(s1);
        this.c7.put(s3);
        this.c7.put(s4);
        Set<Integer> expected = Set.of(1,2,3,4);
        Set<Integer> actual = Arrays.stream(this.c7.getRegisteredIDs()).boxed().collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

    //Basic
    @Test
    @Tag("score:3")
    @DisplayName("GetWaitlistedIDs Test 1")
    void testGetWaitlistedIDs1() {
        this.c7.put(s2);
        this.c7.put(s1);
        this.c7.put(s3);
        this.c7.put(s4);
        this.c7.put(s6);
        this.c7.put(s7);
        Set<Integer> expected = Set.of(6,7);
        Set<Integer> actual = Arrays.stream(this.c7.getWaitlistedIDs()).boxed().collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

    //Basic
    @Test
    @Tag("score:3")
    @DisplayName("GetWaitlistedStudents Test 1")
    void testGetWaitlistedStudents1() {
        this.c7.put(s2);
        this.c7.put(s1);
        this.c7.put(s3);
        this.c7.put(s4);
        this.c7.put(s6);
        this.c7.put(s7);
        Set<Student> expected = Set.of(s6,s7);
        Set<Student> actual = new HashSet<>(Arrays.asList(this.c7.getWaitlistedStudents()));
        assertEquals(expected, actual);
    }
}
