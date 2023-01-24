package ru.practicum.shareit.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.PersistenceConfig;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Sql(scripts = {"file:src/test/java/resources/test_data.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceConfig.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:file:./db/shareitTest"
})
public class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Test
    void findCommentsByItemId() {

        List<Comment> comments = commentRepository.findCommentsByItemId(2);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(2, comments.get(0).getItemId());
        assertEquals("Отличный шуруповерт!", comments.get(0).getText());

    }
}
