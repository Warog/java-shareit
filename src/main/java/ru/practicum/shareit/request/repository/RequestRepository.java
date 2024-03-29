package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> getAllByRequestorOrderByCreatedDesc(int ownerID);

    List<Request> getRequestsByRequestorNot(int userId);

    Page<Request> getRequestsByRequestorNot(int userId, Pageable pageable);

}
