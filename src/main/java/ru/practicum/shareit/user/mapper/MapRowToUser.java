package ru.practicum.shareit.user.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import ru.practicum.shareit.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
public class MapRowToUser implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .build();
    }
}
