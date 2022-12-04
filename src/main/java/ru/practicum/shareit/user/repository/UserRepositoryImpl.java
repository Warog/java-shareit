package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.sql.SqlRequests;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.MapRowToUserDto;
import ru.practicum.shareit.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDto getUser(int id) {
        return jdbcTemplate.queryForObject(SqlRequests.SQL_GET_USER_BY_ID, new MapRowToUserDto(), id);
//        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public List<User> allUsers() {
        return null;
    }

    @Override
    public void deleteAllUsers() {

    }

    public User toUser(ResultSet rs, int rowNum) throws SQLDataException {
        return User.builder()
                .id(1)
                .email("")
                .name("")
                .build();
    }
}
