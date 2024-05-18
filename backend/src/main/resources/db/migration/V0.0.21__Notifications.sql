create table notifications (
    id uuid primary key,
    user_id uuid not null,
    title text not null,
    description text,
    notification_date timestamp not null,
    is_viewed bool not null,
    status text,

    constraint fk_notifications_users foreign key (user_id) references users(id)
);