package com.weesftw.adapter.endpoint;

import com.weesftw.api.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RolesAllowed({"ROLE_MOD", "ROLE_ADMIN"})
public class GeneralEndpoint {

    private final UserService userService;

    public GeneralEndpoint(UserService userService) {
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
