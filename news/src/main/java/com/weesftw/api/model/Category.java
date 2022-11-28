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
public class Category {

    private int id;
    private String name;
    private String password;
    private boolean isPublic;
    private boolean enabled;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
