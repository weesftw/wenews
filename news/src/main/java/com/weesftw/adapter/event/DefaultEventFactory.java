package com.weesftw.adapter.event;

import com.weesftw.adapter.event.impl.NewsExternalEventHandler;
import com.weesftw.adapter.event.impl.NewsHttpEventHandler;
import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.NewsService;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.context.Factory;
import com.weesftw.common.event.EventHandler;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Factory
public class DefaultEventFactory {

    @Singleton
    public EventHandler<HttpRequest<CreateNewsRequest>> newsPublisher(NewsService service, @Named("createNewsSubscriberChain") SubscriberChain<Message<CreateNewsRequest>> createSubscriberChain, @Named("newsSubscriberChain") SubscriberChain<Message<News>> newsSubscriberChain) {
        return new NewsHttpEventHandler(service, newsSubscriberChain, createSubscriberChain);
    }

    @Singleton
    public EventHandler<CreateNewsRequest> newsExternalPublisher(NewsService service, @Named("createNewsSubscriberChain") SubscriberChain<Message<CreateNewsRequest>> createSubscriberChain, @Named("newsSubscriberChain") SubscriberChain<Message<News>> newsSubscriberChain) {
        return new NewsExternalEventHandler(service, newsSubscriberChain, createSubscriberChain);
    }
}
