package com.weesftw.api.model.response;

import com.weesftw.common.context.Introspected;
import lombok.*;

@Getter
@Introspected
@AllArgsConstructor
public class NewsErrorResponse {

    private String error;
}
