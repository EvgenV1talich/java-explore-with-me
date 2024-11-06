package ru.yandex.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.model.event.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
