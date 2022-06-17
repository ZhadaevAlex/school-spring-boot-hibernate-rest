package ru.zhadaev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@ComponentScan("ru.zhadaev")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    private static final String FILE_PROP_NAME = "application.properties";
    private static final String URL_PROP_NAME = "URL";
    private static final String USER_PROP_NAME = "USER";
    private static final String PASSWORD_PROP_NAME = "PASSWORD";

    private final ApplicationContext applicationContext;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    PropertiesReader propertiesReader() {
        return new PropertiesReader(FILE_PROP_NAME);
    }

    @Bean
    ConnectionManager connectionManager(PropertiesReader propertiesReader) {

        String url = propertiesReader.getProperty(URL_PROP_NAME);
        String user = propertiesReader.getProperty(USER_PROP_NAME);
        String password = propertiesReader.getProperty(PASSWORD_PROP_NAME);

        return new ConnectionManager(url, user, password);
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
}
