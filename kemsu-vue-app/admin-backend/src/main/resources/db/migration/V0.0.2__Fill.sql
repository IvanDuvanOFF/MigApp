insert into bd_type values ('Postgres', 'org.postgresql.Driver');

insert into property_type values ('logo');

insert into attribute_type(name, description, bd_type) values ('integer', 'typical choice for integer', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('smallint', 'small-range integer', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('bigint', 'large-range integer', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('decimal', 'user-specified precision', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('numeric', 'user-specified precision', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('real', 'variable-precision, inexact', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('double precision', 'variable-precision, inexact', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('smallserial', 'small autoincrementing integer', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('serial', 'autoincrementing integer', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('bigserial', 'large autoincrementing integer', 'Postgres');

insert into attribute_type(name, description, bd_type) values ('money', 'currency amount', 'Postgres');

insert into attribute_type(name, description, bd_type) values ('varchar', 'variable-length with limit', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('char', 'fixed-length, blank-padded', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('bpchar', 'variable unlimited length, blank-trimmed', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('text', 'variable unlimited length', 'Postgres');

insert into attribute_type(name, description, bd_type) values ('uuid', 'uuid datatype', 'Postgres');

insert into attribute_type(name, description, bd_type) values ('bytea', '1 or 4 bytes plus the actual binary string', 'Postgres');

insert into attribute_type(name, description, bd_type) values ('timestamp', 'both date and time (no time zone)', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('timestamp with time zone', 'both date and time (no time zone)', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('date', 'date (no time of day)', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('time', 'time of day (no date)', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('time with time zone', 'time of day (no date), with time zone', 'Postgres');
insert into attribute_type(name, description, bd_type) values ('interval', 'time interval', 'Postgres');