package ru.zhadaev.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zhadaev.service.*;
import ru.zhadaev.util.StudentsDistributor;
import ru.zhadaev.util.creation.*;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
public class InitConfig {
    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public SecureRandom sequreRandom() {
        return new SecureRandom();
    }

    @Bean
    public RandomWords randomWords(Random random) {
        return new RandomWords(random);
    }

    @Bean
    public GroupCreator groupCreator(RandomWords randomWords) {
        return new GroupCreator(randomWords);
    }

    @Bean
    public CourseCreator courseCreator() {
        return new CourseCreator();
    }

    @Bean
    public StudentsCreator studentsCreator(Random random) {
        return new StudentsCreator(random);
    }

    @Bean
    public StudentsDistributor studentsDistributor(SecureRandom secureRandom) {
        return new StudentsDistributor(secureRandom);
    }

    @Bean
    public SchoolInitData schoolInitData() {
        return new SchoolInitData();
    }

    @Bean
    public SchoolCreator schoolCreator(SchoolInitData schoolInitData,
                                       GroupCreator groupCreator,
                                       CourseCreator courseCreator,
                                       StudentsDistributor studentsDistributor) {
        SchoolCreator schoolCreator = new SchoolCreator(
                SchoolInitData.NUMBER_GROUPS,
                SchoolInitData.MIN_STUDENTS_IN_GROUP,
                SchoolInitData.MAX_STUDENTS_IN_GROUP,
                schoolInitData.getSubjects(),
                SchoolInitData.MIN_COURSES,
                SchoolInitData.MAX_COURSES,
                groupCreator,
                courseCreator,
                studentsDistributor);
        return schoolCreator;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "init-on-start", havingValue = "true")
    public SchoolInitializer schoolInitializer(GroupService groupService,
                                               CourseService courseService,
                                               StudentService studentService) {
        return new SchoolInitializer(groupService, courseService, studentService);
    }

    @Bean
    @ConditionalOnBean(SchoolInitializer.class)
    public InitApp initApp(SchoolInitializer schoolInitializer,
                           SchoolInitData schoolInitData,
                           StudentsCreator studentsCreator,
                           SchoolCreator schoolCreator) {
        return new InitApp(schoolInitializer, schoolInitData, studentsCreator, schoolCreator);
    }
}
