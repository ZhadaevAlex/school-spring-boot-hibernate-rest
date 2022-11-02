package ru.zhadaev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get all groups",
            description = "This method allows you to get a list of all school groups")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = GroupDto.class)))})})
    @GetMapping()
    public List<GroupDto> findAll() {
        List<Group> groups = groupService.findAll();
        return groupMapper.groupsToGroupsDto(groups);
    }

    @Operation(summary = "Add a new group",
            description = "This method adds a new group to the school")
    @RequestBody(
            description = "You must specify the group parameters",
            required = true)
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = GroupDto.class))})})
    @PostMapping()
    public GroupDto save(@RequestBody GroupDto groupDto) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        Group saved = groupService.save(group);
        GroupDto savedDto = groupMapper.groupToGroupDto(saved);
        Integer id = saved.getId();
        return savedDto;
    }

    @Operation(summary = "Get a group by id",
            description = "This method allows you to get a school group by its id")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = GroupDto.class))})})
    @GetMapping("/{id}")
    public GroupDto findById(@PathVariable("id") Integer id) {
        Group group = groupService.findById(id);
        return groupMapper.groupToGroupDto(group);
    }

    @Operation(summary = "Delete a group",
            description = "This method removes the group with the specified id")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation")})
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        groupService.deleteById(id);
    }

    @Operation(summary = "Delete all groups",
            description = "This resource represents an all groups in the system")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation")})
    @DeleteMapping()
    public void deleteAll() {
        groupService.deleteAll();
    }

    @Operation(summary = "Edit a group",
            description = "This method updates the group with the specified id. The values of the group properties are replaced with the values from the request body. Unspecified values are replaced with default values")
    @RequestBody(
            description = "You must specify the group parameters",
            required = true)
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = GroupDto.class))})})
    @PutMapping("/{id}")
    public GroupDto replace(@RequestBody GroupDto groupDto, @PathVariable("id") Integer id) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        Group replaced = groupService.update(group, id);
        return groupMapper.groupToGroupDto(replaced);
    }

    @Operation(summary = "Edit a group",
            description = "This method updates the group with the specified id. The values of the group properties are replaced with the values from the request body. Unspecified values are not changed")
    @RequestBody(
            description = "You must specify the group parameters",
            required = true)
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = GroupDto.class))})})
    @PatchMapping("/{id}")
    public GroupDto update(@RequestBody GroupDto groupDto, @PathVariable("id") Integer id) {
        Group group = groupService.findById(id);
        groupMapper.updateGroupFromDto(groupDto, group);
        Group updated = groupService.update(group, id);
        return groupMapper.groupToGroupDto(updated);
    }

    @Operation(summary = "Get filtered groups",
            description = "This method returns a groups with less or equal student count")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = GroupDto.class)))})})
    @GetMapping("/filter")
    public List<GroupDto> findAllFilter(@RequestParam("numberStudents") Integer numberStudents) {
        List<Group> groups = schoolManager.findGroupsByNumberStudents(numberStudents);
        return groupMapper.groupsToGroupsDto(groups);
    }
}
