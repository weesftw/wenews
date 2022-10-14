package com.weesftw.api.subscriber;

import com.weesftw.api.Message;
import com.weesftw.api.model.News;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.chain.SubscriberFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class NewsSubscriberChain implements SubscriberChain<Message<News>> {

    private final List<SubscriberFilter<Message<News>>> filters;

    public NewsSubscriberChain(List<SubscriberFilter<Message<News>>> news) {
        this.filters = news;
    }

    @Override
    public void proceed(Message<News> message) {
        filters.forEach(messageSubscriberFilter -> {
            try {
                messageSubscriberFilter.doFilter(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
