package com.weesftw.adapter.endpoint;

import com.weesftw.api.service.CategoryService;
import com.weesftw.api.service.SocketService;
import com.weesftw.api.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@PermitAll
@Controller
public class GeneralEndpoint {

    private final UserService userService;
    private final SocketService socketService;
    private final CategoryService categoryService;

    public GeneralEndpoint(UserService userService, SocketService socketService, CategoryService categoryService) {
        this.userService = userService;
        this.socketService = socketService;
        this.categoryService = categoryService;
    }

    @Get("/v1/users")
    public HttpResponse<Object> getUsers() {
        return HttpResponse.ok(userService.getUsers());
    }

    @Get("/v1/users/{name}")
    public HttpResponse<Object> getUsers(@PathVariable String name) {
        return HttpResponse.ok(userService.getUser(name));
    }

    @Get("/v1/categories")
    public HttpResponse<Object> getCategories() {
        var result = categoryService.getAll();
        var allUsers = result.stream().reduce(0, (a, b) -> a + b.getCount(), Integer::sum);
        return HttpResponse.ok(Map.of("allUsers", allUsers, "categories", result));
    }

    @Get("/v1/categories/{category}")
    public HttpResponse<Object> getByCategory(@PathVariable String category) {
        var result = socketService.getSockets(category);
        return HttpResponse.ok(result);
    }
}
