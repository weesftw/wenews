package com.weesftw.api.service.impl;

import com.weesftw.api.exception.InvalidCategoryException;
import com.weesftw.api.exception.SocketNotFoundException;
import com.weesftw.api.exception.UserNotFoundException;
import com.weesftw.api.model.Category;
import com.weesftw.api.model.Socket;
import com.weesftw.api.service.CategoryService;
import com.weesftw.api.service.SocketService;
import com.weesftw.api.service.UserService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static java.util.Collections.emptyList;

@Slf4j
@Singleton
public class DefaultSocketService implements SocketService {

    private final Map<Category, List<Socket>> sockets = new LinkedHashMap<>();

    private final UserService userService;
    private final CategoryService categoryService;

    public DefaultSocketService(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public Optional<Socket> getSocket(String username) {
        return sockets.values().stream()
                .flatMap(List::stream)
                .filter(socket -> socket.getUser().equals(username))
                .findFirst();
    }

    @Override
    public void addSocket(Socket socket, String category) {
        var user = userService.getUser(socket.getUser());
        var var1 = categoryService.getCategory(category);
        if(var1 != null) {
            if(user != null) {
                if(!sockets.containsKey(var1)) {
                    var arr = new ArrayList<Socket>();
                    arr.add(socket);
                    sockets.put(var1, arr);
                    return;
                }

                sockets.get(var1).add(socket);
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
    public Map<Category, List<Socket>> getSockets() {
        return sockets;
    }

    @Override
    public List<Socket> getSockets(String category) {
        return sockets.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(category))
                .map(Map.Entry::getValue).findFirst().orElse(emptyList());
    }
}
