create table revoked_tokens
(
    token           text primary key,
    user_id         uuid      not null,
    expiration_date timestamp not null,

    constraint fk_revoked_tokens_users foreign key (user_id) references users (id)
);

CREATE FUNCTION delete_expired_rows()
    RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM revoked_tokens WHERE expiration_date < CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_expired_after_insert
    AFTER INSERT OR UPDATE ON revoked_tokens
    FOR EACH ROW EXECUTE PROCEDURE delete_expired_rows();