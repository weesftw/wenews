package com.weesftw.adapter.event;

@FunctionalInterface
public interface EventHandler<T> {

    void handler(T obj);
}
