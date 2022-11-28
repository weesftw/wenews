package com.weesftw.adapter.endpoint.socket;

import com.weesftw.adapter.endpoint.socket.model.response.SocketResponse;
import com.weesftw.adapter.endpoint.socket.service.SocketService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.*;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
@PermitAll // TODO: make this private only authorization with socket token private
@ServerWebSocket("/{username}/{category}")
public class SocketEndpoint {

    private final SocketService socketService;
    private final WebSocketBroadcaster broadcaster;

    public SocketEndpoint(WebSocketBroadcaster broadcaster, SocketService socketService) {
        this.broadcaster = broadcaster;
        this.socketService = socketService;
    }

    @OnOpen
    public Publisher<SocketResponse> onOpen(WebSocketSession session, @PathVariable String category, @PathVariable String username) {
        log("onOpen", session, username, category);
        var response = socketService.registerSocket(username, category, session);
        return broadcaster.broadcast(response, all(category));
    }

    @OnMessage
    public Publisher<SocketResponse> onMessage(@PathVariable String category, @PathVariable String username, String message, WebSocketSession session) {
        log("onMessage", session, username, category);
        var response = socketService.createMessage(username).content(message);
        return broadcaster.broadcast(response, all(category));
    }

    @OnClose
    public Publisher<SocketResponse> onClose(@PathVariable String category, @PathVariable String username, WebSocketSession session) {
        log("onClose", session, username, category);
        var response = socketService.removeSocket(username);
        return broadcaster.broadcast(response, all(category));
    }

    @OnError
    public HttpResponse<Object> onError(Throwable error, @PathVariable String username) {
        return HttpResponse.badRequest(Map.of("errors", List.of(error.getMessage())));
    }

    private void log(String event, WebSocketSession session, String username, String category) {
        log.info("ws: {} received for session {} from '{}' regarding '{}'", event, session.getId(), username, category);
    }

    private Predicate<WebSocketSession> all(String category) {
        return entries -> category.equalsIgnoreCase(entries.getUriVariables().get("category", String.class, null));
    }
}
