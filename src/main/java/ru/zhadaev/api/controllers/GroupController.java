package ru.zhadaev.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.api.dto.GroupDto;
import ru.zhadaev.api.validation.Marker;
import ru.zhadaev.service.GroupService;

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

    @GetMapping()
    public List<GroupDto> findAll(@RequestParam(name = "numberStudents", required = false) @Valid @Min(0) Integer numberStudents) {
        return groupService.findAll(numberStudents);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnPostPut.class)
    public GroupDto save(@RequestBody @Valid GroupDto groupDto) {
        return groupService.save(groupDto);
    }

    @GetMapping("/{id}")
    public GroupDto findById(@PathVariable("id") UUID id) {
        return groupService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") UUID id) {
        groupService.deleteById(id);
    }

    @DeleteMapping()
    public void deleteAll() {
        groupService.deleteAll();
    }

    @PutMapping("/{id}")
    @Validated(Marker.OnPostPut.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GroupDto replace(@RequestBody @Valid GroupDto groupDto, @PathVariable("id") UUID id) {
        return groupService.replace(groupDto, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GroupDto update(@RequestBody GroupDto groupDto, @PathVariable("id") UUID id) {
        return groupService.update(groupDto, id);
    }
}
