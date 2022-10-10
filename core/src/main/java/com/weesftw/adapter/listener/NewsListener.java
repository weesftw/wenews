package com.weesftw.adapter.listener;

import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RabbitListener
public class NewsListener {

    @Queue("news")
    public void receive(byte[] data) {
        log.info("News received: {}", new String(data));
    }
}
