alter table if exists web_message
    add column if not exists oep_instance varchar(255) default null;