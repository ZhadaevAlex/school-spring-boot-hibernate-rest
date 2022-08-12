package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "school", name = "courses")
@Data
public class Course {
    @Id
    @Column(name = "course_id")
    private Integer id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_description")
    private String description;
}
