package ru.zhadaev.util.creation;

import ru.zhadaev.util.StudentsDistributor;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;

import java.util.*;

public class SchoolCreator {
    private final int numberGroups;
    private final int minStudentsInGroup;
    private final int maxStudentsInGroup;
    private final Map<String, String> subjects;
    private final int minCourses;
    private final int maxCourses;

    public SchoolCreator(int numberGroups, int minStudentsInGroup,
                         int maxStudentsInGroup, Map<String, String> subjects,
                         int minCourses, int maxCourses) {

        requiredNotNull(subjects);
        requiredNumberRowsNotZero(subjects);

        this.numberGroups = numberGroups;
        this.minStudentsInGroup = minStudentsInGroup;
        this.maxStudentsInGroup = maxStudentsInGroup;
        this.subjects = subjects;
        this.minCourses = minCourses;
        this.maxCourses = maxCourses;
    }

    public School createSchool(List<Student> students) {
        requiredNotNull(students);
        requiredNumberRowsNotZero(students);

        School school = new School();

        school.setGroups(new GroupCreator().createGroups(numberGroups));
        school.setCourses(new CourseCreator().createCourses(subjects));

        StudentsDistributor studentsDistributor = new StudentsDistributor();
        students = studentsDistributor.distributeByGroups(students, school.getGroups(), minStudentsInGroup, maxStudentsInGroup);
        students = studentsDistributor.distributionByCourses(students, school.getCourses(), minCourses, maxCourses);

        school.setStudents(students);

        return school;
    }

    private void requiredNotNull(Map<String, String> map) {
        if (map == null) {
            throw new IllegalArgumentException("Requires a non-null argument");
        }
    }

    private void requiredNumberRowsNotZero(Map<String, String> map) {
        if (map.isEmpty()) {
            throw new IllegalArgumentException("The number of rows not equal to zero is required");
        }
    }

    private <T> void requiredNotNull(List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("Requires a non-null argument");
        }
    }

    private <T> void requiredNumberRowsNotZero(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("The number of rows not equal to zero is required");
        }
    }
}
