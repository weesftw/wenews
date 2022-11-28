package com.weesftw.adapter.endpoint.socket.service.impl;

import com.weesftw.adapter.client.HttpNewsClient;
import com.weesftw.adapter.endpoint.socket.model.Socket;
import com.weesftw.adapter.endpoint.socket.model.response.SocketResponse;
import com.weesftw.adapter.endpoint.socket.service.SocketService;
import com.weesftw.adapter.listener.Newsletter;
import com.weesftw.api.exception.UserNotFoundException;
import com.weesftw.api.service.UserService;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.weesftw.adapter.endpoint.socket.SocketEvent.*;
import static java.lang.String.format;
import static java.util.Collections.emptyList;

@Slf4j
@Singleton
public class DefaultSocketService implements SocketService {

    private final Map<String, List<Socket>> sockets = new LinkedHashMap<>();

    private final HttpNewsClient httpNewsClient;
    private final UserService userService;
    private final Newsletter newsletter;

    public DefaultSocketService(HttpNewsClient httpNewsClient, UserService userService, Newsletter newsletter) {
        this.httpNewsClient = httpNewsClient;
        this.userService = userService;
        this.newsletter = newsletter;
    }

    @Override
    public SocketResponse createMessage(String username) {
        return SocketResponse.builder()
                .socket(getSocket(username).orElse(null));
    }

    @Override
    public Optional<Socket> getSocket(String username) {
        return sockets.values().stream()
                .flatMap(List::stream)
                .filter(socket -> socket.getUser().getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public SocketResponse registerSocket(String username, String category, WebSocketSession session) {
        var user = userService.getUser(username);
        if (user != null) {
            var socket = Socket.builder()
                    .user(user)
                    .category(category)
                    .session(session)
                    .build();

            newsletter.register(socket);
            sockets.computeIfAbsent(category, v -> new ArrayList<>()).add(socket);
            session.sendSync(createMessage(username)
                    .news(httpNewsClient.getNews(category))
                    .event(LOAD_NEWS));

            return createMessage(username)
                    .content(format("%s joined.", username))
                    .users(getSockets(category))
                    .event(USER_JOINED);
        }

        throw new UserNotFoundException();
    }

    @Override
    public SocketResponse removeSocket(String username) {
        List<Socket> users = new ArrayList<>();
        getSocket(username).ifPresent(socket -> {
            sockets.get(socket.getCategory()).remove(socket);
            users.addAll(getSockets(socket.getCategory()));
            socket.getSession().close();
        });

        return createMessage(username)
                .event(USER_LEFT)
                .content(format("%s left.", username))
                .users(users);
    }

    @Override
    public Map<String, List<Socket>> getSockets() {
        return sockets;
    }

    @Override
    public List<Socket> getSockets(String category) {
        return sockets.entrySet().stream()
                .filter(entry -> entry.getKey().equals(category))
                .map(Map.Entry::getValue).findFirst().orElse(emptyList());
    }
}
