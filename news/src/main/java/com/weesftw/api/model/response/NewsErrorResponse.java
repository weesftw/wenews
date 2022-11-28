package com.weesftw.api.model.response;

import com.weesftw.common.context.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Introspected
@AllArgsConstructor
public class NewsErrorResponse {

    private String error;
}
