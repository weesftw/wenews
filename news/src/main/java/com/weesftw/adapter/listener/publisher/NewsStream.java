package com.weesftw.adapter.listener.publisher;

import com.weesftw.api.model.News;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("newsapp")
public interface NewsStream {

    @Binding("news")
    void send(News data);
}
