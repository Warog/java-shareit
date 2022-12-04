package ru.practicum.shareit.sql;

public interface SqlRequests {
    String SQL_GET_USER_BY_ID = String.format("SELECT * FROM %s WHERE id = ?", Tables.USERS);

    enum Tables {
        USERS,
        ITEMS,
        ITEM_REQUESTS,
        BOOKING
    }
}
