package ru.zhadaev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping()
    public String findAll(Model model) {
        model.addAttribute("courses", courseService.findAll());

        return "courses/index";
    }

    @PostMapping()
    public String save(@ModelAttribute("course") Course course) {
        Course saved = courseService.save(course);
        Integer id = saved.getId();
        return "redirect:/courses";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("course", courseService.findById(id));
        return "courses/show";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        courseService.deleteById(id);
        return "redirect:/courses";
    }

    @DeleteMapping()
    public String deleteAll() {
        courseService.deleteAll();

        return "redirect:/courses";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") Course course) {
        courseService.update(course);

        return "redirect:/courses";
    }
}
