package com.weesftw.adapter.endpoint;

import com.weesftw.adapter.event.EventHandler;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.model.response.NewsErrorResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Post;
import jakarta.annotation.security.RolesAllowed;

import javax.validation.Valid;

@Controller
@RolesAllowed({"ROLE_ADMIN"})
public class NewsEndpoint {

    private final EventHandler<HttpRequest<CreateNewsRequest>> e;

    public NewsEndpoint(EventHandler<HttpRequest<CreateNewsRequest>> e) {
        this.e = e;
    }

    @Post("/v1/news")
    public HttpResponse<Object> saveNews(@Valid CreateNewsRequest body, HttpRequest<CreateNewsRequest> request) {
        e.handle(request);
        return HttpResponse.created(body);
    }

    @Error(global = true)
    public HttpResponse<Object> onError(Throwable e) {
        return HttpResponse.badRequest(new NewsErrorResponse(e.getMessage()));
    }
}
