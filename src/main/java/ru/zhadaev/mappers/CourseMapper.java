package ru.zhadaev.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dto.CourseDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {
    CourseDto courseToCourseDto(Course course);
    Course courseDtoToCourse(CourseDto courseDto);
    List<CourseDto> coursesToCoursesDto(List<Course> courses);
    void updateCourseFromDto(CourseDto courseDto, @MappingTarget Course course);
}
