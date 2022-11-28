package com.weesftw.api.model;

import com.weesftw.common.context.Introspected;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class News {

    private String id;
    private String title;
    private String description;
    private String author;
    private String url;
    private String publishedAt;
    private String urlToImage;
    private String category;
}
