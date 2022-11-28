package com.weesftw.adapter.auth.model;

import com.weesftw.common.context.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import lombok.Getter;

import java.util.Collection;

@Getter
@Introspected
public class CustomBearerAccessRefreshToken extends BearerAccessRefreshToken {

    private final String imageUrl;
    private final boolean banned;

    public CustomBearerAccessRefreshToken(boolean banned, String imageUrl, String username, @Nullable Collection<String> roles, @Nullable Integer expiresIn, String accessToken, @Nullable String refreshToken, String tokenType) {
        super(username, roles, expiresIn, accessToken, refreshToken, tokenType);
        this.imageUrl = imageUrl;
        this.banned = banned;
    }
}
