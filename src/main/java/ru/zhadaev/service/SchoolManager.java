package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.exception.NotValidStudentException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SchoolManager {
    private static final Logger logger = LoggerFactory.getLogger(SchoolManager.class);
    private final GroupService groupService;
    private final StudentService studentService;

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

    public List<Student> findStudentsByCourse(UUID courseId) {
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

    public List<Student> findStudentsByCourseId(UUID courseId) {
        requiredIdIsValid(courseId);
        return findStudentsByCourse(courseId);
    }

    private void requiredIdIsValid(UUID id) {
        if (id == null) {
            logger.error("The id value must be non-null and greater than 0");
            throw new NotValidStudentException("The id value must be non-null and greater than 0");
        }
    }
}
