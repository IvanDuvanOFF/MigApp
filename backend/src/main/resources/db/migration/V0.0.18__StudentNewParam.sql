alter table users
    add column sex varchar(6);

insert into countries (name) values ('India');
insert into countries (name) values ('Kazakhstan');
insert into countries (name) values ('Serbia');

insert into users(id, username, password, is_active, role, email, phone, tfa_enabled, sex, name, surname, patronymic,
                  birthday, country)
values (gen_random_uuid(),
        'student1',
        '$2y$10$XpFTlpGtB6KfcKdsG5KM/uiOjvfL6MZj/ZydDBjGqNlj1GKlmWYv2',
        true,
        'ROLE_USER',
        'student1@mail.com',
        '8(111)111-11-11',
        false,
        'MALE',
        'Name1',
        'Surname1',
        'Patronymic1',
        to_date('2002-02-01', 'yyyy-mm-dd'),
        'India'
);

insert into users(id, username, password, is_active, role, email, phone, tfa_enabled, sex, name, surname, patronymic,
                  birthday, country)
values (gen_random_uuid(),
        'student2',
        '$2y$10$BIsqdKUwrgz9IM1uxjd97u/Fc.Pj8JOl7Q0CFNSUnyq16wXXiNxZC',
        false,
        'ROLE_USER',
        'student2@mail.com',
        '8(222)111-11-11',
        false,
        'FEMALE',
        'Name2',
        'Surname2',
        'Patronymic2',
        to_date('2002-02-01', 'yyyy-mm-dd'),
        'Serbia'
       );

insert into users(id, username, password, is_active, role, email, phone, tfa_enabled, sex, name, surname, patronymic,
                  birthday, country)
values (gen_random_uuid(),
        'student3',
        '$2y$10$lHhV3GBOxRAJjAarEHZw9u.QsWuUP9QFpvKRwImG7ENdwoTuIZOw6',
        false,
        'ROLE_USER',
        'student3@mail.com',
        '8(333)111-11-11',
        false,
        'MALE',
        'Name3',
        'Surname3',
        'Patronymic3',
        to_date('2002-02-01', 'yyyy-mm-dd'),
        'Kazakhstan'
       );

insert into users(id, username, password, is_active, role, email, phone, tfa_enabled, sex, name, surname, patronymic,
                  birthday, country)
values (gen_random_uuid(),
        'student4',
        '$2y$10$GsIq9uEjAn.ljqBhlbMUO.KreidqAJm8DGNTWEBsOMrrqNmgV6Bj6',
        true,
        'ROLE_USER',
        'student4@mail.com',
        '8(444)111-11-11',
        false,
        'MALE',
        'Name4',
        'Surname4',
        'Patronymic4',
        to_date('2002-02-01', 'yyyy-mm-dd'),
        'India'
       );

insert into users(id, username, password, is_active, role, email, phone, tfa_enabled, sex, name, surname, patronymic,
                  birthday, country)
values (gen_random_uuid(),
        'student5',
        '$2y$10$VYdehzUM24qvU5g6Ix6TaOF4noidpoLjFeYmwFARnp4NJfSZxywJK',
        true,
        'ROLE_USER',
        'student5@mail.com',
        '8(555)111-11-11',
        false,
        'MALE',
        'Name5',
        'Surname5',
        'Patronymic5',
        to_date('2002-02-01', 'yyyy-mm-dd'),
        'Serbia'
       );