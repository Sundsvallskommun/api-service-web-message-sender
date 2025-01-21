alter table if exists web_message
   add column if not exists sender_user_id varchar(255) AFTER party_id;
   