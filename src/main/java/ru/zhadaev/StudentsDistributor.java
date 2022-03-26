package ru.zhadaev;

import java.util.*;

public class StudentsDistributor {
    public List<Student> distributionByCourses(List<Student> students, List<Course> courses, int minNumberCourses, int maxNumberCourses) {
        requiredNotNull(students);
        requiredNotNull(courses);
        requiredNumberRowsNotZero(students);
        requiredNumberRowsNotZero(courses);

        Random rnd = new Random();
        for (Student student : students) {
            int numberCourses = minNumberCourses + rnd.nextInt(maxNumberCourses - minNumberCourses + 1);
            Set<Course> coursesForStudents = new HashSet<>();
            int countCourses = 0;
            while (countCourses < numberCourses) {
                coursesForStudents.add(courses.get(rnd.nextInt(courses.size())));
                countCourses = coursesForStudents.size();
            }

            student.setCourse(coursesForStudents);
        }

        return students;
    }

    public List<Student> distributeByGroups(List<Student> students, Set<Group> groups, int minStudentsInGroup, int maxStudentsInGroup) {
        requiredNotNull(students);
        requiredNotNull(groups);
        requiredNumberRowsNotZero(students);
        requiredNumberRowsNotZero(groups);

        Random rnd = new Random();
        int nextGroupPointer = 0;
        for (Group group : groups) {
            int studentsInGroup = minStudentsInGroup + rnd.nextInt(maxStudentsInGroup - minStudentsInGroup + 1);
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
