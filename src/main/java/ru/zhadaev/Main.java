package ru.zhadaev;

import ru.zhadaev.config.ConnectionCreator;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.impl.CourseRepository;
import ru.zhadaev.dao.repository.impl.GroupRepository;
import ru.zhadaev.dao.repository.impl.StudentRepository;
import ru.zhadaev.exception.IsNotFileException;
import ru.zhadaev.service.ISchoolManager;
import ru.zhadaev.service.SchoolInitData;
import ru.zhadaev.service.SchoolInitializer;
import ru.zhadaev.service.impl.SchoolManager;
import ru.zhadaev.util.creation.SchoolCreator;
import ru.zhadaev.util.creation.StudentsCreator;

import java.nio.file.NoSuchFileException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, NoSuchFileException, IsNotFileException {
        System.out.println("1. Find all groups with less or equals student count");
        System.out.println("2. Find all students related to course with given name");
        System.out.println("3. Add new student");
        System.out.println("4. Delete student by STUDENT_ID");
        System.out.println("5. Add a student to the course (from a list)");
        System.out.println("6. Remove the student from one of his or her courses");

        SchoolInitData schoolInitData = new SchoolInitData();

        List<Student> students = new StudentsCreator().createStudents(
                SchoolInitData.NUMBER_STUDENTS,
                schoolInitData.getFirstNames(),
                schoolInitData.getLastNames());

        School school = new SchoolCreator(
                SchoolInitData.NUMBER_GROUPS,
                SchoolInitData.MIN_STUDENTS_IN_GROUP,
                SchoolInitData.MAX_STUDENTS_IN_GROUP,
                schoolInitData.getSubjects(),
                SchoolInitData.MIN_COURSES,
                SchoolInitData.MAX_COURSES).createSchool(students);

        ConnectionCreator connectionCreator = new ConnectionCreator();
        ConnectionManager connectionManager = connectionCreator.createConnection();
        SchoolInitializer schoolInitializer = new SchoolInitializer(connectionManager);
        schoolInitializer.initialize(school);

        GroupRepository groupRepository = new GroupRepository(connectionManager);
        CourseRepository courseRepository = new CourseRepository(connectionManager);
        StudentRepository studentRepository = new StudentRepository(connectionManager);

        ISchoolManager manager = new SchoolManager(connectionManager);

        List<Group> result1 = manager.findGroupsByStudentsCount(25);

        List<Student> result2 = manager.findStudentsByCourseName("Economics");

        List<Group> groups3 = groupRepository.findAll();
        Group group3 = new Group(groups3.get(0).getName());
        List<Group> result3 = manager.findGroup(group3);

        Course course4 = new Course("Music");
        course4.setDescription("Subject Music");
        List<Course> coursesDb4 = manager.findCourse(course4);

        List<Student> students5 = studentRepository.findAll();
        Student student5 = new Student(students5.get(0).getFirstName(), students5.get(0).getLastName());
        List<Student> result5 = manager.findStudent(student5);

        Student student6 = new Student("Ivan", "Ivanov");
        Group group61 = new Group(school.getGroups().get(0).getName());
        group61.setId(school.getGroups().get(0).getId());
        student6.setGroup(group61);
        List<Course> coursesAll = courseRepository.findAll();
        List<Course> courses6 = new ArrayList<>();
        Collections.addAll(courses6, coursesAll.get(0), coursesAll.get(1), coursesAll.get(2));
        student6.setCourses(courses6);
        Student studentDb6 = manager.addNewStudent(student6);

        int a = 2;

        manager.deleteStudentById(201);

        a = 3;

        Student student7 = new Student("Petr1", "Petr1");
        student7.setGroup(group61);
        Collections.addAll(courses6, coursesAll.get(3), coursesAll.get(4), coursesAll.get(5));
        student7.setCourses(courses6);
        Student studentDb7 = manager.addNewStudent(student7);

        a = 4;

        manager.removeStudentFromCourse(studentDb7, coursesAll.get(0));

        a = 5;

//        Student student2 = new Student("Petr", "Petrov");
//        student2.setId(null);
//        Group group2 = new Group(school.getGroups().get(0).getName());
//        group2.setId(null);
//        student2.setGroup(group2);
//        List<Course> courses2 = new ArrayList<>();
//        courses2.add(new Course("Math"));
//        courses2.add(new Course("History"));
//        courses2.add(new Course("Astronomy"));
//        student2.setCourses(courses2);
//        Student result4 = manager.addNewStudent(student2);
//
//        Student student3 = new Student("Petr", "Petrov");
//        student3.setId(null);
//        Group group311 = new Group(school.getGroups().get(0).getName());
//        group3.setId(null);
//        student3.setGroup(group2);
//        List<Course> courses3 = new ArrayList<>();
//        courses3.add(new Course("Literature"));
//        student3.setCourses(courses3);
//        Student studentDb = manager.addNewStudent(student3);
//
//        Student studentfind = new Student("Petr", "Petrov");
//        List<Student> studentFindResult = studentRepository.find(studentfind).get();
//        manager.deleteStudentById(201);
//
//        List<Course> courses4 = new ArrayList<>();
//        Course course41 = new Course("Music");
//        course41.setId(5);
//        courses4.add(course41);
//        Course course42 = new Course("Botany");
//        course42.setId(6);
//        courses4.add(course42);
//
//        manager.addStudentToCourses(studentDb, courses4);

        //List<Course> course4 = new ArrayList<>();
//        course4.add(new Course("aaaa"));
//        course4.add(new Course("bbbb"));
//        course4.add(new Course("cccc"));
//        course4.add(new Course("dddd"));
//
//        manager.deleteStudentById(201);
//        int a = 2;


//        ISchoolManager schoolManager = new SchoolManager();
//        schoolManager.findGroupsByStudentsCount(4);


//

//

//
//        int a1 = 2;
//        //groupRepository.deleteAll();
//        //courseRepository.deleteAll();
//        Student student = studentRepository.findById(3).get();
//        boolean is = studentRepository.existsById(4);
//        boolean is1 = studentRepository.existsById(999);
//
//        List<Student> studentsWithId1 = studentRepository.findAll();
//
//        long count = studentRepository.count();
//
//        studentRepository.deleteById(3);
//        studentRepository.deleteAll();
//


//

//

//
//        long a = groupRepository.count();
//        System.out.println(a);
//
//        groupRepository.deleteById(3);
//
//        int b = 2;

//        Course courseIn1 = new Course("Math");
//        courseIn1.setId(1);
//        courseIn1.setDescription("Subject Math");
//        CourseRepository cr = new CourseRepository(connectionManager);
//        Course courseOut1 = cr.save(courseIn1);
//
//        Optional<Course> courseOut2 = cr.findById(1);
//        Optional<Course> courseOut3 = cr.findById(33);
//        List<Course> courses = cr.findAll();
//        long count = cr.count();
//        cr.deleteById(3);
//        cr.delete(courseIn1);
//        cr.deleteAll();


    }
}
