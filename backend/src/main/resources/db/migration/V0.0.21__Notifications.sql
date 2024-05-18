create table firebase_tokens
(
    token   text primary key,
    user_id uuid not null,

    constraint fk_firebase_tokens_users foreign key (user_id) references users (id)
);

create table notifications
(
    id                uuid primary key,
    recipient_token   text      not null,
    title             text      not null,
    description       text,
    notification_date timestamp not null,
    is_viewed         bool      not null,
    status            text,

    constraint fk_notifications_firebase_tokens foreign key (recipient_token) references firebase_tokens (token)
);