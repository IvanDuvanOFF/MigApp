create table workspace (
    id uuid primary key ,
    name text not null default 'Untitled'
);

create table admin_user (
    id uuid primary key ,
    username text not null unique ,
    password text not null,
    is_active bool
);

create table workspace_user (
    workspace_id uuid references workspace(id),
    user_id uuid references admin_user(id),
    is_root bool,
    primary key (workspace_id, user_id)
);

create table bd_type (
    name text primary key ,
    driver_name text
);

create table config (
    id uuid primary key ,
    workspace_id uuid references workspace(id),
    bd_url text,
    bd_type text references bd_type(name)
);

create table property_type (
    name text primary key
);

create table property (
    id uuid primary key ,
    type text references property_type(name),
    config_id uuid references config(id),
    value text
);

create table user_table (
    id uuid primary key ,
    config_id uuid references config(id),
    name text
);

create table attribute_type (
    name text primary key,
    description text,
    bd_type text not null references bd_type(name)
);

create table attribute (
    table_id uuid references user_table(id),
    name text,
    type text references attribute_type(name),
    length int default null,
    is_primary_key bool not null default false,
    is_unique bool not null default false,
    is_nullable bool not null default true,
    reference_table text,
    reference_column text,
    check_constraint text
);