package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.api.dto.StudentDto;
import ru.zhadaev.api.errors.NotFoundException;
import ru.zhadaev.api.mappers.StudentMapper;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.StudentDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class StudentService {
    private final StudentDAO studentDAO;
    private final StudentMapper mapper;

    public StudentDto save(StudentDto studentDto) {
        Student student = mapper.toEntity(studentDto);
        Student saved = studentDAO.save(student);
        return mapper.toDto(saved);
    }

    public StudentDto replace(StudentDto studentDto, UUID id) {
        if (!existsById(id)) throw new NotFoundException("Student replace error. Student not found by id");
        Student student = mapper.toEntity(studentDto);
        student.setId(id);
        Student replaced = studentDAO.update(student);
        return mapper.toDto(replaced);
    }

    public StudentDto update(StudentDto studentDto, UUID id) {
        Student student = studentDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Student update error. Student not found by id"));
        mapper.update(studentDto, student);
        return mapper.toDto(student);
    }

    public StudentDto findById(UUID id) {
        Student student = studentDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found by id"));
        return mapper.toDto(student);
    }

    public List<StudentDto> find(StudentDto studentDto) {
        Student student = mapper.toEntity(studentDto);
        List<Student> students = studentDAO.findLike(student);
        if (students.isEmpty()) {
            throw new NotFoundException("Students not found by id");
        }
        return mapper.toDto(students);
    }

    public List<StudentDto> findAll(UUID courseId) {
        List<Student> students = (courseId == null) ?
                studentDAO.findAll()
                : findStudentsByCourseId(courseId);
        return mapper.toDto(students);
    }

    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    public boolean existsById(UUID id) {
        return studentDAO.existsById(id);
    }

    public long count() {
        return studentDAO.count();
    }

    public void deleteById(UUID id) {
        if (studentDAO.existsById(id)) {
            studentDAO.deleteById(id);
        } else {
            throw new NotFoundException("Student delete error. Student not found by id");
        }

        studentDAO.deleteById(id);
    }

    public void delete(Student student) {
        studentDAO.delete(student);
    }

    public void deleteAll() {
        studentDAO.deleteAll();
    }

    public List<Student> findStudentsByCourseId(UUID courseId) {
        List<Student> studentsDb = studentDAO.findAll();
        List<Student> studentsByCourse = new ArrayList<>();

        for (Student student : studentsDb) {
            boolean contains = student.getCourses().stream()
                    .anyMatch(p -> p.getId().equals(courseId));

            if (contains) {
                studentsByCourse.add(student);
            }
        }

        return studentsByCourse;
    }
}
