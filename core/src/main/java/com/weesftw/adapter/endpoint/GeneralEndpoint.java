package com.weesftw.adapter.endpoint;

import com.weesftw.api.service.SocketService;
import com.weesftw.api.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PermitAll
@Controller
public class GeneralEndpoint {

    private final UserService userService;

    public GeneralEndpoint(UserService userService, SocketService socketService) {
        this.userService = userService;
    }

    @Get("/v1/users")
    public HttpResponse<Object> getUsers() {
        return HttpResponse.ok(userService.getUsers());
    }

    @Get("/v1/users/{name}")
    public HttpResponse<Object> getUsers(@PathVariable String name) {
        return HttpResponse.ok(userService.getUser(name));
    }
}
