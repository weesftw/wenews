package com.weesftw.api.chain.filter;

import com.weesftw.adapter.listener.publisher.NewsPublisher;
import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.common.chain.SubscriberFilter;
import com.weesftw.common.chain.SubscriberPhase;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class PublisherNewsFilter implements SubscriberFilter<Message<News>> {

    private final NewsPublisher stream;
    private final NewsRepository.OutboxNewsRepository repository;

    public PublisherNewsFilter(NewsPublisher stream, NewsRepository.OutboxNewsRepository repository) {
        this.stream = stream;
        this.repository = repository;
    }

    @Override
    public int getOrder() {
        return SubscriberPhase.LAST.order();
    }

    @Override
    public void doFilter(Message<News> message) {
        log.info("Publishing news to stream...");
        try {
            repository.commit(message.body());
            stream.send(message.body());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
