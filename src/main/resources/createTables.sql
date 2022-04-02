drop schema if exists school;

create schema school;

drop table if exists school.students_courses;

drop table if exists school.students;

drop table if exists school.groups;

drop table if exists school.courses;

create table if not exists school.groups
(
    group_id   serial primary key,
    group_name varchar(255)
);

create table if not exists school.students
(
    student_id serial primary key,
    group_id   integer,
    first_name varchar(255),
    last_name  varchar(255),
    foreign key (group_id) references school.groups (group_id) on delete set null
);

create table if not exists school.courses
(
    course_id          serial,
    course_name        varchar(255),
    course_description text
);

create table if not exists school.students_courses
(
    id         serial primary key,
    student_id integer,
    course_id  integer,
    foreign key (student_id) references school.students (student_id),
    foreign key (student_id) references school.students (student_id),
    unique (student_id, course_id)
);