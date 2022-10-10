package com.weesftw.api.service.impl;

import com.weesftw.api.exception.SenderNotFoundException;
import com.weesftw.api.exception.SocketNotFoundException;
import com.weesftw.api.model.Sender;
import com.weesftw.api.model.Socket;
import com.weesftw.api.model.response.SenderResponse;
import com.weesftw.api.service.SenderService;
import com.weesftw.api.service.SocketService;
import com.weesftw.api.service.UserService;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.weesftw.adapter.endpoint.SocketEndpoint.CONSOLE;
import static com.weesftw.api.Utils.capitalize;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Singleton
public class DefaultSenderService implements SenderService {

    private final List<Sender> senders = new ArrayList<>();

    private final UserService userService;
    private final SocketService socketService;

    public DefaultSenderService(UserService userService, SocketService socketService) {
        this.userService = userService;
        this.socketService = socketService;
    }

    private Sender getSender(String username) {
        var user = userService.getUser(username);

        var sender = senders.stream()
                .filter(s -> s.getUser().getUsername().equals(username))
                .findFirst()
                .or(() -> {
                    var result = Sender.builder()
                            .user(user)
                            .messages(new ArrayList<>())
                            .build();
                    addSender(result);
                    return ofNullable(result);
                });

        return sender.orElse(null);
    }

    private void addSender(Sender sender) {
        senders.add(sender);
    }

    @Override
    public SenderResponse send(String username, String content) {
        if(!bypass(username)) {
            if(socketService.getSocket(username).isPresent()) {
                var sender = getSender(username);
                if(sender != null) {
                    sender.getMessages().add(new Sender.Message(content));
                } else {
                    throw new SenderNotFoundException();
                }
            } else {
                throw new SocketNotFoundException();
            }
        }

        return SenderResponse.builder()
                .username(username)
                .content(content)
                .dateTime(now())
                .build();
    }

    @Override
    public SenderResponse sendOnJoinLeft(String username, String content, String category) {
        var sender = send(username, content);
        sender.setConnectedUsers(socketService.getSockets(capitalize(category)).stream()
                .map(Socket::getUser)
                .collect(Collectors.toList()));
        return sender;
    }

    private boolean bypass(String username) {
        return username.equals(CONSOLE);
    }
}
