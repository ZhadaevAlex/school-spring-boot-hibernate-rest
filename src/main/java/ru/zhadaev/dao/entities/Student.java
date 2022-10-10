package ru.zhadaev.dao.entities;

import lombok.Data;

import java.util.Set;

//@Entity
//@Table(name = "students", schema = "school")
@Data
public class Student {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "student_id")
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name = "group_id")
    private Group group;

//    @Column(name = "first_name")
    private String firstName;

//    @Column(name = "last_name")
    private String lastName;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "students_courses", schema = "school",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "course_id")
//    )
    private Set<Course> courses;
}
