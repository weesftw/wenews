package com.weesftw.common.chain;

public interface SubscriberChain<T> {

	void proceed(T message);
}