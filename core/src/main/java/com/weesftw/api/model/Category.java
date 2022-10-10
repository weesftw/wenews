package com.weesftw.api.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@Introspected
@EqualsAndHashCode
public class Category {

    private int id;
    private String name;
    private String password;
    private boolean isPublic;
    private LocalDateTime createdAt;

    private int count;
}
