package com.weesftw.adapter.endpoint;

import com.weesftw.api.exception.CategoryAlreadyExistsException;
import com.weesftw.api.model.request.CreateCategoryRequest;
import com.weesftw.api.service.CategoryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static io.micronaut.http.HttpStatus.CREATED;

@PermitAll
@Controller
public class CategoryEndpoint {

    private final CategoryService service;

    public CategoryEndpoint(CategoryService service) {
        this.service = service;
    }

    @Get("/v1/categories")
    public HttpResponse<Object> getCategories() {
        return HttpResponse.ok(Map.of(
                "internal", service.getAll(),
                "external", service.getAllExternal()
        ));
    }

    @Get("/v1/categories/{name}")
    public HttpResponse<Object> getCategories(@PathVariable String name) {
        return HttpResponse.ok(service.getCategory(name));
    }

    @Post("/v1/categories")
    @RolesAllowed({"ROLE_MOD", "ROLE_ADMIN"})
    public HttpResponse<Object> createCategory(@Valid @Body CreateCategoryRequest request) {
        service.createCategory(request);
        return HttpResponse.status(CREATED);
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<Object> onConstraintViolationException(ConstraintViolationException ex) {
        return HttpResponse.badRequest(Map.of("errors", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList()));
    }

    @Error(exception = CategoryAlreadyExistsException.class)
    public HttpResponse<Object> onCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return HttpResponse.badRequest(Map.of("errors", List.of(ex.getMessage())));
    }
}
