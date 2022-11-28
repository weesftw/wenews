package com.weesftw.api.model;

import com.weesftw.common.context.Introspected;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Introspected
@EqualsAndHashCode
public class News {

    private String id;
    private String title;
    private String description;
    private String author;
    private String url;
    private LocalDateTime publishedAt;
    private String urlToImage;
    private String category;
}
