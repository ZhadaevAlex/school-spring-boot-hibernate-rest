package ru.zhadaev.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.api.dto.StudentDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {
    StudentDto studentToStudentDto(Student student);
    Student studentDtoToStudent(StudentDto studentDto);
    List<StudentDto> studentsToStudentsDto(List<Student> students);
    void updateStudentFromDto(StudentDto studentDto, @MappingTarget Student student);
}
