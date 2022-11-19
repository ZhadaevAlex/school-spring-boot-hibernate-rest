package ru.zhadaev.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.api.dto.CourseDto;
import ru.zhadaev.service.CourseService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;


    @GetMapping()
    public List<CourseDto> findAll() {
        return courseService.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto save(@RequestBody @Valid CourseDto courseDto) {
        return courseService.save(courseDto);
    }

    @GetMapping("/{id}")
    public CourseDto findById(@PathVariable("id") UUID id) {
        return courseService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") UUID id) {
        courseService.deleteById(id);
    }

    @DeleteMapping()
    public void deleteAll() {
        courseService.deleteAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CourseDto replace(@RequestBody @Valid CourseDto courseDto, @PathVariable UUID id) {
        return courseService.replace(courseDto, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CourseDto update(@RequestBody @Valid CourseDto courseDto, @PathVariable("id") UUID id) {
        return courseService.update(courseDto, id);
    }
}
