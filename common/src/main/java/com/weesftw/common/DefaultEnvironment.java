package com.weesftw.common;

import com.weesftw.common.context.Context;
import com.weesftw.common.context.Environment;
import com.weesftw.common.context.Factory;
import jakarta.inject.Singleton;

@Factory
public class DefaultEnvironment {

    @Context
    @Singleton
    public Environment getEnvironment(io.micronaut.context.env.Environment environment) {
        return key -> environment.getProperty(key, String.class).orElse(null);
    }
}
