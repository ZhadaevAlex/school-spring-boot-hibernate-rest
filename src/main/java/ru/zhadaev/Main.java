package ru.zhadaev;

import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.config.PropertiesReader;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.dao.repository.impl.GroupRepository;
import ru.zhadaev.domain.ISchoolManager;
import ru.zhadaev.domain.SchoolInitData;
import ru.zhadaev.domain.SchoolInitializer;
import ru.zhadaev.domain.impl.SchoolManager;
import ru.zhadaev.exception.IsNotFileException;
import ru.zhadaev.util.creation.SchoolCreator;
import ru.zhadaev.util.creation.StudentsCreator;

import java.nio.file.NoSuchFileException;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws SQLException, NoSuchFileException, IsNotFileException {
        System.out.println("1. Find all groups with less or equals student count");
        System.out.println("2. Find all students related to course with given name");
        System.out.println("3. Add new student");
        System.out.println("4. Delete student by STUDENT_ID");
        System.out.println("5. Add a student to the course (from a list)");
        System.out.println("6. Remove the student from one of his or her courses");

        SchoolInitializer schoolInitializer = new SchoolInitializer();
        schoolInitializer.initialize();


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
//        int bb = 2;




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
