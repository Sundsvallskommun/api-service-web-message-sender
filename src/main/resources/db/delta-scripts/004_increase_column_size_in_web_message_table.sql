alter table web_message modify column message longtext;

-- Necessary line in order to document the change.
insert into schema_history (schema_version,comment,applied) VALUES ('004','Increased message column size in table web_message', NOW());