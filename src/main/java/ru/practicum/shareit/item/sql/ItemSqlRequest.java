package ru.practicum.shareit.item.sql;

public interface ItemSqlRequest {
    String SQL_GET_ITEM_BY_ID = String.format("SELECT * FROM %s WHERE ID = ?", Tables.ITEMS);
    String SQL_ADD_ITEM = String.format("INSERT INTO %s(name, description, available, owner, request) VALUES(?,?,?,?,?)", Tables.ITEMS);
    String SQL_GET_ALL_ITEMS = String.format("SELECT * FROM %s", Tables.ITEMS);
    String SQL_GET_ALL_OWNER_ITEMS_BY_OWNER_ID = String.format("SELECT * FROM %s WHERE OWNER = ?", Tables.ITEMS);
    String SQL_DELETE_ITEM_BY_ID = String.format("DELETE FROM %s WHERE ID = ?", Tables.ITEMS);
    String SQL_SEARCH_ITEM_BY_DESCRIPTION = String.format("SELECT * FROM %s WHERE LOWER(DESCRIPTION) LIKE ? AND AVAILABLE = TRUE", Tables.ITEMS);
    String SQL_UPDATE_ITEM_BY_ID = String.format("UPDATE %s SET NAME = COALESCE(?, NAME), DESCRIPTION = COALESCE(?, DESCRIPTION), AVAILABLE = COALESCE(?, AVAILABLE) WHERE ID = ?", Tables.ITEMS);
    String SQL_FOUND_USER = String.format("SELECT * FROM (SELECT ID FROM %s) WHERE ID = ? ", Tables.USERS);
    String SQL_GET_OWNER_ITEM = String.format("SELECT CASE " +
            "WHEN EXISTS(SELECT 1 FROM %s WHERE OWNER = ? AND ID = ?) " +
            "THEN 1 " +
            "ELSE 0 " +
            "END", Tables.ITEMS);
    String SQL_GET_ITEM_BY_ALL_PARAMETERS = String.format("SELECT * FROM %s WHERE NAME = ? AND DESCRIPTION = ? AND AVAILABLE = ?", Tables.ITEMS);

    enum Tables {
        USERS,
        ITEMS,
        ITEM_REQUESTS,
        BOOKING
    }
}
