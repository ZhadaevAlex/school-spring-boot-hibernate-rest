package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.api.dto.StudentDto;
import ru.zhadaev.api.mappers.StudentMapper;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentDAO studentDAO;
    private final StudentMapper studentMapper;

    public StudentDto save(StudentDto studentDto) {
        Student student = studentMapper.studentDtoToStudent(studentDto);
        Student saved = studentDAO.save(student);
        return studentMapper.studentToStudentDto(saved);
    }

    public StudentDto replace(StudentDto studentDto, UUID id) {
        Student student = studentMapper.studentDtoToStudent(studentDto);
        student.setId(id);
        Student replaced = studentDAO.update(student);
        return studentMapper.studentToStudentDto(replaced);
    }

    public StudentDto update(StudentDto studentDto, UUID id) {
        Student student = studentDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        studentMapper.updateStudentFromDto(studentDto, student);
//        Student updated = studentDAO.update(student);
        return studentMapper.studentToStudentDto(student);
    }

    public StudentDto findById(UUID id) {
        Student student = studentDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        return studentMapper.studentToStudentDto(student);
    }

    public List<Student> find(Student student) {
        List<Student> studentDb = studentDAO.findLike(student);

        if (studentDb.isEmpty()) {
            throw new NotFoundException("Students not found");
        }

        return studentDb;
    }

    public List<StudentDto> findAll(UUID courseId) {
        List<Student> students = (courseId == null) ?
                studentDAO.findAll()
                : findStudentsByCourseId(courseId);
        return studentMapper.studentsToStudentsDto(students);
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
            logger.error("Student delete error. Student not found by id");
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
