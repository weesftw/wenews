package com.weesftw.api.subscriber;

import com.weesftw.adapter.listener.publisher.NewsStream;
import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.api.subscriber.filter.IdempotenceFilter;
import com.weesftw.api.subscriber.filter.PublisherNewsFilter;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.chain.SubscriberFilter;
import com.weesftw.common.context.Factory;
import jakarta.inject.Singleton;

import java.util.ArrayList;

@Factory
public class DefaultSubscriberFactory {

    @Singleton
    public SubscriberChain<Message<News>> subscriberChain(NewsRepository.OutboxNewsRepository repository, NewsStream stream) {
        var filters = new ArrayList<SubscriberFilter<Message<News>>>();
        filters.add(new IdempotenceFilter(repository));
        filters.add(new PublisherNewsFilter(stream, repository));
        return new NewsSubscriberChain(filters);
    }
}
