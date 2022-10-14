package com.weesftw.api.subscriber.filter;

import com.weesftw.adapter.listener.publisher.NewsStream;
import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.common.chain.SubscriberFilter;
import com.weesftw.common.chain.SubscriberPhase;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class PublisherNewsFilter implements SubscriberFilter<Message<News>> {

    private final NewsStream stream;
    private final NewsRepository.OutboxNewsRepository repository;

    public PublisherNewsFilter(NewsStream stream, NewsRepository.OutboxNewsRepository repository) {
        this.stream = stream;
        this.repository = repository;
    }

    @Override
    public int getOrder() {
        return SubscriberPhase.LAST.order();
    }

    @Override
    public void doFilter(Message<News> message) throws SQLException {
        repository.commit(message.body());
        stream.send(message.body());
    }
}
