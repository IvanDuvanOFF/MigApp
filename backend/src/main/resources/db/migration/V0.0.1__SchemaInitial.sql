create table roles (
    name text primary key
);

create table users (
    id uuid primary key,
    username text unique not null,
    password text not null,
    is_active bool not null default false,
    role text not null,

    constraint fk_users_roles foreign key (role) references roles(name)
);

create table verification_tokens (
    token uuid primary key,
    user_id uuid not null,
    expiration_date timestamp without time zone,

    constraint fk_verification_tokens_users foreign key (user_id) references users(id)
);

create table admins (
    user_id uuid primary key,
    name text not null,
    surname text not null,

    constraint fk_admins_users foreign key (user_id) references users(id)
);

create table student_statuses (
    name text primary key
);

create table countries (
    name text primary key
);

create table students (
    user_id uuid primary key,
    name text not null,
    surname text not null,
    patronymic text,
    email text unique not null,
    phone text unique not null,
    country text not null,
    birthday date not null,
    status text not null,

    constraint fk_students_users foreign key (user_id) references users(id),
    constraint fk_students_statuses foreign key (status) references student_statuses(name),
    constraint fk_students_countries foreign key (country) references countries(name)
);

create table typography_types (
    name text primary key
);

create table typography_status (
    name text primary key
);

create table typographies (
    id uuid primary key,
    typography_type text not null,
    creation_date date not null,
    link text,
    status text not null,
    student_id uuid not null,

    constraint fk_typographies_typo_types foreign key (typography_type) references typography_types (name),
    constraint fk_typographies_typo_statuses foreign key (status) references typography_status (name),
    constraint fk_typographies_students foreign key (student_id) references students(user_id)
);

create table document_types (
    name text primary key
);

create table documents (
    id uuid primary key,
    document_type text not null,
    link text,
    expiration_date date not null,

    constraint fk_documents_document_types foreign key (document_type) references document_types (name)
);

create table typography_documents (
    typography_id uuid,
    document_id uuid,

    primary key (typography_id, document_id),
    constraint fk_typography_documents_typographies foreign key (typography_id) references typographies(id),
    constraint fk_typography_documents_documents foreign key (document_id) references documents(id)
);

create table deadlines (
    typography_type text,
    country text,
    days integer,

    primary key (typography_type, country),
    constraint fk_deadlines_typography_types foreign key (typography_type) references typography_types(name),
    constraint fk_deadlines_countries foreign key (country) references countries (name)
);