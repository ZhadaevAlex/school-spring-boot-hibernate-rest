package ru.zhadaev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.DAOException;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentDAO studentDAO;

    @Autowired
    public StudentController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping()
    public String findAll(Model model) throws DAOException {
        model.addAttribute("students", studentDAO.findAll());
        return "students/index";
    }

    @GetMapping("/new")
    public String newStudent(@ModelAttribute("student") Student student,
                             @ModelAttribute("group") Student group,
                             @ModelAttribute("course") Student course) {
        return "students/new";
    }

    @PostMapping()
    public String save(@ModelAttribute("student") Student student) throws DAOException {
        studentDAO.save(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) throws DAOException {
        model.addAttribute("student", studentDAO.findById(id).get());
        return "/students/show";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Integer id) throws DAOException {
        studentDAO.deleteById(id);
        return "redirect:/students";
    }

    @DeleteMapping()
    public String deleteAll() throws DAOException {
        studentDAO.deleteAll();
        return "redirect:/students";
    }
}
