package ru.zhadaev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dto.StudentDto;
import ru.zhadaev.mappers.StudentMapper;
import ru.zhadaev.service.SchoolManager;
import ru.zhadaev.service.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;
    private final SchoolManager schoolManager;
    private final StudentMapper studentMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> findAll(@Nullable @RequestParam("courseId") UUID courseId) {
        List<Student> students = (courseId == null) ?
                studentService.findAll()
                : schoolManager.findStudentsByCourseId(courseId);
        return studentMapper.studentsToStudentsDto(students);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDto save(@RequestBody StudentDto studentDto) {
        Student student = studentMapper.studentDtoToStudent(studentDto);
        Student saved = studentService.save(student);
        return studentMapper.studentToStudentDto(saved);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDto findById(@PathVariable("id") UUID id) {
        Student student = studentService.findById(id);
        return studentMapper.studentToStudentDto(student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") UUID id) {
        studentService.deleteById(id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll() {
        studentService.deleteAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StudentDto replace(@RequestBody StudentDto studentDto, @PathVariable UUID id) {
        Student student = studentMapper.studentDtoToStudent(studentDto);
        Student replaced = studentService.update(student, id);
        return studentMapper.studentToStudentDto(replaced);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StudentDto update(@RequestBody StudentDto studentDto, @PathVariable("id") UUID id) {
        Student student = studentService.findById(id);
        studentMapper.updateStudentFromDto(studentDto, student);
        Student updated = studentService.update(student, id);
        return studentMapper.studentToStudentDto(updated);
    }
}