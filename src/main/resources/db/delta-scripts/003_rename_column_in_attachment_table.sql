-- Renaming column that contains file name from file_type to file_name

	alter table attachment
		change column file_type
                   file_name
                   varchar(255);

-- Necessary line in order to document the change.
insert into schema_history (schema_version,comment,applied) VALUES ('003','Renamed attachment column file_type to file_name', NOW());