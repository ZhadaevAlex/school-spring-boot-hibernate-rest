package ru.zhadaev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.exception.DAOException;

import java.sql.SQLException;

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
    public String save(@ModelAttribute("group") Group group) throws DAOException {
        groupDAO.save(group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) throws DAOException {
        model.addAttribute("group", groupDAO.findById(id).get());
        return "/groups/show";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Integer id) throws DAOException {
        groupDAO.deleteById(id);
        return "redirect:/groups";
    }

    @DeleteMapping()
    public String deleteAll() throws DAOException {
        groupDAO.deleteAll();
        return "redirect:/groups";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) throws DAOException {
        model.addAttribute("group", groupDAO.findById(id).get());
        return "groups/edit";
    }

    @PutMapping("{/id}")
    public String update(@ModelAttribute("group") Group group, @PathVariable("id") int id) throws SQLException {
        group.setId(id);
        groupDAO.update(group);
        return "redirect:/groups";
    }
}
