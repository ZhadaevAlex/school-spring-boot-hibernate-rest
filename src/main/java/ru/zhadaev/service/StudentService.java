package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.NotFoundException;
import ru.zhadaev.exception.NotValidStudentException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentDAO studentDAO;
    private final CourseService courseService;
    private final GroupService groupService;

    public Student save(Student student) {
        requiredNotNull(student);
        return studentDAO.save(student);
    }

    public Student update(Map<String, String> updatedData, Integer id) {
        Student student = findById(id);
        Group group = groupService.findById(Integer.parseInt(updatedData.get("groupId")));
        student.setFirstName(updatedData.get("firstName"));
        student.setLastName(updatedData.get("lastName"));
        student.setGroup(group);
        return studentDAO.update(student);
    }

    public Student findById(Integer id) {
        requiredIdIsValid(id);
        return studentDAO.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public List<Student> find(Student student) {
        requiredNotNull(student);

        List<Student> studentDb = studentDAO.findLike(student);

        if (studentDb.isEmpty()) {
            throw new NotFoundException("Students not found");
        }

        return studentDb;
    }

    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    public boolean existsById(Integer id) {
        requiredIdIsValid(id);

        return studentDAO.existsById(id);
    }

    public long count() {
        return studentDAO.count();
    }

    public void deleteById(Integer id) {
        requiredIdIsValid(id);

        if (studentDAO.existsById(id)) {
            studentDAO.deleteById(id);
        } else {
            logger.error("Student delete error. Student not found by id");
            throw new NotFoundException("Student delete error. Student not found by id");
        }

        studentDAO.deleteById(id);
    }

    public void delete(Student student) {
        requiredNotNull(student);
        studentDAO.delete(student);
    }

    public void deleteAll() {
        studentDAO.deleteAll();
    }

    public void signOnCourses(Integer studentId, List<Integer> coursesId) {
        requiredIdIsValid(studentId);
        requiredStudentIsExist(studentId);
        Student student = findById(studentId);

        Set<Course> courses = (student.getCourses() == null) ?
                new HashSet<>() : student.getCourses();

        for (Integer courseId : coursesId) {
            requiredIdIsValid(courseId);
            requiredCourseIsExist(courseId);
            courses.add(courseService.findById(courseId));
        }

        student.setCourses(courses);
        studentDAO.update(student);
    }

    public void removeFromCourses(Integer studentId, List<Integer> coursesId) {
        requiredIdIsValid(studentId);
        requiredStudentIsExist(studentId);
        Student student = findById(studentId);

        Set<Course> courses = (student.getCourses() == null) ?
                new HashSet<>() : student.getCourses();

        for (Integer courseId : coursesId) {
            requiredIdIsValid(courseId);
            requiredCourseIsExist(courseId);
            Course course = courses.stream().filter(p -> p.getId() == courseId).findFirst().get();
            courses.remove(course);
        }

        student.setCourses(courses);
        studentDAO.update(student);
    }

    private void requiredNotNull(Student student) {
        if (student == null) {
            logger.error("Student required is not null!");
            throw new NotValidStudentException("Student required is not null!");
        }
    }

    private void requiredStudentIsExist(Integer id) {
        if (!studentDAO.existsById(id)) {
            logger.error("The student's course was not found in the database");
            throw new NotFoundException("The student's was not found in the database");
        }
    }

    private void requiredCourseIsExist(Integer id) {
        if (!courseService.existsById(id)) {
            logger.error("The student's course was not found in the database");
            throw new NotFoundException("The student's course was not found in the database");
        }
    }

    private void requiredIdIsValid(Integer id) {
        if (id == null || id < 1) {
            logger.error("The id value must be non-null and greater than 0");
            throw new NotValidStudentException("The id value must be non-null and greater than 0");
        }
    }
}
