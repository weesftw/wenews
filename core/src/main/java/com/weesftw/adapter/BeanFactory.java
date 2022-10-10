package com.weesftw.adapter;

import com.weesftw.context.Environment;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class BeanFactory {

    @Bean
    @Context
    @Singleton
    public Environment getEnvironment(io.micronaut.context.env.Environment environment) {
        return key -> environment.getProperty(key, String.class).orElse(null);
    }
}
