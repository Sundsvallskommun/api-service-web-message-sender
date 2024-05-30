
    create table attachment (
        id bigint not null auto_increment,
        file_name varchar(255),
        mime_type varchar(255),
        parent_id varchar(255) not null,
        file longblob,
        primary key (id)
    ) engine=InnoDB;

    create table external_reference (
        id bigint not null auto_increment,
        parent_id varchar(255) not null,
        ref_key varchar(255),
        ref_value varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table web_message (
        oep_message_id integer,
        created datetime(6),
        id varchar(255) not null,
        oep_instance varchar(255),
        party_id varchar(255),
        message longtext,
        primary key (id)
    ) engine=InnoDB;

    create index external_reference_ref_key_index 
       on external_reference (ref_key);

    create index external_reference_ref_value_index 
       on external_reference (ref_value);

    alter table if exists external_reference 
       add constraint unique_external_reference unique (ref_key, ref_value, parent_id);

    create index web_message_party_id_index 
       on web_message (party_id);

    alter table if exists attachment 
       add constraint fk_attachment_parent_id_web_message_id 
       foreign key (parent_id) 
       references web_message (id);

    alter table if exists external_reference 
       add constraint fk_external_reference_parent_id_web_message_id 
       foreign key (parent_id) 
       references web_message (id);
