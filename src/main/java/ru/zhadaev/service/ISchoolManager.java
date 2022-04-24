package ru.zhadaev.service;

import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.exception.DAOException;

import java.util.List;

public interface ISchoolManager {
    List<Group> findGroupsByStudentsCount(long studentsCount) throws DAOException;
    List<Student> findStudentsByCourseName(String courseName) throws DAOException;
    List<Group> findGroup(Group group) throws DAOException;
    List<Course> findCourse(Course course) throws DAOException;
    List<Student> findStudent(Student student) throws DAOException;
    Student addNewStudent(Student student) throws DAOException;
    void deleteStudentById(int id) throws DAOException;
    void addStudentToCourses(Student student, List<Course> courses) throws DAOException;
    void removeStudentFromCourse(Student student, Course course) throws DAOException;
}
