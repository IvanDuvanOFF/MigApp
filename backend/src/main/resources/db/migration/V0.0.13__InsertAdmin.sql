insert into users (id, username, password, is_active, role, email)
values (gen_random_uuid(),
        'ivanchick',
        '$2a$12$Tc3T8trXvDvYEnXQ4/OubOh5LGBUqAmktOctEyHCpvDwjDiz6WrZC',
        true,
        'ROLE_ADMIN', 'ivan.duvanov.3@gmail.com');