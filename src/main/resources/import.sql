INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role)
VALUES ('45358564-927d-447c-a832-c5c263ada7bc', now(), now(), 'admin@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 4);

INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role)
VALUES ('65358564-927d-447c-a832-c5c263ada7bd', now(), now(), 'hr_specialist@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 1);

INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a', now(), now(), 'editor@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 3);

INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a', now(), now(), 'researcher@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 2);

INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8b', now(), now(), 'sec_researcher@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 2);

INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role, current_state)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6y5b8b', now(), now(), 'job_applicant@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 0, 0);

INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role, current_state)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6y5b8k', now(), now(), 'job_applicant2@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 0, 0);

INSERT INTO metadata_registry (id, created_date, modified_date, name, type)
VALUES ('e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6e', now(), now(), 'citation_count', 1);

INSERT INTO metadata_registry (id, created_date, modified_date, name, type)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a', now(), now(), 'name', 0);

INSERT INTO metadata_registry (id, created_date, modified_date, name, type)
VALUES ('e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6f', now(), now(), 'education_degree', 2);

INSERT INTO metadata_registry (id, created_date, modified_date, name, type)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4g', now(), now(), 'research_field', 3);

INSERT INTO metadata_value (id, created_date, modified_date, value, metadata_registry_id, owner_id)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4x', now(), now(), 'Alper Gayretoglu', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a');

INSERT INTO metadata_value (id, created_date, modified_date, value, metadata_registry_id, owner_id)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4v', now(), now(), '3', 'e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6e', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a');

INSERT INTO metadata_value (id, created_date, modified_date, value, metadata_registry_id, owner_id)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4e', now(), now(), 'MASTER', 'e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6f', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8b');

INSERT INTO metadata_value (id, created_date, modified_date, value, metadata_registry_id, owner_id)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4q', now(), now(), 'COMPUTER_SCIENCE', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4g', 'c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8b');

INSERT INTO files (id, created_date, modified_date, location, name, size)
VALUES ('c9a2f3d2-7b8b-4b32-9901-ec223b6c5b4m', now(), now(), 'not_exist', 'not_exist_file', 0)
