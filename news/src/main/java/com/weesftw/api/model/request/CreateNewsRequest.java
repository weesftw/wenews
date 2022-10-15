package com.weesftw.api.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.weesftw.common.context.Introspected;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@Introspected
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class CreateNewsRequest {

    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Size(min = 5, message = "Description must be at least 5 characters long")
    private String description;

    @NotNull(message = "Author must not be null")
    private String author;

    @NotNull(message = "Link must not be null")
    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "America/Sao_Paulo")
    @NotNull(message = "publishedAt must not be null")
    private LocalDateTime publishedAt;

    @NotNull(message = "urlToImage must not be null")
    private String urlToImage;

    @NotNull(message = "Category must not be null")
    private String category;
}
