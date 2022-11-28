package com.weesftw.adapter.event.impl;

import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.NewsService;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.event.EventHandler;
import io.micronaut.http.HttpRequest;

public class NewsHttpEventHandler implements EventHandler<HttpRequest<CreateNewsRequest>> {

    private final NewsService service;
    private final SubscriberChain<Message<News>> newsSubscriberChain;
    private final SubscriberChain<Message<CreateNewsRequest>> createSubscriberChain;

    public NewsHttpEventHandler(NewsService service, SubscriberChain<Message<News>> newsSubscriberChain, SubscriberChain<Message<CreateNewsRequest>> createSubscriberChain) {
        this.service = service;
        this.newsSubscriberChain = newsSubscriberChain;
        this.createSubscriberChain = createSubscriberChain;
    }

    @Override
    public void handle(HttpRequest<CreateNewsRequest> request) {
        request.getBody().ifPresent(createNewsRequest -> {
            createSubscriberChain.proceed(() -> createNewsRequest);
            var news = service.saveNews(createNewsRequest);
            newsSubscriberChain.proceed(() -> news);
        });
    }
}
