package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "school", name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer id;

    @Column(name = "group_name")
    private String name;
}