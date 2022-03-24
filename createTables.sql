drop table if exists school.groups;

drop table if exists school.students;

drop table if exists school.courses;

create table if not exists school.groups (
    group_id integer primary key,
    group_name varchar(255)
);

create table if not exists school.students (
    student_id integer primary key,
    group_id integer,
    first_name varchar(255),
    last_name varchar(255),
    foreign key (group_id) references school.groups(group_id)
);

create table if not exists school.courses (
    course_id integer,
    course_name varchar(255),
    course_description text
);