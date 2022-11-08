package ru.zhadaev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dto.GroupDto;
import ru.zhadaev.mappers.GroupMapper;
import ru.zhadaev.service.GroupService;
import ru.zhadaev.service.SchoolManager;
import ru.zhadaev.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
@Validated
public class GroupController {
    private final GroupService groupService;
    private final SchoolManager schoolManager;
    private final GroupMapper groupMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<GroupDto> findAll(@RequestParam("numberStudents") @Valid @Nullable @Min(0) Integer numberStudents) {
        List<Group> groups = (numberStudents == null) ?
                groupService.findAll()
                : schoolManager.findGroupsByNumberStudents(numberStudents);
        return groupMapper.groupsToGroupsDto(groups);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnPostPut.class)
    public GroupDto save(@RequestBody @Valid GroupDto groupDto) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        Group saved = groupService.save(group);
        GroupDto savedDto = groupMapper.groupToGroupDto(saved);
        UUID id = saved.getId();
        return savedDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GroupDto findById(@PathVariable("id") UUID id) {
        Group group = groupService.findById(id);
        return groupMapper.groupToGroupDto(group);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") UUID id) {
        groupService.deleteById(id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll() {
        groupService.deleteAll();
    }

    @PutMapping("/{id}")
    @Validated(Marker.OnPostPut.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GroupDto replace(@RequestBody @Valid GroupDto groupDto, @PathVariable("id") UUID id) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        Group replaced = groupService.update(group, id);
        return groupMapper.groupToGroupDto(replaced);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GroupDto update(@RequestBody GroupDto groupDto, @PathVariable("id") UUID id) {
        Group group = groupService.findById(id);
        groupMapper.updateGroupFromDto(groupDto, group);
        Group updated = groupService.update(group, id);
        return groupMapper.groupToGroupDto(updated);
    }
}
