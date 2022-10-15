package com.weesftw.common.chain;

public interface SubscriberFilter<T> {

    default int getOrder() { return 0; }
    void doFilter(T message);
}
