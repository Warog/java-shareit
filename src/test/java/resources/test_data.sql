ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ITEMS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE BOOKING ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE COMMENTS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE REQUESTS ALTER COLUMN ID RESTART WITH 1;
DELETE FROM COMMENTS;
DELETE FROM USERS;
DELETE FROM ITEMS;
DELETE FROM BOOKING;
DELETE FROM REQUESTS;
-- DELETE FROM REQUEST_ITEM;


INSERT INTO USERS (EMAIL, NAME)
VALUES ('BUGAGA@mail.ru', 'Olen'),
       ('GUGAGA@mail.ru', 'Inok'),
       ('DUDADA@gmail.com', 'fanok'),
       ('PUPAPA@email.com', 'panok');

INSERT INTO ITEMS (name, description, available, owner)
VALUES ('Drel', 'drelit', true, 1),
       ('Shurupowert', 'shurupit', true, 3),
       ('Hammer', 'beyt', true, 4);

INSERT INTO BOOKING (START_DATE, END_DATE, ITEM_ID, BOOKER_ID, STATUS)
VALUES (TIMESTAMP '2021-01-01 07:15:31.123456789', TIMESTAMP '2021-01-07 07:15:31.123456789', 1, 2, 'APPROVED'),
       (TIMESTAMP '2022-02-10 07:15:31.123456789', TIMESTAMP '2022-02-15 07:15:31.123456789', 2, 4, 'WAITING'),
       (TIMESTAMP '2020-03-20 07:15:31.123456789', TIMESTAMP '2020-03-25 07:15:31.123456789', 3, 3, 'REJECTED'),
       (TIMESTAMP '2020-04-20 07:15:31.123456789', TIMESTAMP '2020-04-25 07:15:31.123456789', 2, 4, 'REJECTED'),
       (TIMESTAMP '2020-04-26 07:15:31.123456789', TIMESTAMP '2020-04-30 07:15:31.123456789', 2, 1, 'REJECTED');

INSERT INTO REQUESTS (description, requestor, created)
VALUES ('Nuzhen Shurik', 2, TIMESTAMP '2021-05-13 07:15:31.123456789'),
       ('Treb drel', 3, TIMESTAMP '2022-05-13 07:15:31.123456789'),
       ('Neodhodima benzopila', 2, TIMESTAMP '2020-05-13 07:15:31.123456789');

INSERT INTO COMMENTS (TEXT, ITEM_ID, AUTHOR_ID, ADD_DATE)
VALUES ('good shurik!', 2, 1, TIMESTAMP '2021-05-13 07:15:31.123456789'),
       ('nice drel!', 3, 2, TIMESTAMP '2022-05-13 07:15:31.123456789'),
       ('don t user!!!', 1, 3, TIMESTAMP '2020-05-13 07:15:31.123456789');
