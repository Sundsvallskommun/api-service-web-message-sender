
alter table if exists web_message
   add column municipality_id varchar(255) AFTER id;
   
create index web_message_municipality_id_index 
   on web_message (municipality_id);