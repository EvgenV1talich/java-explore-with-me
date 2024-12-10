package ru.yandex.service.adminapi.user;

import ru.yandex.dto.user.NewUserRequest;
import ru.yandex.model.user.User;

import java.util.List;

public interface AdminUserService {

    List<User> getUsers(List<Long> ids);

    User postUser(NewUserRequest newUserRequest);

    void deleteUserById(int userId);

    User getUser(Long userId);

    boolean exists(Long id);

}
