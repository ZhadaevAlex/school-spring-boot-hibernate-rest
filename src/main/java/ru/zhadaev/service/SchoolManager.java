package ru.zhadaev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.exception.NotValidStudentException;

import java.util.ArrayList;
import java.util.List;

@Component
public class SchoolManager {
    private static final Logger logger = LoggerFactory.getLogger(SchoolManager.class);

    private final GroupService groupService;
    private final StudentService studentService;

    @Autowired
    public SchoolManager(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    public List<Group> findGroupsByNumberStudents(long numberStudents) {
        List<Student> studentsDb = studentService.findAll();
        List<Group> groupsDb = groupService.findAll();
        List<Group> result = new ArrayList<>();

        for (Group group : groupsDb) {
            long numberStudentsInGroup = studentsDb.stream()
                    .filter(p -> p.getGroup() != null && p.getGroup().getId().equals(group.getId()))
                    .count();

            if (numberStudentsInGroup <= numberStudents) {
                result.add(group);
            }
        }

        return result;
    }

    public List<Student> findStudentsByCourse(Integer courseId) {
        requiredIdIsValid(courseId);

        List<Student> studentsDb = studentService.findAll();
        List<Student> studentsByCourse = new ArrayList<>();

        for (Student student : studentsDb) {
            boolean contains = student.getCourses().stream()
                    .anyMatch(p -> p.getId().equals(courseId));

            if (contains) {
                studentsByCourse.add(student);
            }
        }

        return studentsByCourse;
    }

    public List<Student> findStudentsByCoursesName(List<Integer> coursesId) {
        List<Student> students = new ArrayList<>();

        for (Integer courseId : coursesId) {
            students.addAll(findStudentsByCourse(courseId));
        }

        return students;
    }

    private void requiredIdIsValid(Integer id) {
        if (id == null || id < 1) {
            logger.error("The id value must be non-null and greater than 0");
            throw new NotValidStudentException("The id value must be non-null and greater than 0");
        }
    }
}
