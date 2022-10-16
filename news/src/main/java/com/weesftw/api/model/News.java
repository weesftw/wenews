package com.weesftw.api.model;

import com.weesftw.common.context.Introspected;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class News {

    @EqualsAndHashCode.Exclude
    private String id;
    private String title;
    private String description;
    private String author;
    private String url;
    private LocalDateTime publishedAt;
    private String imageToUrl;
    private String category;
}
