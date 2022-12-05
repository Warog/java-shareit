package ru.practicum.shareit.sql;

public interface SqlRequests {
    String SQL_GET_USER_BY_ID = String.format("SELECT * FROM %s WHERE id = ?", Tables.USERS);
    String SQL_GET_USER_BY_EMAIL = String.format("SELECT * FROM %s WHERE EMAIL LIKE ?", Tables.USERS);
    String SQL_GET_COUNT_OF_USERS_WITH_SAME_EMAIL = String.format("SELECT COUNT(*) FROM %s WHERE EMAIL LIKE ?", Tables.USERS);
    String SQL_ADD_USER = String.format("INSERT INTO %s(name, email) VALUES (?,?)", Tables.USERS);
    String SQL_UPDATE_USER_BY_ID = String.format("UPDATE %s SET NAME = COALESCE(?, NAME), EMAIL = COALESCE(?, EMAIL) WHERE ID = ?", Tables.USERS);
    String SQL_GET_ALL_USERS = String.format("SELECT * FROM %s", Tables.USERS);
    String SQL_DELETE_USER_BY_ID = String.format("DELETE FROM %s WHERE ID = ?", Tables.USERS);
    String SQL_DELETE_USER_BY_EMAIL = String.format("DELETE FROM %s WHERE EMAIL LIKE ?", Tables.USERS);


    enum Tables {
        USERS,
        ITEMS,
        ITEM_REQUESTS,
        BOOKING
    }
}
