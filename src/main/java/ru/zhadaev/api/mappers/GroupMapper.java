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
    GroupDto groupToGroupDto(Group group);
    Group groupDtoToGroup(GroupDto groupDto);
    List<GroupDto> groupsToGroupsDto(List<Group> groups);
    void updateGroupFromDto(GroupDto groupDto, @MappingTarget Group group);
}
