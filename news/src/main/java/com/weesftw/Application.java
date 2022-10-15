package com.weesftw;

import io.micronaut.runtime.Micronaut;

import java.util.TimeZone;

public class Application {

    public static void main(String... args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Micronaut.run(Application.class, args);
    }
}
