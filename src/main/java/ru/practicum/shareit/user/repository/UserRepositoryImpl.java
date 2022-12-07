package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserDuplicateEmailException;
import ru.practicum.shareit.exception.UserEmailEmptyException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.MapRowToUserDto;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.sql.UserSqlRequest.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDto getUser(int id) {

        return jdbcTemplate.queryForObject(SQL_GET_USER_BY_ID, new MapRowToUserDto(), id);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        try {

            if (Optional.ofNullable(userDto.getEmail()).isEmpty())
                throw new UserEmailEmptyException("Не указан E-Mail");

            Optional<Integer> sameEmailCountOptional = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_COUNT_OF_USERS_WITH_SAME_EMAIL, Integer.class, userDto.getEmail()));
            sameEmailCountOptional.ifPresent(sameEmailCount -> {
                if (sameEmailCount != 0) {
                    throw new UserDuplicateEmailException("Данный E-Mail уже существует!");
                }
            });


            jdbcTemplate.update(SQL_ADD_USER, userDto.getName(), userDto.getEmail());

        } catch (DuplicateKeyException e) {
            throw new UserDuplicateEmailException("Данный E-Mail уже существует!");
        }

        return jdbcTemplate.queryForObject(SQL_GET_USER_BY_EMAIL, new MapRowToUserDto(), userDto.getEmail());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        Optional<Integer> sameEmailCountOptional = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_COUNT_OF_USERS_WITH_SAME_EMAIL, Integer.class, userDto.getEmail()));

        sameEmailCountOptional.ifPresent(sameEmailCount -> {
            if (sameEmailCount > 0) {
                throw new UserDuplicateEmailException("Данный E-Mail уже существует!");
            }
        });

        jdbcTemplate.update(SQL_UPDATE_USER_BY_ID, userDto.getName(), userDto.getEmail(), userDto.getId());

        return getUser(userDto.getId());
    }

    @Override
    public List<UserDto> allUsers() {
        return jdbcTemplate.query(SQL_GET_ALL_USERS, new MapRowToUserDto());
    }

    @Override
    public void deleteUser(int id) {
        jdbcTemplate.update(SQL_DELETE_USER_BY_ID, id);
    }

    @Override
    public void deleteAllUsers() {

    }

}
