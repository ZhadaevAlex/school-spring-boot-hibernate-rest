package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.NotFoundException;
import ru.zhadaev.exception.NotValidStudentException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentDAO studentDAO;

    public Student save(Student student) {
        requiredNotNull(student);
        return studentDAO.save(student);
    }

    public Student update(Student student, UUID id) {
        requiredNotNull(student);
        requiredIdIsValid(id);
        student.setId(id);
        return studentDAO.update(student);
    }

    public Student findById(UUID id) {
        requiredIdIsValid(id);
        return studentDAO.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public List<Student> find(Student student) {
        requiredNotNull(student);

        List<Student> studentDb = studentDAO.findLike(student);

        if (studentDb.isEmpty()) {
            throw new NotFoundException("Students not found");
        }

        return studentDb;
    }

    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    public boolean existsById(UUID id) {
        requiredIdIsValid(id);

        return studentDAO.existsById(id);
    }

    public long count() {
        return studentDAO.count();
    }

    public void deleteById(UUID id) {
        requiredIdIsValid(id);

        if (studentDAO.existsById(id)) {
            studentDAO.deleteById(id);
        } else {
            logger.error("Student delete error. Student not found by id");
            throw new NotFoundException("Student delete error. Student not found by id");
        }

        studentDAO.deleteById(id);
    }

    public void delete(Student student) {
        requiredNotNull(student);
        studentDAO.delete(student);
    }

    public void deleteAll() {
        studentDAO.deleteAll();
    }

    private void requiredNotNull(Student student) {
        if (student == null) {
            logger.error("Student required is not null!");
            throw new NotValidStudentException("Student required is not null!");
        }
    }

    private void requiredIdIsValid(UUID id) {
        if (id == null) {
            logger.error("The id value must be non-null and greater than 0");
            throw new NotValidStudentException("The id value must be non-null and greater than 0");
        }
    }
}
