package ru.zhadaev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.NotFoundException;
import ru.zhadaev.exception.NotValidCourseException;
import ru.zhadaev.exception.NotValidStudentException;

import java.util.List;

@Component
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(SchoolManager.class);

    private final CourseDAO courseDAO;
    private StudentDAO studentDAO;

    @Autowired
    public StudentService(CourseDAO courseDAO, StudentDAO studentDAO) {
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
    }

    public Student save(Student student) throws DAOException {
        requiredNotNull(student);

        return studentDAO.save(student);
    }

    public Student update(Student student) throws DAOException {
        requiredNotNull(student);

        return studentDAO.update(student);
    }

    public Student findById(Integer id) throws DAOException {
        requiredIdIsValid(id);

        Student studentDb = studentDAO.findById(id).get();

        if (studentDb == null) {
            throw new NotFoundException("Student not found");
        }

        return studentDb;
    }

    public List<Student> find(Student student) throws DAOException {
        requiredNotNull(student);

        List<Student> studentDb = studentDAO.find(student).get();

        if (studentDb.isEmpty()) {
            throw new NotFoundException("Groups not found");
        }

        return studentDb;
    }

    public List<Student> findAll() throws DAOException {
        return studentDAO.findAll();
    }

    public boolean existsById(Integer id) throws DAOException {
        requiredIdIsValid(id);

        return studentDAO.existsById(id);
    }

    public long count() throws DAOException {
        return studentDAO.count();
    }

    public void deleteById(Integer id) throws DAOException {
        requiredIdIsValid(id);

        if (studentDAO.existsById(id)) {
            studentDAO.deleteById(id);
        } else {
            logger.error("Student delete error. Student not found by id");
            throw new NotFoundException("Student delete error. Student not found by id");
        }

        studentDAO.deleteById(id);
    }

    public void delete(Student student) throws DAOException {
        requiredNotNull(student);

        studentDAO.delete(student);
    }

    public void deleteAll() throws DAOException {
        studentDAO.deleteAll();
    }

    public void signOnCourses(Integer studentId, List<Integer> coursesId) throws DAOException {
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

    public void removeFromCourses(Integer studentId, List<Integer> coursesId) throws DAOException {
        requiredIdIsValid(studentId);
        requiredStudentIsExist(studentId);

        for (Integer courseId : coursesId) {
            requiredIdIsValid(courseId);
            requiredCourseIsExist(courseId);
            studentDAO.removeFromCourse(studentId, courseId);
        }
    }

    public boolean studentCourseIsExist(Integer studentId, Integer courseId) throws DAOException {
        requiredIdIsValid(studentId);
        requiredStudentIsExist(studentId);
        requiredIdIsValid(courseId);
        requiredCourseIsExist(courseId);

        if (studentDAO.studentCourseIsExist(studentId, courseId)) {
            return true;
        }

        return false;
    }

    private void requiredNotNull(Student student) {
        if (student == null) {
            logger.error("Student required is not null!");
            throw new NotValidStudentException("Student required is not null!");
        }
    }

    private void requiredNotNull(Course course) {
        if (course == null) {
            logger.error("Course required is not null!");
            throw new NotValidCourseException("Course required is not null!");
        }
    }

    private void requiredIdNotNull(Student student) {
        if (student.getId() == null) {
            logger.error("Student ID required is not null!");
            throw new NotValidStudentException("Student ID required is not null!");
        }
    }

    private void requiredIdNotNull(Course course) {
        if (course.getId() == null) {
            logger.error("Course ID required is not null");
            throw new NotValidStudentException("Course ID required is not null");
        }
    }

    private void requiredStudentIsExist(Student student) throws DAOException {
        if (!studentDAO.existsById(student.getId())) {
            logger.error("The student's course was not found in the database");
            throw new NotFoundException("The student's was not found in the database");
        }
    }

    private void requiredStudentIsExist(Integer id) throws DAOException {
        if (!studentDAO.existsById(id)) {
            logger.error("The student's course was not found in the database");
            throw new NotFoundException("The student's was not found in the database");
        }
    }

    private void requiredCourseIsExist(Course course) throws DAOException {
        if (!courseDAO.existsById(course.getId())) {
            logger.error("The student's course was not found in the database");
            throw new NotFoundException("The student's course was not found in the database");
        }
    }

    private void requiredCourseIsExist(Integer id) throws DAOException {
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
