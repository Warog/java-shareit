CREATE TABLE IF NOT EXISTS USERS
(
    ID    INTEGER NOT NULL AUTO_INCREMENT,
    EMAIL CHARACTER VARYING NOT NULL,
    NAME  CHARACTER VARYING,

    CONSTRAINT PK_USER_ID PRIMARY KEY (ID),
    UNIQUE (EMAIL)
);

CREATE TABLE IF NOT EXISTS ITEM_REQUESTS
(
    ID          INTEGER NOT NULL AUTO_INCREMENT,
    DESCRIPTION CHARACTER VARYING,
    REQUESTOR   INTEGER,
    CREATED     TIMESTAMP,

    CONSTRAINT PK_ITEM_REQUEST_ID PRIMARY KEY (ID),
    CONSTRAINT FK_REQUEST_USER FOREIGN KEY (REQUESTOR) REFERENCES USERS (ID) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ID          INTEGER NOT NULL AUTO_INCREMENT,
    NAME        CHARACTER VARYING,
    DESCRIPTION CHARACTER VARYING,
    AVAILABLE   BOOLEAN,
    OWNER       INTEGER,
    REQUEST     INTEGER,

    CONSTRAINT PK_ITEM_ID PRIMARY KEY (ID),
    CONSTRAINT FK_ITEM_USER FOREIGN KEY (OWNER) REFERENCES USERS (ID) ON DELETE CASCADE ON UPDATE RESTRICT
--     CONSTRAINT FK_ITEM_REQUEST FOREIGN KEY (REQUEST) REFERENCES ITEM_REQUESTS (ID) ON DELETE SET NULL ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS BOOKING
(
    ID     INTEGER NOT NULL AUTO_INCREMENT,
    START_TIME  TIMESTAMP,
    END_TIME    TIMESTAMP,
    ITEM   INTEGER,
    BOOKER INTEGER,
    STATUS CHARACTER VARYING,

    CONSTRAINT PK_BOOKING_ID PRIMARY KEY (ID),
    CONSTRAINT FK_BOOKING_USER FOREIGN KEY (BOOKER) REFERENCES USERS (ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT FK_BOOKING_ITEM FOREIGN KEY (ITEM) REFERENCES ITEMS (ID) ON DELETE CASCADE ON UPDATE RESTRICT
);