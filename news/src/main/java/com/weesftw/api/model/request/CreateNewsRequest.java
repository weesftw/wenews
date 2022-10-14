package com.weesftw.api.model.request;

import com.weesftw.common.context.Introspected;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class CreateNewsRequest {

    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Size(min = 5, message = "Description must be at least 5 characters long")
    private String description;

    @NotNull(message = "Link must not be null")
    private String link;

    @NotNull(message = "PubDate must not be null")
    private String pubDate;

    @NotNull(message = "Image must not be null")
    private String image;

    @NotNull(message = "Category must not be null")
    private String category;
}
