package com.weesftw.adapter.endpoint;

import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.NewsService;
import com.weesftw.common.event.EventHandler;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
public class NewsEndpoint {

    private final NewsService newsService;
    private final EventHandler<HttpRequest<CreateNewsRequest>> e;

    public NewsEndpoint(NewsService newsService, EventHandler<HttpRequest<CreateNewsRequest>> e) {
        this.newsService = newsService;
        this.e = e;
    }

    @Get("/v1/news/{category}")
    public HttpResponse<Object> getNews(@PathVariable String category, @QueryValue int size) {
        return HttpResponse.ok(newsService.getNews(category, size));
    }

    @Post("/v1/news")
    @RolesAllowed({"ROLE_ADMIN"})
    public HttpResponse<Object> saveNews(@Valid @Body CreateNewsRequest body, HttpRequest<CreateNewsRequest> request) {
        e.handle(request);
        return HttpResponse.created(body);
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<Object> onConstraintViolationException(ConstraintViolationException ex) {
        return HttpResponse.badRequest(Map.of("errors", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList()));
    }
}
