package ru.zhadaev.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.api.dto.GroupDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupMapper {
    GroupDto toDto(Group group);
    Group toEntity(GroupDto groupDto);
    List<GroupDto> toDto(List<Group> groups);
    void update(GroupDto groupDto, @MappingTarget Group group);
}
