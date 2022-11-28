package com.weesftw.api.factory;

import com.weesftw.adapter.listener.publisher.NewsPublisher;
import com.weesftw.api.Message;
import com.weesftw.api.chain.CreateNewsSubscriberChain;
import com.weesftw.api.chain.NewsSubscriberChain;
import com.weesftw.api.chain.filter.IdempotenceFilter;
import com.weesftw.api.chain.filter.PublisherNewsFilter;
import com.weesftw.api.chain.filter.QualityCreateNewsFilter;
import com.weesftw.api.model.News;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.chain.SubscriberFilter;
import com.weesftw.common.context.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.LinkedList;

@Factory
public class NewsSubscriberChainFactory {

    @Named
    @Singleton
    public SubscriberChain<Message<News>> newsSubscriberChain(NewsRepository.OutboxNewsRepository repository, NewsPublisher stream) {
        var filters = new LinkedList<SubscriberFilter<Message<News>>>();
        filters.add(new IdempotenceFilter(repository));
        filters.add(new PublisherNewsFilter(stream, repository));
        return new NewsSubscriberChain(filters);
    }

    @Named
    @Singleton
    public SubscriberChain<Message<CreateNewsRequest>> createNewsSubscriberChain() {
        var filters = new LinkedList<SubscriberFilter<Message<CreateNewsRequest>>>();
        filters.add(new QualityCreateNewsFilter());
        return new CreateNewsSubscriberChain(filters);
    }
}
