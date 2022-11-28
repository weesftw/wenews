package com.weesftw.adapter.endpoint.socket.model;

import com.weesftw.adapter.endpoint.socket.model.response.SocketResponse;
import com.weesftw.api.Observer;
import com.weesftw.api.model.News;
import com.weesftw.api.model.response.UserResponse;
import io.micronaut.websocket.WebSocketSession;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.weesftw.adapter.endpoint.socket.SocketEvent.NEW_NEWS;

@Slf4j
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Socket implements Observer {

    private UserResponse user;
    private String category;
    private WebSocketSession session;

    public String getSessionId() {
        return session.getId();
    }

    @Override
    public void update(News news) {
        log.info("Received news: {}", news);
        if(session != null)
            session.sendSync(SocketResponse.builder()
                    .event(NEW_NEWS)
                    .news(List.of(news)));
    }
}
