package com.weesftw.common.event;

@FunctionalInterface
public interface EventHandler<T> {

    void handle(T obj);
}
