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

create table if not exists school.courses1 (
    course_id integer,
    course_name varchar(255),
    course_description text
);

create table if not exists school.courses2 (
    course_id integer,
    course_name varchar(255),
    course_description text
);

create table if not exists school.courses3 (
                                               course_id integer,
                                               course_name varchar(255),
                                               course_description text
);

create table if not exists school.courses4 (
                                               course_id integer,
                                               course_name varchar(255),
                                               course_description text
);


-- drop table school.students;
-- drop table school.groups;
-- drop table school.courses;