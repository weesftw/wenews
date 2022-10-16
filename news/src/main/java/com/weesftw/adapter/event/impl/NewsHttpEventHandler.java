package com.weesftw.adapter.event.impl;

import com.weesftw.adapter.event.EventHandler;
import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.NewsService;
import com.weesftw.common.chain.SubscriberChain;
import io.micronaut.http.HttpRequest;

public class NewsHttpEventHandler implements EventHandler<HttpRequest<CreateNewsRequest>> {

    private final NewsService service;
    private final SubscriberChain<Message<News>> chain;

    public NewsHttpEventHandler(NewsService service, SubscriberChain<Message<News>> chain) {
        this.service = service;
        this.chain = chain;
    }

    @Override
    public void handle(HttpRequest<CreateNewsRequest> request) {
        if(request.getBody().isPresent()) {
            var body = request.getBody().get();
            var news = service.saveNews(body);

            chain.proceed(() -> news);
            return;
        }

        throw new RuntimeException();
    }
}
