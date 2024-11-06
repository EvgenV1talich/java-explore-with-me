package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(List<Long> ids);

}
