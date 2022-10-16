package com.weesftw.adapter.event;

@FunctionalInterface
public interface EventHandler<T> {

    void handle(T obj);
}
