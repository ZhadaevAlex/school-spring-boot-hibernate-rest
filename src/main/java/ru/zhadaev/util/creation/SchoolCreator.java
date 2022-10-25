package ru.zhadaev.util.creation;

import lombok.RequiredArgsConstructor;
import ru.zhadaev.dao.entities.School;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.util.StudentsDistributor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SchoolCreator {
    private final int numberGroups;
    private final int minStudentsInGroup;
    private final int maxStudentsInGroup;
    private final Map<String, String> subjects;
    private final int minCourses;
    private final int maxCourses;

    private final GroupCreator groupCreator;
    private final CourseCreator courseCreator;
    private final StudentsDistributor studentsDistributor;

    public School createSchool(List<Student> students) {
        requiredNotNull(subjects);
        requiredNumberRowsNotZero(subjects);
        requiredNotNull(students);
        requiredNumberRowsNotZero(students);

        School school = new School();

        school.setGroups(groupCreator.createGroups(numberGroups));
        school.setCourses(courseCreator.createCourses(subjects));

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
