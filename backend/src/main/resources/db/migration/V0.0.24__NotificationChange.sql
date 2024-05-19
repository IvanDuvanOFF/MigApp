alter table notifications add column user_id uuid;
alter table notifications add constraint fk_notifications_users foreign key (user_id) references users(id);