package com.weesftw.adapter.endpoint.socket;

public enum SocketEvent {

    NEW_NEWS, // send to all users new news when new news is created
    USER_JOINED, // send to all users when a new user joins
    USER_LEFT, // send to all users when a user leaves
    LOAD_NEWS, // send to a user when he joins
    BAN // send to a user when he is banned
}
