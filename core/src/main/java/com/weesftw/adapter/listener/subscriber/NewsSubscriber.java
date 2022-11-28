package com.weesftw.adapter.listener.subscriber;

import com.weesftw.adapter.listener.Newsletter;
import com.weesftw.api.Observer;
import com.weesftw.api.model.News;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RabbitListener
public class NewsSubscriber implements Newsletter {

    private final List<Observer> observers = new ArrayList<>();

    @Queue("news")
    public void receive(News data) {
        notifyObservers(data);
    }

    @Override
    public void notifyObservers(News news) {
        log.info("Notifying observers...");
        observers.forEach(observer -> observer.update(news));
    }

    @Override
    public void register(Observer observer) {
        log.info("Registering observer...");
        this.observers.add(observer);
    }
}
