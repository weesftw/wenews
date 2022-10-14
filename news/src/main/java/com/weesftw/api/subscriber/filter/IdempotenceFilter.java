package com.weesftw.api.subscriber.filter;

import com.weesftw.api.Message;
import com.weesftw.api.exception.NewsAlreadyCommitedException;
import com.weesftw.api.model.News;
import com.weesftw.api.repository.NewsRepository;
import com.weesftw.common.chain.SubscriberFilter;
import com.weesftw.common.chain.SubscriberPhase;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class IdempotenceFilter implements SubscriberFilter<Message<News>> {

    private final NewsRepository.OutboxNewsRepository repository;

    public IdempotenceFilter(NewsRepository.OutboxNewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public int getOrder() {
        return SubscriberPhase.FIRST.after();
    }

    @Override
    public void doFilter(Message<News> message) throws SQLException {
        var isCommited = repository.isCommited(message.body().getId());
        if(isCommited)
            throw new NewsAlreadyCommitedException();
    }
}