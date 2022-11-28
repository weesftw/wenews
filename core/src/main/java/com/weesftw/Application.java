package com.weesftw;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Singleton;

import java.text.SimpleDateFormat;

import static java.util.TimeZone.getTimeZone;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

    @Singleton
    static class ObjectMapperBeanEventListener implements BeanCreatedEventListener<ObjectMapper> {

        @Override
        public ObjectMapper onCreated(BeanCreatedEvent<ObjectMapper> event) {
            final var mapper = event.getBean();
            mapper.setTimeZone(getTimeZone("America/Sao_Paulo"));
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            return mapper;
        }
    }
}
