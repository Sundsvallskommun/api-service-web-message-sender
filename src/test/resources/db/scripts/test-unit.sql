-- Entity 1:
INSERT INTO web_message(id, municipality_id, created, party_id, oep_instance, sender_user_id)
VALUES('1e098e28-d9ba-459c-94c7-5508be826c08', '2281', '2021-11-21 10:05:48.198', 'fbfbd90c-4c47-11ec-81d3-0242ac130003', 'external', 'user001');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('1e098e28-d9ba-459c-94c7-5508be826c08', 'key1', 'value1');

-- Entity 2:
INSERT INTO web_message(id, municipality_id, created, party_id, oep_instance, sender_user_id)
VALUES('68cd9896-9918-4a80-bb41-e8fe0faf03f9', '2281', '2021-11-22 10:05:48.198', '262f3855-b985-4f3a-a4b8-7b9409ac9590', 'external', 'user002');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('68cd9896-9918-4a80-bb41-e8fe0faf03f9', 'common-key', 'common-value');

-- Entity 3:
INSERT INTO web_message(id, municipality_id, created, party_id, oep_instance, sender_user_id)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710526', '2281', '2021-11-23 10:05:48.198', 'b7bd0e55-0811-4d3a-91d9-6bab7fd9ce5e', 'external', 'user003');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710526', 'common-key', 'common-value');

-- Entity 4:
INSERT INTO web_message(id, municipality_id, created, party_id, oep_instance)
VALUES('e535c9f7-c473-44f2-81d5-a8fbfcc932ea', '2281', '2021-11-24 10:05:48.198', 'b28d3b8f-ebb4-49de-bdc4-2f3d7aa6a933', 'internal');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('e535c9f7-c473-44f2-81d5-a8fbfcc932ea', 'to-be-deleted', 'to-be-deleted');

INSERT INTO attachment(file, file_name, mime_type, parent_id)
VALUES(x'89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796BD0000000049454E44AE426082',
'test_image.jpg', 'image/jpeg', 'e535c9f7-c473-44f2-81d5-a8fbfcc932ea');

-- Entity 5:
INSERT INTO web_message(id, municipality_id, created, party_id, oep_instance)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710527', '2281', '2021-11-23 10:05:48.198', 'b7bd0e55-0811-4d3a-91d9-6bab7fd9ce5e', 'internal');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710527', 'common-key', 'common-value');

-- Entity 6:
INSERT INTO web_message(id, municipality_id, created, party_id, oep_instance)
VALUES('37317c16-3fd6-11ed-b878-0242ac120002', '2281', '2022-09-29 11:08:23.766', '38a536ae-79e1-440e-b0c1-34ad1acbe411', 'internal');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('37317c16-3fd6-11ed-b878-0242ac120002', 'common-key', 'unique-value');

INSERT INTO attachment(file, file_name, mime_type, parent_id)
VALUES(x'89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796BD0000000049454E44AE426082',
'test_image.jpg', 'image/jpeg', '37317c16-3fd6-11ed-b878-0242ac120002');
