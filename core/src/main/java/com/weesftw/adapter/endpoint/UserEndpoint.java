package com.weesftw.adapter.endpoint;

import com.weesftw.api.model.request.CreateUserRequest;
import com.weesftw.api.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Post;
import jakarta.annotation.security.PermitAll;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@PermitAll
@Controller
public class UserEndpoint {

    private final UserService service;

    public UserEndpoint(UserService service) {
        this.service = service;
    }

    @Post("/user")
    public HttpResponse<Object> createUser(@Valid CreateUserRequest user) {
        return HttpResponse.created(service.createUser(user));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<Object> onCreateUserFailed(ConstraintViolationException ex) {
        return HttpResponse.badRequest(Map.of("errors", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList()));
    }
}
