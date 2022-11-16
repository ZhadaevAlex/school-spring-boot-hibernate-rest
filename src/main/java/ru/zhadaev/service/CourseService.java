package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.api.dto.CourseDto;
import ru.zhadaev.api.mappers.CourseMapper;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseDAO courseDAO;
    private final CourseMapper courseMapper;

    public CourseDto save(CourseDto courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        Course saved = courseDAO.save(course);
        UUID id = saved.getId();
        return courseMapper.courseToCourseDto(saved);
    }

    public CourseDto replace(CourseDto courseDto, UUID id) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        course.setId(id);
        Course replaced = courseDAO.update(course);
        return courseMapper.courseToCourseDto(replaced);
    }

    public CourseDto update(CourseDto courseDto, UUID id) {
        Course course = courseDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        courseMapper.updateCourseFromDto(courseDto, course);
        return courseMapper.courseToCourseDto(course);
    }

    public CourseDto findById(UUID id) {
        Course course = courseDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        return courseMapper.courseToCourseDto(course);
    }

    public List<Course> find(Course course) {
        List<Course> courseDb = courseDAO.findLike(course);
        if (courseDb.isEmpty()) {
            throw new NotFoundException("Courses not found");
        }
        return courseDb;
    }

    public List<CourseDto> findAll() {
        List<Course> courses = courseDAO.findAll();
        return courseMapper.coursesToCoursesDto(courses);
    }

    public boolean existsById(UUID id) {
        return courseDAO.existsById(id);
    }

    public long count() {
        return courseDAO.count();
    }

    public void deleteById(UUID id) {
        if (courseDAO.existsById(id)) {
            courseDAO.deleteById(id);
        } else {
            logger.error("Course delete error. Course not found by id");
            throw new NotFoundException("Course delete error. Course not found by id");
        }
    }

    public void delete(Course course) {
        courseDAO.delete(course);
    }

    public void deleteAll() {
        courseDAO.deleteAll();
    }
}
