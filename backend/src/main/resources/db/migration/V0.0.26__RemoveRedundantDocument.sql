alter table typographies rename column link to file_name;
alter table typographies add constraint fk_typographies_files foreign key (file_name) references files(name);

drop table typography_documents;

create table typography_documents (
    typography_type text,
    document_type text,

    primary key (typography_type, document_type),
    constraint fk_typography_documents_typography_types foreign key (typography_type) references typography_types(name),
    constraint fk_typography_documents_document_types foreign key (document_type) references document_types(name)
);

alter table documents drop column expiration_date;
alter table documents add column expiration_date date;

alter table documents add column creation_date date;
alter table documents add column typography_id uuid;
alter table documents add constraint fk_documents_typographies foreign key (typography_id) references typographies(id);