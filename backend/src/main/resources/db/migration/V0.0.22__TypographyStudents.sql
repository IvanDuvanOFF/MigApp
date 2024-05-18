alter table typographies drop column student_id;

alter table typographies add column user_id uuid;

alter table typographies add constraint fk_typographies_users foreign key (user_id) references users(id);

create table files (
    name text primary key ,
    link text ,
    user_id uuid,

    constraint fk_files_users foreign key (user_id) references users(id)
);

alter table documents drop column link;
alter table documents add column file_name text;
alter table documents add constraint fk_documents_files foreign key (file_name) references files(name);