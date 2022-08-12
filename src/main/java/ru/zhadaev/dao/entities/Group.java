package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "school", name = "groups")
@Data
public class Group {
    @Id
    @Column(name = "group_id")
    private Integer id;

    @Column(name = "group_name")
    private String name;
}
