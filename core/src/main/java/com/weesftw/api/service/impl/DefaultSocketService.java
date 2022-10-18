package com.weesftw.api.service.impl;

import com.weesftw.api.exception.InvalidCategoryException;
import com.weesftw.api.exception.SocketNotFoundException;
import com.weesftw.api.exception.UserNotFoundException;
import com.weesftw.api.model.Socket;
import com.weesftw.api.service.SocketService;
import com.weesftw.api.service.UserService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static java.util.Collections.emptyList;

@Slf4j
@Singleton
public class DefaultSocketService implements SocketService {

    private final Map<String, List<Socket>> sockets = new LinkedHashMap<>();

    private final UserService userService;

    public DefaultSocketService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Socket> getSocket(String username) {
        return sockets.values().stream()
                .flatMap(List::stream)
                .filter(socket -> socket.getUser().equals(username))
                .findFirst();
    }

    @Override
    public void addSocket(Socket socket) {
        var user = userService.getUser(socket.getUser());
        var category = socket.getCategory();
        if(category != null) {
            if(user != null) {
                if(!sockets.containsKey(category)) {
                    var arr = new ArrayList<Socket>();
                    arr.add(socket);
                    sockets.put(category, arr);
                    return;
                }

                sockets.get(category).add(socket);
                return;
            }

            throw new UserNotFoundException();
        }

        throw new InvalidCategoryException();
    }

    @Override
    public boolean removeSocket(String username) {
        var socket = getSocket(username);
        if(socket.isPresent())
            return sockets.values().stream().anyMatch(sockets -> sockets.remove(socket.get()));

        throw new SocketNotFoundException();
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
