package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.api.dto.GroupDto;
import ru.zhadaev.api.errors.NotFoundException;
import ru.zhadaev.api.mappers.GroupMapper;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.dao.repository.impl.StudentDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GroupService {
    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;
    private final GroupMapper mapper;

    public GroupDto save(GroupDto groupDto) {
        Group group = mapper.toEntity(groupDto);
        Group saved = groupDAO.save(group);
        UUID id = saved.getId();
        return mapper.toDto(saved);
    }

    public GroupDto replace(GroupDto groupDto, UUID id) {
        if (!existsById(id)) throw new NotFoundException("Group replace error. Group not found by id");
        Group group = mapper.toEntity(groupDto);
        group.setId(id);
        Group replaced = groupDAO.update(group);
        return mapper.toDto(replaced);
    }

    public GroupDto update(GroupDto groupDto, UUID id) {
        Group group = groupDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Group update error. Group not found by id"));
        mapper.update(groupDto, group);
        return mapper.toDto(group);
    }

    public GroupDto findById(UUID id) {
        Group group = groupDAO.findById(id).
                orElseThrow(() -> new NotFoundException("Group not found by id"));
        return mapper.toDto(group);
    }

    public List<GroupDto> find(GroupDto groupDto) {
        Group group = mapper.toEntity(groupDto);
        List<Group> groups = groupDAO.findLike(group);
        if (groups.isEmpty()) {
            throw new NotFoundException("Groups not found");
        }
        return mapper.toDto(groups);
    }

    public List<GroupDto> findAll(Integer numberStudents) {
        List<Group> groups = (numberStudents == null) ?
                groupDAO.findAll()
                : findGroupsByNumberStudents(numberStudents);
        return mapper.toDto(groups);
    }

    public List<Group> findAll() {
        return groupDAO.findAll();
    }

    public boolean existsById(UUID id) {
        return groupDAO.existsById(id);
    }

    public long count() {
        return groupDAO.count();
    }

    public void deleteById(UUID id) {
        if (groupDAO.existsById(id)) {
            groupDAO.deleteById(id);
        } else {
            throw new NotFoundException("Group delete error. Group not found by id");
        }
    }

    public void delete(Group group) {
        groupDAO.delete(group);
    }

    public void deleteAll() {
        groupDAO.deleteAll();
    }

    public List<Group> findGroupsByNumberStudents(long numberStudents) {
        List<Student> studentsDb = studentDAO.findAll();
        List<Group> groupsDb = groupDAO.findAll();
        List<Group> result = new ArrayList<>();

        for (Group group : groupsDb) {
            long numberStudentsInGroup = studentsDb.stream()
                    .filter(p -> p.getGroup() != null && p.getGroup().getId().equals(group.getId()))
                    .count();

            if (numberStudentsInGroup <= numberStudents) {
                result.add(group);
            }
        }

        return result;
    }
}
