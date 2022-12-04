package ru.practicum.shareit.user.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
public class MapRowToUserDto implements RowMapper<UserDto> {
    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserDto.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .build();
    }
}
