package com.weesftw.api.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class SenderResponse {

    private String username;
    private String content;
    private LocalDateTime dateTime;
    private List<String> connectedUsers;
}
