create role zhadaev with createdb login password '123';

create database school;

grant create, connect, temporary on database school to zhadaev;
		