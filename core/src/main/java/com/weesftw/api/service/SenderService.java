package com.weesftw.api.service;

import com.weesftw.api.model.response.SenderResponse;

public interface SenderService {

    SenderResponse send(String username, String content);

    SenderResponse sendOnJoinLeft(String username, String content, String category);
}
