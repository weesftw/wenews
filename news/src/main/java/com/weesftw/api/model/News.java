package com.weesftw.api.model;

import com.weesftw.common.context.Introspected;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class News {

    @EqualsAndHashCode.Exclude
    private String id;
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String image;
    private String category;
}
