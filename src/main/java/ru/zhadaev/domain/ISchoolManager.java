package ru.zhadaev.domain;

import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.exception.DAOException;

import java.util.List;

public interface ISchoolManager {
    public List<Group> findGroupsByStudentsCount(int studentsCount) throws DAOException;
    public List<Student> findStudentsByCourseName(String courseName);
    public Student addNewStudent(Student student);
    public void deleteStudentById(int id);
    public void addStudentToCourses(List<String> courses);
    public void removeStudentFromCourse(String course);
}
