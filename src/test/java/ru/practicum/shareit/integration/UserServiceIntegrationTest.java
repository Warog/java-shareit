package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/test/java/resources/test_data.sql"})
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:file:./db/shareitTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class UserServiceIntegrationTest {
    private final UserService userService;

    private final EntityManager em;

    @Test
    void allUsers() {
        List<User> users = userService.allUsers();

        assertEquals(4, users.size());

    }

    @Test
    void addUser() {
        User user = User.builder()
                .name("Test")
                .email("test@email.com")
                .build();

        UserDto userDto = userService.addUser(user);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u Where u.email = :email", User.class);
        User result = query.setParameter("email", user.getEmail()).getSingleResult();

        assertNotNull(result);
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void getUser() {
        UserDto user = userService.getUser(1);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Olen", user.getName());

    }

    @Test
    void updateUser() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Freya")
                .email("fr.a@mail.com")
                .build();

        UserDto user = userService.updateUser(userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u Where u.email = :email", User.class);
        User result = query.setParameter("email", user.getEmail()).getSingleResult();

        assertNotNull(user);
        assertEquals(result.getId(), user.getId());
        assertEquals(result.getName(), user.getName());
        assertEquals(result.getEmail(), user.getEmail());

    }

    @Test
    void deleteUser() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u Where u.id = :id", User.class);
        User result = query.setParameter("id", 1).getSingleResult();

        assertNotNull(result);
        assertEquals(1, result.getId());

        userService.deleteUser(1);

        List<User> resultList = query.setParameter("id", 1).getResultList();

        assertEquals(0, resultList.size());

    }

}
