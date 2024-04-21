drop table if exists students cascade ;
drop table if exists admins cascade ;

alter table typography_status rename to typography_statuses;

insert into countries (name) values ('Russia');
insert into countries (name) values ('NONE');

insert into student_statuses (name) values ('OK');
insert into student_statuses (name) values ('NONE');

alter table users add column name text not null default 'NONE';
alter table users add column surname text not null default 'NONE';
alter table users add column patronymic text;
alter table users add column email text unique not null default 'NONE';
alter table users add column phone text unique not null default 'NONE';
alter table users add column country text not null default 'NONE';
alter table users add column birthday date not null default date('1970-01-01');
alter table users add column status text not null default 'NONE';

alter table users add constraint fk_users_country foreign key (country) references countries(name);
alter table users add constraint fk_users_status foreign key (status) references student_statuses(name);