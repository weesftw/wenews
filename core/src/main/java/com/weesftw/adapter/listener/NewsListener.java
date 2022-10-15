package com.weesftw.adapter.listener;

import com.weesftw.api.model.News;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RabbitListener
public class NewsListener {

    @Queue("news")
    public void receive(News data) {
        log.info("News received: {}", data);
    }
}
