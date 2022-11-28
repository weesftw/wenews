package com.weesftw.adapter.endpoint.socket.service;

import com.weesftw.adapter.endpoint.socket.model.Socket;
import com.weesftw.adapter.endpoint.socket.model.response.SocketResponse;
import io.micronaut.websocket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SocketService {

    Map<String, List<Socket>> getSockets();
    List<Socket> getSockets(String category);
    Optional<Socket> getSocket(String username);
    SocketResponse removeSocket(String username);
    SocketResponse createMessage(String username);
    SocketResponse registerSocket(String username, String category, WebSocketSession session);
}
