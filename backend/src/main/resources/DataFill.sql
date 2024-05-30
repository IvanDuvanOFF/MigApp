insert into users(id, username, password, is_active, role, name, surname, patronymic, email, phone, country, birthday, status, tfa_enabled, sex, photo, institute, group_name)
values ('f016c726-1ca2-4747-a734-f7f4ed58594c', 'stud0', '$2y$10$Yn.koBsa6CR4wZBHdYhlGuI1DThI7Ua8wT69STybf4nMDHVbXj3jO', true, 'ROLE_USER', 'Имя', 'Фамилия', 'Отчество', 'stud1@mail.ru', '8(888)888-88-88', 'Serbia', to_date('2002-01-01', 'yyyy-mm-dd'), 'OK', false, 'MALE', null, 'IC', 'PI-252');
insert into users(id, username, password, is_active, role, name, surname, patronymic, email, phone, country, birthday, status, tfa_enabled, sex, photo, institute, group_name)
values ('b3055f2a-e684-4e03-9742-4067ab408b2f', 'stud1', '$2y$10$zbBIqNfuxU4yaNRMdClW2usoEyTEduGCjjHo62IEZ125aLXo9Kvs2', true, 'ROLE_USER', 'Ями', 'Яилимаф', 'Овтсучто', 'stud2@mail.ru', '8(777)888-88-88', 'Serbia', to_date('2002-01-02', 'yyyy-mm-dd'), 'OK', false, 'FEMALE', null, 'IC', 'PI-252');
insert into users(id, username, password, is_active, role, name, surname, patronymic, email, phone, country, birthday, status, tfa_enabled, sex, photo, institute, group_name)
values ('578f6ff8-38b9-4ff5-b852-c264869ea3f7', 'stud2', '$2y$10$rZllOrYU8wWyTqR7imxTkuN2uTC/ShjettURW/E5b06ca8h0f2VDq', true, 'ROLE_USER', 'Имя', 'Фамилия', null, 'stud3@mail.ru', '8(666)888-88-88', 'Kazakhstan', to_date('2002-03-01', 'yyyy-mm-dd'), 'OK', false, 'MALE', null, 'IC', 'PI-242');

insert into document_types(name) values ('Паспорт');
insert into document_types(name) values ('Миграционная карта');
insert into document_types(name) values ('Договор на проживание в общежитии');
insert into document_types(name) values ('Свидетельство о рождении');
insert into document_types(name) values ('Доверенность от законных представителей');
insert into document_types(name) values ('Виза');
insert into document_types(name) values ('Фото');
insert into document_types(name) values ('Квитанция оплаты пошлины');
insert into document_types(name) values ('Дактилоскопия');
insert into document_types(name) values ('Сертификат на ВИЧ');

insert into typography_types(name) values ('Миграционный учет');
insert into typography_types(name) values ('Виза');

insert into deadlines(typography_type, country, days) VALUES ('Миграционный учет', 'Serbia', 30);
insert into deadlines(typography_type, country, days) VALUES ('Миграционный учет', 'Kazakhstan', 30);
insert into deadlines(typography_type, country, days) VALUES ('Миграционный учет', 'India', 60);

insert into deadlines(typography_type, country, days) VALUES ('Виза', 'Serbia', 21);
insert into deadlines(typography_type, country, days) VALUES ('Виза', 'India', 30);

insert into typography_statuses(name) values ('DONE');
insert into typography_statuses(name) values ('LOADING');
insert into typography_statuses(name) values ('FAILED');
insert into typography_statuses(name) values ('IN_PROGRESS');

insert into typography_documents(typography_type, document_type) VALUES ('Виза', 'Квитанция оплаты пошлины');
insert into typography_documents(typography_type, document_type) VALUES ('Виза', 'Паспорт');
insert into typography_documents(typography_type, document_type) VALUES ('Виза', 'Миграционная карта');
insert into typography_documents(typography_type, document_type) VALUES ('Виза', 'Фото');
insert into typography_documents(typography_type, document_type) VALUES ('Виза', 'Договор на проживание в общежитии');

insert into typography_documents(typography_type, document_type) VALUES ('Миграционный учет', 'Сертификат на ВИЧ');
insert into typography_documents(typography_type, document_type) VALUES ('Миграционный учет', 'Паспорт');
insert into typography_documents(typography_type, document_type) VALUES ('Миграционный учет', 'Миграционная карта');
insert into typography_documents(typography_type, document_type) VALUES ('Миграционный учет', 'Фото');
insert into typography_documents(typography_type, document_type) VALUES ('Миграционный учет', 'Дактилоскопия');

insert into typographies (id, typography_type, creation_date, file_name, status, user_id)
values ('2d159baa-809b-4073-9146-dd400ad29e5b', 'Виза', to_date('2024-05-08', 'yyyy-mm-dd'), null, 'IN_PROGRESS', 'f016c726-1ca2-4747-a734-f7f4ed58594c');
insert into typographies (id, typography_type, creation_date, file_name, status, user_id)
values ('2cdc5bd2-b738-4a13-8592-127dc7063c8f', 'Миграционный учет', to_date('2024-04-21', 'yyyy-mm-dd'), null, 'IN_PROGRESS', 'f016c726-1ca2-4747-a734-f7f4ed58594c');

insert into typographies (id, typography_type, creation_date, file_name, status, user_id)
values ('95e9e8a4-52a8-4fdb-b91d-0cf67ffd7f4f', 'Виза', to_date('2024-04-03', 'yyyy-mm-dd'), null, 'IN_PROGRESS', 'b3055f2a-e684-4e03-9742-4067ab408b2f');
insert into typographies (id, typography_type, creation_date, file_name, status, user_id)
values ('eb7352c2-39b2-4278-8daa-05115def9eed', 'Миграционный учет', to_date('2024-04-12', 'yyyy-mm-dd'), null, 'IN_PROGRESS', 'b3055f2a-e684-4e03-9742-4067ab408b2f');

insert into typographies (id, typography_type, creation_date, file_name, status, user_id)
values ('31945a8a-5421-4a68-8074-683c68ca73bf', 'Миграционный учет', to_date('2024-05-19', 'yyyy-mm-dd'), null, 'IN_PROGRESS', '578f6ff8-38b9-4ff5-b852-c264869ea3f7');