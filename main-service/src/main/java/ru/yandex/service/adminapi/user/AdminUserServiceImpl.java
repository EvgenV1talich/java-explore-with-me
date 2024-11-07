package ru.yandex.service.adminapi.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.dto.user.NewUserRequest;
import ru.yandex.error.apierror.exceptions.SaveException;
import ru.yandex.model.user.User;
import ru.yandex.repository.UserRepository;

import java.util.List;

import static ru.yandex.mapper.UserMapper.toUser;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository repository;

    @Override
    public List<User> getUsers(List<Long> ids) {
        log.info("Сформирован список пользователей");

        if (ids == null) {
            return repository.findAll();
        } else {
            return repository.findAllByIdIn(ids);
        }
    }

    @Override
    public User postUser(NewUserRequest newUserRequest) {
        User user;
        if (newUserRequest.getEmail().length() > 254) {
            throw new IllegalArgumentException("Field: email. Error: must not be blank. Value: null");
        }
        try {
            user = repository.save(toUser(newUserRequest));
        } catch (DataIntegrityViolationException e) {
            throw new SaveException("could not execute statement; SQL [n/a]; constraint [uq_email]; "
                    + "nested exception is org.hibernate.exception.ConstraintViolationException: "
                    + "could not execute statement");
        }
        log.info("Добавлен новый пользователь: " + user);

        return user;
    }

    @Override
    public void deleteUserById(int userId) {
        log.info("Пользователь с id=" + userId + " удален");

        repository.deleteById((long) userId);
    }
}
