package ru.yandex.controller.adminapi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.user.NewUserRequest;
import ru.yandex.dto.user.UserDto;
import ru.yandex.model.user.User;
import ru.yandex.service.adminapi.user.AdminUserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.yandex.mapper.UserMapper.toUserDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    private final AdminUserService service;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) List<Long> ids,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("Received /GET request to AdminUserController...");
        return new ResponseEntity<>(pagedResponse(service.getUsers(ids), from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> postUser(@RequestBody @Valid NewUserRequest newUserRequest) {


        log.info("Received /POST request to AdminUserController...");
        return new ResponseEntity<>(toUserDto(service.postUser(newUserRequest)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int userId) {

        log.info("Received /DELETE request to AdminUserController...");
        service.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private List<UserDto> pagedResponse(List<User> users, int from, int size) {
        List<UserDto> pagedUsers = new ArrayList<>();

        int totalUsers = users.size();
        int toIndex = from + size;

        if (from <= totalUsers) {
            if (toIndex > totalUsers) {
                toIndex = totalUsers;
            }
            for (User user : users.subList(from, toIndex)) {
                pagedUsers.add(toUserDto(user));
            }
            return pagedUsers;
        } else {
            return Collections.emptyList();
        }
    }

}
