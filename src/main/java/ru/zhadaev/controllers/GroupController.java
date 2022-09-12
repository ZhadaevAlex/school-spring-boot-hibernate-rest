package ru.zhadaev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.service.GroupService;
import ru.zhadaev.service.SchoolManager;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final SchoolManager schoolManager;

    @GetMapping()
    public String findAll(Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "groups/index";
    }

    @PostMapping()
    public String save(@ModelAttribute("group") Group group) {
        Group saved = groupService.save(group);
        Integer id = saved.getId();
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("group", groupService.findById(id));
        return "/groups/show";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        groupService.deleteById(id);
        return "redirect:/groups";
    }

    @DeleteMapping()
    public String deleteAll() {
        groupService.deleteAll();
        return "redirect:/groups";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") Group group) {
        groupService.update(group);
        return "redirect:/groups";
    }

    @GetMapping("/filter")
    public String findAllFilter(@RequestParam("numberStudents") Integer numberStudents,
                                Model model) {
        model.addAttribute("groups", schoolManager.findGroupsByNumberStudents(numberStudents));
        return "groups/index";
    }
}
