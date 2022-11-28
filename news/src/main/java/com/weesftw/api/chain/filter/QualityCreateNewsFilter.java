package com.weesftw.api.chain.filter;

import com.weesftw.api.Message;
import com.weesftw.api.exception.QualityNewsException;
import com.weesftw.api.model.request.CreateNewsRequest;
import com.weesftw.common.chain.SubscriberFilter;
import lombok.extern.slf4j.Slf4j;

import static java.time.LocalDateTime.now;

@Slf4j
public class QualityCreateNewsFilter implements SubscriberFilter<Message<CreateNewsRequest>> {

    @Override
    public void doFilter(Message<CreateNewsRequest> message) {
        log.info("Validating news quality...");
        var body = message.body();
        if(body.getAuthor() == null)
            body.setAuthor("Unknown Author");

        if(body.getPublishedAt() == null)
            body.setPublishedAt(now());

        if(body.getTitle() == null || body.getTitle().isEmpty() || body.getDescription() == null || body.getDescription().isEmpty() || body.getUrl() == null) {
            throw new QualityNewsException();
        }
    }
}
