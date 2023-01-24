-- Creation of table
    create table attachment (
           id bigint not null auto_increment,
            file longblob,
            file_type varchar(255),
            mime_type varchar(255),
            parent_id varchar(255) not null,
            primary key (id)
        ) engine=InnoDB;

        alter table attachment
           add constraint fk_attachment_parent_id_web_message_id
           foreign key (parent_id)
           references web_message (id);

-- Necessary line in order to document the change.
insert into schema_history (schema_version,comment,applied) VALUES ('002','Created attachment table', NOW());