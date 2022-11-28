package com.weesftw.adapter.endpoint;

import com.weesftw.adapter.endpoint.socket.SocketEvent;
import com.weesftw.adapter.endpoint.socket.service.SocketService;
import com.weesftw.api.exception.PersistenceException;
import com.weesftw.api.model.request.CreateUserRequest;
import com.weesftw.api.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RolesAllowed({"ROLE_MOD", "ROLE_ADMIN"})
public class UserEndpoint {

    private final UserService service;
    private final SocketService socketService;

    public UserEndpoint(UserService service, SocketService socketService) {
        this.service = service;
        this.socketService = socketService;
    }

    @Get("/v1/user/{username}")
    public HttpResponse<Object> getUser(@PathVariable String username) {
        return HttpResponse.ok(service.getUser(username));
    }

    @RolesAllowed("ROLE_ADMIN")
    @Get("/v1/user/ban/{username}")
    public HttpResponse<Object> banUser(@PathVariable String username) {
        if (service.banUser(username))
            socketService.getSocket(username).ifPresent(socket -> {
                var message = socketService.createMessage(username);
                message.content("You have been banned from the server.");
                message.event(SocketEvent.BAN);
                socket.getSession().sendSync(message);
            });
        return HttpResponse.ok();
    }

    @PermitAll
    @Post("/v1/user")
    public HttpResponse<Object> createUser(@Valid CreateUserRequest user) {
        return HttpResponse.created(service.createUser(user));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<Object> onConstraintViolationException(ConstraintViolationException ex) {
        return HttpResponse.badRequest(Map.of("errors", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList()));
    }

    @Error(exception = PersistenceException.class)
    public HttpResponse<Object> onPersistenceException(ConstraintViolationException ex) {
        return HttpResponse.badRequest(Map.of("errors", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList()));
    }
}
