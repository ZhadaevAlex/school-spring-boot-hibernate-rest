package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.School;
import ru.zhadaev.dao.entities.Student;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SchoolInitializer {
    private final GroupService groupService;
    private final CourseService courseService;
    private final StudentService studentService;

    public void initialize(School school) {
        List<Group> groupsDb = initializeGroups(school.getGroups());
        List<Course> coursesDb = initializeCourses(school.getCourses());

        for (Student student : school.getStudents()) {
            if (student.getGroup() != null) {
                Integer id = groupsDb.stream()
                        .filter(p -> p.getName()
                                .equals(student.getGroup().getName()))
                        .findFirst().get().getId();
                student.getGroup().setId(id);
            }

            for (Course courseStudent : student.getCourses()) {
                Integer id = coursesDb.stream()
                        .filter(p -> p.getName().equals(courseStudent.getName())
                                && p.getDescription().equals(courseStudent.getDescription()))
                        .findFirst().get().getId();

                courseStudent.setId(id);
            }
        }

        List<Student> studentsDb = initializeStudents(school.getStudents());

        signStudentsOnCourses(studentsDb);
    }

    private List<Group> initializeGroups(List<Group> groups) {
        List<Group> groupsDb = new ArrayList<>();
        for (Group group : groups) {
            groupsDb.add(groupService.save(group));
        }

        return groupsDb;
    }

    private List<Course> initializeCourses(List<Course> courses) {
        List<Course> coursesDb = new ArrayList<>();
        for (Course course : courses) {
            coursesDb.add(courseService.save(course));
        }

        return coursesDb;
    }

    private List<Student> initializeStudents(List<Student> students) {
        List<Student> studentsDb = new ArrayList<>();
        for (Student student : students) {
            studentsDb.add(studentService.save(student));
        }

        return studentsDb;
    }

    private void signStudentsOnCourses(List<Student> students) {

        for (Student student : students) {
            List<Integer> coursesId = new ArrayList<>();

            for (Course course : student.getCourses()) {
                coursesId.add(course.getId());
            }

            studentService.signOnCourses(student.getId(), coursesId);
        }
    }
}
