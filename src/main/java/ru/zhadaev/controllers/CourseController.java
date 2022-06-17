package ru.zhadaev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.exception.DAOException;

import java.sql.SQLException;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseDAO courseDAO;

    @Autowired
    public CourseController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @GetMapping()
    public String findAll(Model model) throws DAOException {
        model.addAttribute("courses", courseDAO.findAll());
        return "courses/index";
    }

    @GetMapping("/new")
    public String newCourse(@ModelAttribute("course") Course course) {
        return "courses/new";
    }

    @PostMapping()
    public String save(@ModelAttribute("course") Course course) throws DAOException {
        courseDAO.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) throws DAOException {
        model.addAttribute("course", courseDAO.findById(id).get());
        return "courses/show";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Integer id) throws DAOException {
        courseDAO.deleteById(id);
        return "redirect:/courses";
    }

    @DeleteMapping()
    public String deleteAll() throws DAOException {
        courseDAO.deleteAll();
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) throws DAOException {
        model.addAttribute("course", courseDAO.findById(id).get());
        return "courses/edit";
    }

    @PatchMapping("{/id}")
    public String update(@ModelAttribute("group") Course course, @PathVariable("id") int id) throws SQLException {
        courseDAO.update(course);
        return "redirect:/courses";
    }
}
