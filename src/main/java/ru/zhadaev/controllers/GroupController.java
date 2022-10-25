package ru.zhadaev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dto.GroupDto;
import ru.zhadaev.mappers.GroupMapper;
import ru.zhadaev.service.GroupService;
import ru.zhadaev.service.SchoolManager;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;
    private final SchoolManager schoolManager;
    private final GroupMapper groupMapper;

    @GetMapping()
    public List<GroupDto> findAll() {
        List<Group> groups = groupService.findAll();
        return groupMapper.groupsToGroupsDto(groups);
    }

    @PostMapping()
    public GroupDto save(@RequestBody GroupDto groupDto) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        Group saved = groupService.save(group);
        GroupDto savedDto = groupMapper.groupToGroupDto(saved);
        Integer id = saved.getId();
        return savedDto;
    }

    @GetMapping("/{id}")
    public GroupDto findById(@PathVariable("id") Integer id) {
        Group group = groupService.findById(id);
        return groupMapper.groupToGroupDto(group);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        groupService.deleteById(id);
    }

    @DeleteMapping()
    public void deleteAll() {
        groupService.deleteAll();
    }

    @PutMapping("/{id}")
    public GroupDto replace(@RequestBody GroupDto groupDto, @PathVariable("id") Integer id) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        Group replaced = groupService.update(group, id);
        return groupMapper.groupToGroupDto(replaced);
    }

    @PatchMapping("/{id}")
    public GroupDto update(@RequestBody GroupDto groupDto, @PathVariable("id") Integer id) {
        Group group = groupService.findById(id);
        groupMapper.updateGroupFromDto(groupDto, group);
        Group updated = groupService.update(group, id);
        return groupMapper.groupToGroupDto(updated);
    }

    @GetMapping("/filter")
    public List<GroupDto> findAllFilter(@RequestParam("numberStudents") Integer numberStudents) {
        List<Group> groups = schoolManager.findGroupsByNumberStudents(numberStudents);
        return groupMapper.groupsToGroupsDto(groups);
    }
}
