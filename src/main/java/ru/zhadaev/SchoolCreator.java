package ru.zhadaev;

import java.util.*;

public class SchoolCreator {
    private final int numberGroups;
    private final Map<String, String> subjects;
    private final List<String> firstNames;
    private final List<String> lastNames;
    private final int numberStudents;
    private final int minStudentsInGroup;
    private final int maxStudentsInGroup;
    private final int minCourses;
    private final int maxCourses;

    public SchoolCreator(int numberGroups, Map<String, String> subjects, int numberStudents, List<String> firstNames, List<String> lastNames,
                         int minStudentsInGroup, int maxStudentsInGroup, int minCourses, int maxCourses) {
        this.numberGroups = numberGroups;
        this.subjects = subjects;
        this.firstNames = firstNames;
        this.lastNames = lastNames;
        this.numberStudents = numberStudents;
        this.minStudentsInGroup = minStudentsInGroup;
        this.maxStudentsInGroup = maxStudentsInGroup;
        this.minCourses = minCourses;
        this.maxCourses = maxCourses;

    }

    public School createSchool() {
        School school = new School();

        school.setGroups(createGroups(numberGroups));
        school.setCourses(createCourses(subjects));

        school.setStudents(createStudents(numberStudents, firstNames, lastNames,
                school.getGroups(), school.getCourses(), minStudentsInGroup, maxStudentsInGroup, minCourses, maxCourses));

        return school;
    }

    private List<Group> createGroups(int numberGroups) {
        List<Group> groups = new ArrayList<>();

        RandomWords randomWords = new RandomWords();
        for (int i = 0; i < numberGroups; i++) {
            StringBuilder name = new StringBuilder();
            name.append(randomWords.getRandomWord(2, LetterCase.UPPERCASE))
                    .append("-")
                    .append(randomWords.getRandomNumber(10, 99));

            groups.add(new Group(name.toString()));
        }

        return groups;
    }

    private List<Course> createCourses(Map<String, String> subjects) {
        List<Course> courses = new ArrayList<>();

        for (Map.Entry<String, String> item : subjects.entrySet()) {
            Course course = new Course(item.getKey());
            course.setDescription(item.getValue());
            courses.add(course);
        }

        return courses;
    }

    private List<Student> createStudents(int numberStudents, List<String> firstNames, List<String> lastNames,
                                         List<Group> groups, List<Course> courses,
                                         int minStudentsInGroup, int maxStudentsInGroup, int minCourses, int maxCourses) {
        List<Student> students = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i < numberStudents; i++) {
            String firstName = firstNames.get(rnd.nextInt(lastNames.size()));
            String lastName = lastNames.get(rnd.nextInt(lastNames.size()));
            Student student = new Student(firstName, lastName);
            student.setGroup(new Group("No group"));

            //Распределение по курсам
            int numberCourses = minCourses + rnd.nextInt(maxCourses -  minCourses + 1);
            Set<Course> coursesForStudents = new HashSet<>();
            int countCourses = 0;
            while (countCourses < numberCourses) {
                coursesForStudents.add(courses.get(rnd.nextInt(courses.size())));
                countCourses = coursesForStudents.size();
            }
            student.setCourse(coursesForStudents);

            students.add(student);
        }

        //Распределение по группам
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
}
