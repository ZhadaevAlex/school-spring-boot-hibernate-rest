package ru.zhadaev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SchoolManager {
    private final GroupService groupService;
    private final StudentService studentService;




}
