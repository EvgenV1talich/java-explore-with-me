package ru.yandex.controller.privateapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.ParticipationRequestDto;
import ru.yandex.model.request.Request;
import ru.yandex.service.privateapi.user.PrivateUserService;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.mapper.RequestMapper.toParticipationRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateUserController {

    private final PrivateUserService service;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByUserId(@PathVariable int userId) {

        log.info("---START GET REQUESTS BY USER ID ENDPOINT---");

        List<ParticipationRequestDto> requestsDto = new ArrayList<>();
        List<Request> requestList = service.getRequestsByUserId(userId);

        for (Request r : requestList) {
            requestsDto.add(toParticipationRequestDto(r));
        }

        return new ResponseEntity<>(requestsDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> postRequestsByUserId(@PathVariable int userId,
                                                                        @RequestParam int eventId) {

        log.info("---START POST REQUEST ENDPOINT---");

        return new ResponseEntity<>(toParticipationRequestDto(service.postRequestsByUserId(userId, eventId)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateRequestsByUserId(@PathVariable int userId,
                                                                          @PathVariable int requestId) {

        log.info("---START UPDATE REQUEST BY USER ID ENDPOINT---");

        return new ResponseEntity<>(toParticipationRequestDto(service.updateRequestsByUserId(userId, requestId)),
                HttpStatus.OK);
    }

}
