package com.weesftw.api.chain;

import com.weesftw.api.Message;
import com.weesftw.api.exception.QualityNewsException;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.common.chain.SubscriberChain;
import com.weesftw.common.chain.SubscriberFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CreateNewsSubscriberChain implements SubscriberChain<Message<CreateNewsRequest>> {

    private final List<SubscriberFilter<Message<CreateNewsRequest>>> filters;

    public CreateNewsSubscriberChain(List<SubscriberFilter<Message<CreateNewsRequest>>> news) {
        this.filters = news;
    }

    @Override
    public void proceed(Message<CreateNewsRequest> message) {
        try {
            filters.forEach(filter -> filter.doFilter(message));
        } catch (QualityNewsException e) {
            log.warn("News is not valid, skipped all others processors.");
        } catch (Exception e) {
            log.warn("Occurred an error while processing the news, skipped all others processors.");
            throw e;
        }
    }
}
