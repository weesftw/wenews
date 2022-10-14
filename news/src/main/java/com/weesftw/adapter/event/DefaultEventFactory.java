package com.weesftw.adapter.event;

import com.weesftw.adapter.event.impl.NewsHttpEventHandler;
import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.service.NewsService;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.context.Factory;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;

@Factory
public class DefaultEventFactory {

    @Singleton
    public EventHandler<HttpRequest<CreateNewsRequest>> newsPublisher(NewsService service, SubscriberChain<Message<News>> chain) {
        return new NewsHttpEventHandler(service, chain);
    }
}
