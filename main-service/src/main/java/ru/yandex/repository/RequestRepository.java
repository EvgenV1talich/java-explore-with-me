package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.model.request.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEventId(int eventId);

    List<Request> findAllByRequesterId(int userId);

    Request findByIdAndRequesterId(int requestId, int userId);

    List<Request> findAllByIdIn(List<Integer> requestIds);

    boolean existsByRequesterIdAndEventId(int userId, int eventId);


}
