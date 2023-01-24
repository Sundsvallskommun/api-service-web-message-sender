
    create table attachment (
       id bigint not null auto_increment,
        file longblob,
        file_name varchar(255),
        mime_type varchar(255),
        parent_id varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table external_reference (
       id bigint not null auto_increment,
        ref_key varchar(255),
        ref_value varchar(255),
        parent_id varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table schema_history (
       schema_version varchar(255) not null,
        applied datetime(6) not null,
        comment varchar(8192) not null,
        primary key (schema_version)
    ) engine=InnoDB;

    create table web_message (
       id varchar(255) not null,
        created datetime(6),
        message varchar(255),
        oep_message_id integer,
        party_id varchar(255),
        primary key (id)
    ) engine=InnoDB;
create index external_reference_ref_key_index on external_reference (ref_key);
create index external_reference_ref_value_index on external_reference (ref_value);

    alter table external_reference 
       add constraint unique_external_reference unique (ref_key, ref_value, parent_id);
create index web_message_party_id_index on web_message (party_id);

    alter table attachment
       add constraint fk_attachment_parent_id_web_message_id
       foreign key (parent_id)
       references web_message (id);

    alter table external_reference
       add constraint fk_external_reference_parent_id_web_message_id
       foreign key (parent_id)
       references web_message (id);
