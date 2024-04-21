create table two_fa_codes (
    code varchar(6) primary key ,
    user_id uuid not null,
    expiration_date timestamp not null,

    constraint fk_two_fa_codes_users foreign key (user_id) references users(id)
);

alter table users add column is_2fa_enabled bool not null default false;