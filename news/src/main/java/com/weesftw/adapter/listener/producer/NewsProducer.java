package com.weesftw.adapter.listener.producer;

import com.weesftw.api.model.News;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("newsapp")
public interface NewsProducer {

    @Binding("news")
    void updateNews(News data);
}
