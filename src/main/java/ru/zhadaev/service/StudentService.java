package ru.zhadaev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.*;

import java.util.List;

@Component
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final CourseDAO courseDAO;
    private final StudentDAO studentDAO;

    @Autowired
    public StudentService(CourseDAO courseDAO, StudentDAO studentDAO) {
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
    }

    public Student save(Student student) {
        requiredNotNull(student);

        return studentDAO.save(student);
    }

    public Student update(Student student) {
        requiredNotNull(student);

        return studentDAO.update(student);
    }

    public Student findById(Integer id) {
        requiredIdIsValid(id);

        return studentDAO.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public List<Student> find(Student student) {
        requiredNotNull(student);

        List<Student> studentDb = studentDAO.find(student).get();

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

    public long count() throws DAOException {
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

        for (Integer courseId : coursesId) {
            requiredIdIsValid(courseId);
            requiredCourseIsExist(courseId);
            if (!studentCourseIsExist(studentId, courseId)) {
                studentDAO.signOnCourse(studentId, courseId);
            }
        }
    }

    public void removeFromCourses(Integer studentId, List<Integer> coursesId) {
        requiredIdIsValid(studentId);
        requiredStudentIsExist(studentId);

        for (Integer courseId : coursesId) {
            requiredIdIsValid(courseId);
            requiredCourseIsExist(courseId);
            studentDAO.removeFromCourse(studentId, courseId);
        }
    }

    public boolean studentCourseIsExist(Integer studentId, Integer courseId) {
        requiredIdIsValid(studentId);
        requiredStudentIsExist(studentId);
        requiredIdIsValid(courseId);
        requiredCourseIsExist(courseId);

        return studentDAO.studentCourseIsExist(studentId, courseId);
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
        if (!courseDAO.existsById(id)) {
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
