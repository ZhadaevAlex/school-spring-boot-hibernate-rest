package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.exception.NotFoundException;
import ru.zhadaev.exception.NotValidGroupException;
import ru.zhadaev.exception.NotValidStudentException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupDAO groupDAO;

    public Group save(Group group) {
        requiredNotNull(group);
        return groupDAO.save(group);
    }

    public Group update(Group group, Integer id) {
        requiredNotNull(group);
        requiredIdIsValid(id);
        group.setId(id);
        return groupDAO.update(group);
    }

    public Group findById(Integer id) {
        requiredIdIsValid(id);
        return groupDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Group not found"));
    }

    public List<Group> find(Group group) {
        requiredNotNull(group);
        List<Group> groupsDb = groupDAO.findLike(group);
        if (groupsDb.isEmpty()) {
            throw new NotFoundException("Groups not found");
        }
        return groupsDb;
    }

    public List<Group> findAll() {
        return groupDAO.findAll();
    }

    public boolean existsById(Integer id) {
        requiredIdIsValid(id);
        return groupDAO.existsById(id);
    }

    public long count() {
        return groupDAO.count();
    }

    public void deleteById(Integer id) {
        requiredIdIsValid(id);
        if (groupDAO.existsById(id)) {
            groupDAO.deleteById(id);
        } else {
            logger.error("Group delete error. Group not found by id");
            throw new NotFoundException("Group delete error. Group not found by id");
        }
    }

    public void delete(Group group) {
        requiredNotNull(group);
        groupDAO.delete(group);
    }

    public void deleteAll() {
        groupDAO.deleteAll();
    }

    private void requiredNotNull(Group group) {
        if (group == null) {
            logger.error("Group required is not null!");
            throw new NotValidGroupException("Group required is not null!");
        }
    }

    private void requiredIdIsValid(Integer id) {
        if (id == null || id < 1) {
            logger.error("The id value must be non-null and greater than 0");
            throw new NotValidStudentException("The id value must be non-null and greater than 0");
        }
    }
}
