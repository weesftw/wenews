package com.weesftw.api.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Sender {

    private User user;
    private List<Message> messages;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class Message {

        private String content;
        private LocalDateTime dateTime;

        public Message(String content) {
            this.content = content;
            this.dateTime = LocalDateTime.now();
        }
    }
}
