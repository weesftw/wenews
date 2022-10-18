package com.weesftw.adapter.endpoint;

import com.weesftw.api.service.CategoryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

@Controller
public class CategoryEndpoint {

    private final CategoryService service;

    public CategoryEndpoint(CategoryService service) {
        this.service = service;
    }

    @Get("/v1/categories")
    public HttpResponse<Object> getCategories() {
        return HttpResponse.ok(service.getAll());
    }

    @Get("/v1/categories/{name}")
    public HttpResponse<Object> getCategories(@PathVariable String name) {
        return HttpResponse.ok(service.getCategory(name));
    }
}
