package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.api.dto.CourseDto;
import ru.zhadaev.api.mappers.CourseMapper;
import ru.zhadaev.api.errors.NotFoundException;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.repository.impl.CourseDAO;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CourseService {
    private final CourseDAO courseDAO;
    private final CourseMapper mapper;

    public CourseDto save(CourseDto courseDto) {
        Course course = mapper.toEntity(courseDto);
        Course saved = courseDAO.save(course);
        UUID id = saved.getId();
        return mapper.toDto(saved);
    }

    public CourseDto replace(CourseDto courseDto, UUID id) {
        if (!existsById(id)) throw new NotFoundException("Group replace error. Group not found by id");
        Course course = mapper.toEntity(courseDto);
        course.setId(id);
        Course replaced = courseDAO.update(course);
        return mapper.toDto(replaced);
    }

    public CourseDto update(CourseDto courseDto, UUID id) {
        Course course = courseDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Course update error. Course not found by id"));
        mapper.update(courseDto, course);
        return mapper.toDto(course);
    }

    public CourseDto findById(UUID id) {
        Course course = courseDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found by id"));
        return mapper.toDto(course);
    }

    public List<CourseDto> find(CourseDto courseDto) {
        Course course = mapper.toEntity(courseDto);
        List<Course> courses = courseDAO.findLike(course);
        if (courses.isEmpty()) {
            throw new NotFoundException("Courses not found by id");
        }

        return mapper.toDto(courses);
    }

    public List<CourseDto> findAll() {
        List<Course> courses = courseDAO.findAll();
        return mapper.toDto(courses);
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
