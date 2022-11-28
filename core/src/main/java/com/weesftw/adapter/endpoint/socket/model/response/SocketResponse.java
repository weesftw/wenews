package com.weesftw.adapter.endpoint.socket.model.response;

import com.weesftw.adapter.endpoint.socket.SocketEvent;
import com.weesftw.adapter.endpoint.socket.model.Socket;
import com.weesftw.api.model.News;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Introspected
public class SocketResponse {

    private SocketEvent[] event;
    private final SocketContent content = new SocketContent();
    private final SocketProperties properties = new SocketProperties();

    public static SocketResponse builder() {
        return new SocketResponse();
    }

    public SocketResponse event(SocketEvent... event) {
        this.event = event;
        return this;
    }

    public SocketResponse socket(Socket socket) {
        this.content.socket = socket;
        return this;
    }

    public SocketResponse content(String content) {
        this.content.content = content;
        return this;
    }

    public SocketResponse news(List<News> news) {
        this.properties.news = news;
        return this;
    }

    public SocketResponse users(List<Socket> users) {
        this.properties.users = users;
        return this;
    }

    @Getter
    public static class SocketProperties {

        private List<News> news;
        private List<Socket> users;
    }

    @Getter
    public static class SocketContent {

        private Socket socket;
        private String content;
        private final String dateTime = LocalDateTime.now().toString();
    }
}
