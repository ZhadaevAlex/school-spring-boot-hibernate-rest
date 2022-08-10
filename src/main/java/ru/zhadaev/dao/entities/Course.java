package ru.zhadaev.dao.entities;

import lombok.Data;

@Data
public class Course extends School {
    private Integer id;
    private String name;
    private String description;
}
