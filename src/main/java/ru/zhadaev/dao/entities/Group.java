package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "school", name = "groups")
public class Group {
    @Id
    @SequenceGenerator(
            schema = "school",
            name = "group_seq",
            sequenceName = "groups_group_id_sequence",
            initialValue = 11,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq")
    @Column(name = "group_id")
    private Integer id;

    @Column(name = "group_name")
    private String name;
}