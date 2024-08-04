CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO metadata_value (id, created_date, modified_date, owner_id, metadata_registry_id, value)
VALUES (uuid_generate_v4(), now(), now(), 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a', 'value');

INSERT INTO metadata_value (id, created_date, modified_date, owner_id, metadata_registry_id, value)
VALUES (uuid_generate_v4(), now(), now(), 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8b', 'e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6e', 2);

INSERT INTO metadata_value (id, created_date, modified_date, owner_id, metadata_registry_id, value)
VALUES (uuid_generate_v4(), now(), now(), 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a', 3);

INSERT INTO metadata_value (id, created_date, modified_date, owner_id, metadata_registry_id, value)
VALUES (uuid_generate_v4(), now(), now(), 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8b', 'e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6e', 'string');

