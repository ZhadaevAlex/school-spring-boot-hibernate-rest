package ru.zhadaev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dto.CourseDto;
import ru.zhadaev.mappers.CourseMapper;
import ru.zhadaev.service.CourseService;
import ru.zhadaev.validation.Marker;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDto> findAll() {
        List<Course> courses = courseService.findAll();
        return courseMapper.coursesToCoursesDto(courses);
    }

    @PostMapping()
    @Validated(Marker.OnPostPut.class)
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto save(@RequestBody @Valid CourseDto courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        Course saved = courseService.save(course);
        CourseDto savedDto = courseMapper.courseToCourseDto(saved);
        UUID id = saved.getId();
        return savedDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDto findById(@PathVariable("id") UUID id) {
        Course course = courseService.findById(id);
        return courseMapper.courseToCourseDto(course);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") UUID id) {
        courseService.deleteById(id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll() {
        courseService.deleteAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Validated(Marker.OnPostPut.class)
    public CourseDto replace(@RequestBody @Valid CourseDto courseDto, @PathVariable UUID id) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        Course replaced = courseService.update(course, id);
        return courseMapper.courseToCourseDto(replaced);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CourseDto update(@RequestBody CourseDto courseDto, @PathVariable("id") UUID id) {
        Course course = courseService.findById(id);
        courseMapper.updateCourseFromDto(courseDto, course);
        Course updated = courseService.update(course, id);
        return courseMapper.courseToCourseDto(updated);
    }
}
