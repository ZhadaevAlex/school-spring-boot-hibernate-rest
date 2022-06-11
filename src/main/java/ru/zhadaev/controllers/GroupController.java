package ru.zhadaev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.exception.DAOException;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupDAO groupDAO;

    @Autowired
    public GroupController(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @GetMapping()
    public String findAll(Model model) throws DAOException {
        model.addAttribute("groups", groupDAO.findAll());
        return "groups/index";
    }

    @GetMapping("/new")
    public String newGroup(@ModelAttribute("group") Group group) {
        return "groups/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("group") Group group) throws DAOException {
        groupDAO.save(group);
        return "redirect:/groups";
    }
}
