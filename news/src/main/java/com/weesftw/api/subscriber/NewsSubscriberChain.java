package com.weesftw.api.subscriber;

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
        for(var messageSubscriberFilter : filters) {
            try {
                messageSubscriberFilter.doFilter(message);
            } catch (NewsAlreadyCommitedException e) {
                log.warn(format("[%s] News has already committed, skipped all others processors.", e.getMessage()));
                break;
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
