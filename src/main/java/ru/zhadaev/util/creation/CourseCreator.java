package ru.zhadaev.util.creation;

import ru.zhadaev.dao.entities.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseCreator {
    public List<Course> createCourses(Map<String, String> subjects) {
        requiredNotNull(subjects);
        requiredNumberRowsNotZero(subjects);

        List<Course> courses = new ArrayList<>();

        for (Map.Entry<String, String> item : subjects.entrySet()) {
            Course course = new Course();
            course.setName(item.getKey());
            course.setDescription(item.getValue());
            courses.add(course);
        }

        return courses;
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
}
