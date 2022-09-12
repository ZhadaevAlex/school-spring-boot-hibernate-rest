package ru.zhadaev.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zhadaev.service.CourseService;
import ru.zhadaev.service.GroupService;
import ru.zhadaev.service.SchoolInitializer;
import ru.zhadaev.service.StudentService;

@Configuration
public class InitConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "init-on-start", havingValue = "true")
    public SchoolInitializer schoolInitializer(GroupService groupService,
                                               CourseService courseService,
                                               StudentService studentService) {
        return new SchoolInitializer(groupService, courseService, studentService);
    }

    @Bean
    @ConditionalOnBean(SchoolInitializer.class)
    public InitApp initApp(SchoolInitializer schoolInitializer) {
        return new InitApp(schoolInitializer);
    }
}
