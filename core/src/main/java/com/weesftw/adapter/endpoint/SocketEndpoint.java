package com.weesftw.adapter.endpoint;

import com.weesftw.api.model.Socket;
import com.weesftw.api.service.SocketService;
import com.weesftw.api.service.SenderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.*;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

import static java.lang.String.format;

@Slf4j
@PermitAll
@ServerWebSocket("/{username}/{category}")
public class SocketEndpoint {

    public static final String CONSOLE = "Console";

    private final SocketService socketService;
    private final SenderService senderService;
    private final WebSocketBroadcaster broadcaster;

    public SocketEndpoint(WebSocketBroadcaster broadcaster, SocketService socketService, SenderService senderService) {
        this.broadcaster = broadcaster;
        this.socketService = socketService;
        this.senderService = senderService;
    }

    @OnOpen
    public Publisher<Object> onOpen(WebSocketSession session, @PathVariable String category, @PathVariable String username) {
        log("onOpen", session, username, category);

        var socket = Socket.builder()
                .user(username)
                .session(session.getId())
                .build();

        socketService.addSocket(socket, category);
        var response = senderService.sendOnJoinLeft(CONSOLE, format("%s joined.", username), category);
        return broadcaster.broadcast(response, isValid(category));
    }

    @OnMessage
    public Publisher<Object> onMessage(@PathVariable String category, @PathVariable String username, String message, WebSocketSession session) {
        log("onMessage", session, username, category);

        var response = senderService.send(username, message);
        return broadcaster.broadcast(response, isValid(category));
    }

    @OnClose
    public Publisher<Object> onClose(@PathVariable String category, @PathVariable String username, WebSocketSession session) {
        log("onClose", session, username, category);

        socketService.removeSocket(username);
        var response = senderService.sendOnJoinLeft(CONSOLE, format("%s left.", username), category);
        return broadcaster.broadcast(response, isValid(category));
    }

    @OnError
    public HttpResponse<Object> onError(@PathVariable String username) {
        return HttpResponse.badRequest();
    }

    private void log(String event, WebSocketSession session, String username, String category) {
        log.info("ws: {} received for session {} from '{}' regarding '{}'", event, session.getId(), username, category);
    }

    private Predicate<WebSocketSession> isValid(String category) {
        return entries -> category.equalsIgnoreCase(entries.getUriVariables().get("category", String.class, null));
    }
}
