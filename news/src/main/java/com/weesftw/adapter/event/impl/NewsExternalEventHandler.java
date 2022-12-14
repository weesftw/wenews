package com.weesftw.adapter.event.impl;

import com.weesftw.api.Message;
import com.weesftw.api.exception.NewsAlreadyExistsException;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.NewsService;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.event.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewsExternalEventHandler implements EventHandler<CreateNewsRequest> {

    private final NewsService service;
    private final SubscriberChain<Message<News>> newsSubscriberChain;
    private final SubscriberChain<Message<CreateNewsRequest>> createSubscriberChain;

    public NewsExternalEventHandler(NewsService service, SubscriberChain<Message<News>> newsSubscriberChain, SubscriberChain<Message<CreateNewsRequest>> createSubscriberChain) {
        this.service = service;
        this.newsSubscriberChain = newsSubscriberChain;
        this.createSubscriberChain = createSubscriberChain;
    }

    @Override
    public void handle(CreateNewsRequest body) {
        if(body != null) {
            createSubscriberChain.proceed(() -> body);

            News news;
            try {
                news = service.saveNews(body);
            } catch(NewsAlreadyExistsException e) {
                news = service.get(body.getTitle());
                if(news == null)
                    throw new RuntimeException();
                log.warn("News already exists, trying send to queue: {}", news.getId());
            }

            sendToQueue(news);
            return;
        }

        throw new RuntimeException();
    }

    private void sendToQueue(final News news) {
        newsSubscriberChain.proceed(() -> news);
    }
}
