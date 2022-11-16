package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.api.dto.GroupDto;
import ru.zhadaev.api.mappers.GroupMapper;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;
    private final GroupMapper groupMapper;

    public GroupDto save(GroupDto groupDto) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        Group saved = groupDAO.save(group);
        UUID id = saved.getId();
        return groupMapper.groupToGroupDto(saved);
    }

    public GroupDto replace(GroupDto groupDto, UUID id) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        group.setId(id);
        Group replaced = groupDAO.update(group);
        return groupMapper.groupToGroupDto(replaced);
    }

    public GroupDto update(GroupDto groupDto, UUID id) {
        Group group = groupDAO.findById(id).
                orElseThrow(() -> new NotFoundException("Group not found"));
        groupMapper.updateGroupFromDto(groupDto, group);
//        Group updated = groupDAO.update(group);
        return groupMapper.groupToGroupDto(group);
    }

    public GroupDto findById(UUID id) {
        Group group = groupDAO.findById(id).
                orElseThrow(() -> new NotFoundException("Group not found"));
        return groupMapper.groupToGroupDto(group);
    }

    public List<GroupDto> find(GroupDto groupDto) {
        Group group = groupMapper.groupDtoToGroup(groupDto);
        List<Group> groupsDb = groupDAO.findLike(group);
        if (groupsDb.isEmpty()) {
            throw new NotFoundException("Groups not found");
        }
        return groupMapper.groupsToGroupsDto(groupsDb);
    }

    public List<GroupDto> findAll(Integer numberStudents) {
        List<Group> groups = (numberStudents == null) ?
                groupDAO.findAll()
                : findGroupsByNumberStudents(numberStudents);
        return groupMapper.groupsToGroupsDto(groups);
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
            logger.error("Group delete error. Group not found by id");
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
