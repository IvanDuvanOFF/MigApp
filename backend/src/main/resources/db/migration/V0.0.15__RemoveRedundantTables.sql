drop table two_fa_codes;

alter table users rename column is_2fa_enabled to tfa_enabled