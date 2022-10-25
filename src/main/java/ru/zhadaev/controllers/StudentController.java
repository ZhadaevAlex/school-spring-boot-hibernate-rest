package ru.zhadaev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.model.WrapperCoursesId;
import ru.zhadaev.service.CourseService;
import ru.zhadaev.service.GroupService;
import ru.zhadaev.service.SchoolManager;
import ru.zhadaev.service.StudentService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final GroupService groupService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final SchoolManager schoolManager;

    @GetMapping()
    public String findAll(@ModelAttribute WrapperCoursesId wrapperCoursesId,
                          Model model) {
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("students", studentService.findAll());
        return "students/index";
    }

    @PostMapping()
    public String save(@ModelAttribute("student") Student student) {
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "/students/show";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        studentService.deleteById(id);
        return "redirect:/students";
    }

    @DeleteMapping()
    public String deleteAll() {
        studentService.deleteAll();
        return "redirect:/students";
    }

    @PatchMapping("/{id}")
    public String update(@RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName,
                         @RequestParam("group.id") String groupId,
                         @PathVariable("id") Integer id) {
        Map<String, String> dataUpdated = new HashMap<>();
        dataUpdated.put("firstName", firstName);
        dataUpdated.put("lastName", lastName);
        dataUpdated.put("groupId", groupId);
        studentService.update(dataUpdated, id);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}/courses")
    public String removeFromCourses(@ModelAttribute("wrapperCoursesId") WrapperCoursesId wrapperCoursesId,
                                    @PathVariable("id") int id) {

        studentService.removeFromCourses(id, wrapperCoursesId.getCoursesId());
        return "redirect:/students";
    }

    @PatchMapping("/{id}/courses")
    public String signOnCourses(@ModelAttribute("wrapperCoursesId") WrapperCoursesId wrapperCoursesId,
                                @PathVariable("id") int id) {

        studentService.signOnCourses(id, wrapperCoursesId.getCoursesId());
        return "redirect:/students";
    }

    @GetMapping("/filter")
    public String findAllFilter(@ModelAttribute("wrapperCoursesId") WrapperCoursesId wrapperCoursesId,
                                Model model) {

        model.addAttribute("students", schoolManager.findStudentsByCoursesName(wrapperCoursesId.getCoursesId()));
        return "students/index";
    }
}
