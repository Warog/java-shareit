ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ITEMS ALTER COLUMN ID RESTART WITH 1;
DELETE FROM USERS;

-- INSERT INTO USERS (EMAIL, NAME)
-- VALUES ('BUGAGA@mail.ru', 'Olen'),
--        ('GUGAGA@mail.ru', 'Inok'),
--        ('DUDADA@gmail.com', 'fanok');

-- INSERT INTO ITEMS (name, description, available, owner, request)
-- VALUES ('Drel', 'drelit', true, 1, null),
--        ('Shurupowert', 'shurupit', true, 3, null);
