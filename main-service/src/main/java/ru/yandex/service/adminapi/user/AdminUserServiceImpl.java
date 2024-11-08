package ru.yandex.service.adminapi.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.dto.user.NewUserRequest;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.IncorrectParameterException;
import ru.yandex.model.user.User;
import ru.yandex.repository.UserRepository;

import java.text.MessageFormat;
import java.util.List;

import static ru.yandex.mapper.UserMapper.toUser;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository repository;

    @Override
    public List<User> getUsers(List<Long> ids) {
        log.info("Getting users list...");

        if (ids == null) {
            return repository.findAll();
        } else {
            return repository.findAllByIdIn(ids);
        }
    }

    @Override
    @Transactional
    public User postUser(NewUserRequest newUserRequest) {
        User user;
        if (newUserRequest.getEmail().length() > 254) {
            throw new IncorrectParameterException("Field: email. Error: must not be blank. Value: null");
        }
        try {
            user = repository.save(toUser(newUserRequest));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Something wrong in AdminUserServiceImpl postUser method :(");
        }
        log.info(MessageFormat
                .format("Added new user: {0}", user));
        return user;
    }

    @Override
    public void deleteUserById(int userId) {
        log.info(MessageFormat
                .format("User id={0} was deleted!", userId));
        repository.deleteById((long) userId);
    }
}
