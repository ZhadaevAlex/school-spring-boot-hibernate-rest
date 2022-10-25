package ru.zhadaev.util;

import lombok.RequiredArgsConstructor;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;

import java.security.SecureRandom;
import java.util.*;

@RequiredArgsConstructor
public class StudentsDistributor {
    private final SecureRandom random;

    public List<Student> distributionByCourses(List<Student> students, List<Course> courses, int minNumberCourses, int maxNumberCourses) {
        requiredNotNull(students);
        requiredNotNull(courses);
        requiredNumberRowsNotZero(students);
        requiredNumberRowsNotZero(courses);

        for (Student student : students) {
            int numberCourses = minNumberCourses + this.random.nextInt(maxNumberCourses - minNumberCourses + 1);
            Set<Course> coursesForStudents = new LinkedHashSet<>();
            int countCourses = 0;
            while (countCourses < numberCourses) {
                coursesForStudents.add(courses.get(this.random.nextInt(courses.size())));
                countCourses = coursesForStudents.size();
            }

            student.setCourses(new HashSet<>(coursesForStudents));
        }

        return students;
    }

    public List<Student> distributeByGroups(List<Student> students, List<Group> groups, int minStudentsInGroup, int maxStudentsInGroup) {
        requiredNotNull(students);
        requiredNotNull(groups);
        requiredNumberRowsNotZero(students);
        requiredNumberRowsNotZero(groups);

        int nextGroupPointer = 0;
        for (Group group : groups) {
            int studentsInGroup = minStudentsInGroup + this.random.nextInt(maxStudentsInGroup - minStudentsInGroup + 1);
            if (nextGroupPointer + studentsInGroup > students.size()) {
                break;
            }

            for (int j = 0; j < studentsInGroup; j++) {
                students.get(nextGroupPointer + j).setGroup(group);
            }

            nextGroupPointer += studentsInGroup;
        }

        return students;
    }


    private <T> void requiredNotNull(Collection<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("Requires a non-null argument");
        }
    }

    private <T> void requiredNumberRowsNotZero(Collection<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("The number of rows not equal to zero is required");
        }
    }
}
