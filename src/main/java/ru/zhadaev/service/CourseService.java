package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.exception.NotFoundException;
import ru.zhadaev.exception.NotValidCourseException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseDAO courseDAO;

    public Course save(Course course) {
        requiredNotNull(course);
        return courseDAO.save(course);
    }

    public Course update(Course course, UUID id) {
        requiredNotNull(course);
        requiredIdIsValid(id);
        course.setId(id);
        return courseDAO.update(course);
    }

    public Course findById(UUID id) {
        requiredIdIsValid(id);
        return courseDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    public List<Course> find(Course course) {
        requiredNotNull(course);
        List<Course> courseDb = courseDAO.findLike(course);

        if (courseDb.isEmpty()) {
            throw new NotFoundException("Courses not found");
        }

        return courseDb;
    }

    public List<Course> findAll() {
        return courseDAO.findAll();
    }

    public boolean existsById(UUID id) {
        requiredIdIsValid(id);
        return courseDAO.existsById(id);
    }

    public long count() {
        return courseDAO.count();
    }

    public void deleteById(UUID id) {
        requiredIdIsValid(id);

        if (courseDAO.existsById(id)) {
            courseDAO.deleteById(id);
        } else {
            logger.error("Course delete error. Course not found by id");
            throw new NotFoundException("Course delete error. Course not found by id");
        }
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

    private void requiredIdIsValid(UUID id) {
        if (id == null) {
            logger.error("The id value must be non-null and greater than 0");
            throw new NotValidCourseException("The id value must be non-null and greater than 0");
        }
    }
}
