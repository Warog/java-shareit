package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserEmailEmptyException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User addUser(User user) {
        if (Optional.ofNullable(user.getEmail()).isEmpty())
            throw new UserEmailEmptyException("Не указан E-Mail");

        entityManager.persist(user);

        return user;
    }

    @Override
    public User getUser(int id) {
        User user = entityManager.find(User.class, id);

        if (user == null)
            throw new UserNotFoundException(String.format("Пользователь с ID = %d не найден!", id));

        return user;
    }

    @Override
    public User updateUser(UserDto user) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> cu = cb.createCriteriaUpdate(User.class);
        Root<User> root = cu.from(User.class);

        if (user.getName() != null)
            cu.set("name", user.getName());

        if(user.getEmail() != null)
            cu.set("email", user.getEmail());

        cu.where(cb.equal(root.get("id"), user.getId()));

        entityManager.createQuery(cu).executeUpdate();

        return getUser(user.getId());

    }

    @Override
    public List<User> allUsers() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public void deleteUser(int id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
        Root<User> root = cd.from(User.class);
        cd.where(cb.equal(root.get("id"), id));

        entityManager.createQuery(cd).executeUpdate();

    }


 /*   @Override
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
    }*/

}
