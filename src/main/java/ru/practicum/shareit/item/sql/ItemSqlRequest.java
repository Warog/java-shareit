package ru.practicum.shareit.item.sql;

public interface ItemSqlRequest {
    String SQL_GET_ITEM_BY_ID = String.format("SELECT * FROM %s WHERE ID = ?", Tables.ITEMS);
    String SQL_ADD_ITEM = String.format("INSERT INTO %s(name, description, available) VALUES(?,?,?)", Tables.ITEMS);
    String SQL_GET_ALL_ITEMS = String.format("SELECT * FROM %s", Tables.ITEMS);
    String SQL_DELETE_ITEM_BY_ID = String.format("DELETE FROM %s WHERE ID = ?", Tables.ITEMS);
    String SQL_SEARCH_ITEM_BY_DESCRIPTION = String.format("SELECT * FROM %s WHERE LOWER(DESCRIPTION) LIKE ?", Tables.ITEMS);
    enum Tables {
        USERS,
        ITEMS,
        ITEM_REQUESTS,
        BOOKING
    }
}
