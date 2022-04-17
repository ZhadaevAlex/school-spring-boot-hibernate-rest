package ru.zhadaev.domain.impl;

import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.config.PropertiesReader;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.dao.repository.impl.StudentRepository;
import ru.zhadaev.domain.ISchoolManager;
import ru.zhadaev.exception.DAOException;

import java.util.List;

public class SchoolManager implements ISchoolManager {
    ConnectionManager connectionManager;

    public SchoolManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    @Override
    public List<Group> findGroupsByStudentsCount(int studentsCount) throws DAOException {
        CrudRepository<Student, Integer> studentRepository = new StudentRepository(connectionManager);
        List<Student> students = studentRepository.findAll();
        return null;
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        return null;
    }

    @Override
    public Student addNewStudent(Student student) {
        return null;
    }

    @Override
    public void deleteStudentById(int id) {

    }

    @Override
    public void addStudentToCourses(List<String> courses) {

    }

    @Override
    public void removeStudentFromCourse(String course) {

    }
}
