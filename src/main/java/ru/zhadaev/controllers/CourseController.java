package ru.zhadaev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dto.CourseDto;
import ru.zhadaev.mappers.CourseMapper;
import ru.zhadaev.service.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping()
    public List<CourseDto> findAll() {
        List<Course> courses = courseService.findAll();
        return courseMapper.coursesToCoursesDto(courses);
    }

    @PostMapping()
    public CourseDto save(@RequestBody CourseDto courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        Course saved = courseService.save(course);
        CourseDto savedDto = courseMapper.courseToCourseDto(saved);
        Integer id = saved.getId();
        return savedDto;
    }

    @GetMapping("/{id}")
    public CourseDto findById(@PathVariable("id") Integer id) {
        Course course = courseService.findById(id);
        return courseMapper.courseToCourseDto(course);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        courseService.deleteById(id);
    }

    @DeleteMapping()
    public void deleteAll() {
        courseService.deleteAll();
    }

    @PutMapping("/{id}")
    public CourseDto replace(@RequestBody CourseDto courseDto, @PathVariable Integer id) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        Course replaced = courseService.update(course, id);
        return courseMapper.courseToCourseDto(replaced);
    }

    @PatchMapping("/{id}")
    public CourseDto update(@RequestBody CourseDto courseDto, @PathVariable("id") Integer id) {
        Course course = courseService.findById(id);
        courseMapper.updateCourseFromDto(courseDto, course);
        Course updated = courseService.update(course, id);
        return courseMapper.courseToCourseDto(updated);
    }
}
