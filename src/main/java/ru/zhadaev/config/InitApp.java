package ru.zhadaev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.School;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.IsNotFileException;
import ru.zhadaev.service.SchoolInitData;
import ru.zhadaev.service.SchoolInitializer;
import ru.zhadaev.util.creation.SchoolCreator;
import ru.zhadaev.util.creation.StudentsCreator;

import java.nio.file.NoSuchFileException;
import java.util.List;

@Component
public class InitApp {

    @Autowired
    public InitApp(SchoolInitializer schoolInitializer) throws NoSuchFileException, DAOException, IsNotFileException {
        SchoolInitData schoolInitData = new SchoolInitData();
        List<Student> students = new StudentsCreator().createStudents(
                SchoolInitData.NUMBER_STUDENTS,
                schoolInitData.getFirstNames(),
                schoolInitData.getLastNames());

        School school = new SchoolCreator(
                SchoolInitData.NUMBER_GROUPS,
                SchoolInitData.MIN_STUDENTS_IN_GROUP,
                SchoolInitData.MAX_STUDENTS_IN_GROUP,
                schoolInitData.getSubjects(),
                SchoolInitData.MIN_COURSES,
                SchoolInitData.MAX_COURSES).createSchool(students);

        schoolInitializer.initialize(school);
    }
}
