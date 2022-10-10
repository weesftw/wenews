package com.weesftw.adapter.endpoint;

import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.NewsService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.validation.Valid;

@Controller
public class NewsEndpoint {

    private final NewsService newsService;

    public NewsEndpoint(NewsService newsService) {
        this.newsService = newsService;
    }

    @Post("/v1/news")
    public HttpResponse<Object> saveNews(@Valid CreateNewsRequest request) {
        return HttpResponse.created(newsService.saveNews(request));
    }
}
