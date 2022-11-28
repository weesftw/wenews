package com.weesftw.api.chain;

import com.weesftw.api.Message;
import com.weesftw.api.exception.NewsAlreadyCommitedException;
import com.weesftw.api.model.News;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.chain.SubscriberFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.lang.String.format;

@Slf4j
public class NewsSubscriberChain implements SubscriberChain<Message<News>> {

    private final List<SubscriberFilter<Message<News>>> filters;

    public NewsSubscriberChain(List<SubscriberFilter<Message<News>>> news) {
        this.filters = news;
    }

    @Override
    public void proceed(Message<News> message) {
        try {
            filters.forEach(filter -> filter.doFilter(message));
        } catch (NewsAlreadyCommitedException e) {
            log.warn(format("[%s] News has already committed, skipped all others processors.", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error, skipped all others processors.", e);
            throw e;
        }
    }
}
