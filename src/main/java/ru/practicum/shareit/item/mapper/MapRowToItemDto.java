package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import ru.practicum.shareit.item.dto.ItemDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
public class MapRowToItemDto implements RowMapper<ItemDto> {

    @Override
    public ItemDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ItemDto.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .request(rs.getInt("request"))
                .owner(rs.getInt("owner"))
                .available(rs.getBoolean("available"))
                .description(rs.getString("description"))
                .build();
    }
}
