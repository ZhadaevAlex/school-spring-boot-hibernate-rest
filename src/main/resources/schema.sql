create schema if not exists school;

drop table if exists school.students_courses;

drop table if exists school.students;

drop table if exists school.groups;

drop table if exists school.courses;

create table if not exists school.groups
(
    group_id   serial primary key,
    group_name varchar(255) null
);

create table if not exists school.students
(
    student_id serial primary key,
    group_id   integer null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    foreign key (group_id) references school.groups (group_id) on delete set null
);

create table if not exists school.courses
(
    course_id          serial primary key,
    course_name        varchar(255) null,
    course_description text null
);

create table if not exists school.students_courses
(
    student_id integer null,
    course_id  integer null,
    foreign key (student_id) references school.students (student_id) on delete set null,
    foreign key (course_id) references school.courses (course_id) on delete set null,
    unique (student_id, course_id)
);

