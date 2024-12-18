package ru.yandex.service.adminapi.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.dto.user.NewUserRequest;
import ru.yandex.error.apierror.exceptions.ConflictException;
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
        try {
            user = repository.save(toUser(newUserRequest));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Something wrong in AdminUserServiceImpl postUser method :(\n" + e.getCause());
        }
        log.info("Added new user: {}", user);
        return user;
    }

    @Override
    @Transactional
    public void deleteUserById(int userId) {
        log.info("User id={} was deleted!", userId);
        repository.deleteById((long) userId);
    }

    @Override
    public User getUser(Long userId) {
        return repository.getReferenceById(userId);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }


}
