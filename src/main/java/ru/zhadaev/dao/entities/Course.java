package ru.zhadaev.dao.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Course extends School {
    private Integer id;
    private String name;
    private String description;
}
