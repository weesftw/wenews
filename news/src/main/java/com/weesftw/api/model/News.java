package com.weesftw.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class News {

    private String id;
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String image;
    private String category;
}
