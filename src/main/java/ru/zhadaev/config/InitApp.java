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
public class InitApp {
    private final SchoolInitializer schoolInitializer;
    private final SchoolInitData schoolInitData;
    private final StudentsCreator studentCreator;
    private final SchoolCreator schoolCreator;

    @PostConstruct
    public void initApp() {
        List<Student> students = studentCreator.createStudents(
                SchoolInitData.NUMBER_STUDENTS,
                schoolInitData.getFirstNames(),
                schoolInitData.getLastNames());

        School school = schoolCreator.createSchool(students);

        schoolInitializer.initialize(school);
    }
}
