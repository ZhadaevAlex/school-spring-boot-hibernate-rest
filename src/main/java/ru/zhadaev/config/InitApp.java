package ru.zhadaev.config;

import lombok.RequiredArgsConstructor;
import ru.zhadaev.dao.entities.School;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.service.SchoolInitData;
import ru.zhadaev.service.SchoolInitializer;
import ru.zhadaev.util.creation.SchoolCreator;
import ru.zhadaev.util.creation.StudentsCreator;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
public class InitApp extends StudentsCreator {
    private final SchoolInitializer schoolInitializer;

    @PostConstruct
    public void initApp() {
        List<Student> students = createStudents(
                SchoolInitData.NUMBER_STUDENTS,
                getFirstNames(),
                getLastNames());

        School school = new SchoolCreator(
                SchoolInitData.NUMBER_GROUPS,
                SchoolInitData.MIN_STUDENTS_IN_GROUP,
                SchoolInitData.MAX_STUDENTS_IN_GROUP,
                getSubjects(),
                SchoolInitData.MIN_COURSES,
                SchoolInitData.MAX_COURSES).createSchool(students);

        schoolInitializer.initialize(school);
    }
}
