package ru.zhadaev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.NotFoundException;
import ru.zhadaev.exception.NotValidCourseException;
import ru.zhadaev.exception.NotValidStudentException;

import java.util.List;

@Component
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseDAO courseDAO;

    @Autowired
    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public Course save(Course course) {
        requiredNotNull(course);

        return courseDAO.save(course);
    }

    public Course update(Course course) {
        requiredNotNull(course);

        return courseDAO.update(course);
    }

    public Course findById(Integer id) {
        requiredIdIsValid(id);

        return courseDAO.findById(id).orElseThrow(() -> new NotFoundException("Course not found"));
    }

    public List<Course> find(Course course) {
        requiredNotNull(course);

        List<Course> courseDb = courseDAO.find(course).get();

        if (courseDb.isEmpty()) {
            throw new NotFoundException("Courses not found");
        }

        return courseDb;
    }

    public List<Course> findAll() {
        return courseDAO.findAll();
    }

    public boolean existsById(Integer id) {
        requiredIdIsValid(id);

        return courseDAO.existsById(id);
    }

    public long count() {
        return courseDAO.count();
    }

    public void deleteById(Integer id) {
        requiredIdIsValid(id);

        if (courseDAO.existsById(id)) {
            courseDAO.deleteById(id);
        } else {
            logger.error("Course delete error. Course not found by id");
            throw new NotFoundException("Course delete error. Course not found by id");
        }

        courseDAO.deleteById(id);
    }

    public void delete(Course course) {
        requiredNotNull(course);

        courseDAO.delete(course);
    }

    public void deleteAll() {
        courseDAO.deleteAll();
    }

    private void requiredNotNull(Course course) {
        if (course == null) {
            logger.error("Course required is not null!");
            throw new NotValidCourseException("Course required is not null!");
        }
    }

    private void requiredIdIsValid(Integer id) {
        if (id == null || id < 1) {
            logger.error("The id value must be non-null and greater than 0");
            throw new NotValidStudentException("The id value must be non-null and greater than 0");
        }
    }
}
