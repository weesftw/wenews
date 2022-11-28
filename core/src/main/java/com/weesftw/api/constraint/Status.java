package com.weesftw.api.constraint;

import io.micronaut.core.annotation.Introspected;

import static io.micronaut.core.util.StringUtils.capitalize;

@Introspected
public enum Status {

    ONLINE, AWAY, OFFLINE;

    public String getId() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return capitalize(getId());
    }
}
