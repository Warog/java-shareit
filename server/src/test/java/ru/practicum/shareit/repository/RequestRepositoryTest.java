package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.PersistenceConfig;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"file:src/test/java/resources/test_data.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceConfig.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:file:./db/shareitTest"
})
public class RequestRepositoryTest {
    @Autowired
    RequestRepository requestRepository;

    @BeforeAll
    static void setUp() {

    }

    @Test
    void saveRequest() {
        Request request = Request.builder()
                .description("Hammer")
                .requestor(2)
                .build();

        Request resultRequest = requestRepository.save(request);

        assertNotNull(resultRequest);
        assertEquals("Hammer", resultRequest.getDescription());
        assertEquals(2, resultRequest.getRequestor());

    }

    @Test
    void getRequestsByRequestorNot() {
        List<Request> requests = requestRepository.getRequestsByRequestorNot(2);

        assertEquals(1, requests.size());
        assertEquals(3, requests.get(0).getRequestor());
    }

    @Test
    void getRequestsByRequestorNot_withPagination() {

        Page<Request> requests = requestRepository.getRequestsByRequestorNot(1, PageRequest.of(0, 1, Sort.unsorted()));

        assertEquals(1, requests.toList().size());

        Page<Request> requestsTwo = requestRepository.getRequestsByRequestorNot(1, PageRequest.of(0, 2, Sort.unsorted()));

        assertEquals(2, requestsTwo.toList().size());
    }

    @Test
    void getAllByRequestorOrderByCreatedDesc() {
        List<Request> requests = requestRepository.getAllByRequestorOrderByCreatedDesc(2);

        assertTrue(requests.get(0).getCreated().isAfter(requests.get(1).getCreated()));
    }
}
