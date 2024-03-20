alter table tfa_codes alter column code type text;
alter table users drop column is_2fa_enabled;