package com.weesftw.adapter.listener;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class ChannelPoolListener extends ChannelInitializer {

    @Override
    public void initialize(Channel channel, String name) throws IOException {
        channel.exchangeDeclare("newsapp", BuiltinExchangeType.DIRECT, true);
        channel.queueDeclare("news", true, false, false, null);
        channel.queueBind("news", "newsapp", "news");
    }
}