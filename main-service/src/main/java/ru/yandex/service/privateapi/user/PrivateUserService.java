package ru.yandex.service.privateapi.user;

import ru.yandex.model.request.Request;

import java.util.List;

public interface PrivateUserService {

    List<Request> getRequestsByUserId(int userId);

    Request postRequestsByUserId(int userId, int eventId);

    Request updateRequestsByUserId(int userId, int requestId);

}
