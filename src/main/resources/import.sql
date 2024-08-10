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

INSERT INTO users (id, created_date, modified_date, email, password_encoded, user_role)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6y5b8b', now(), now(), 'job_applicant@test.com', '$2a$10$5wUz/bDg6tPD2B.ziDx2MeKWFueKaiiFFwr6i.VyhfqUkxXWnna6m', 0);

INSERT INTO metadata_registry (id, created_date, modified_date, name, type)
VALUES ('e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6e', now(), now(), 'citation_count', 1);

INSERT INTO metadata_registry (id, created_date, modified_date, name, type)
VALUES ('c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a', now(), now(), 'name', 0);
