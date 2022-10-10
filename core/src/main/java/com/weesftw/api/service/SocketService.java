package com.weesftw.api.service;

import com.weesftw.api.model.Category;
import com.weesftw.api.model.Socket;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SocketService {

    Optional<Socket> getSocket(String name);

    boolean removeSocket(String username);

    void addSocket(Socket socket, String category);

    Map<Category, List<Socket>> getSockets();

    List<Socket> getSockets(String category);
}
