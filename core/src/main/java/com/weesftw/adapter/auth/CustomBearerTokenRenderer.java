package com.weesftw.adapter.auth;

import com.weesftw.adapter.auth.model.CustomBearerAccessRefreshToken;
import com.weesftw.api.service.UserService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;

@Primary
public class CustomBearerTokenRenderer extends BearerTokenRenderer {

    private static final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;

    private final UserService userService;

    public CustomBearerTokenRenderer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public AccessRefreshToken render(Authentication authentication, Integer expiresIn, String accessToken, @Nullable String refreshToken) {
        var username = authentication.getName();
        var user = userService.getUser(username);
        return new CustomBearerAccessRefreshToken(user.isBanned(), user.getImageUrl(), user.getUsername(), authentication.getRoles(), expiresIn, accessToken, refreshToken, BEARER_TOKEN_TYPE);
    }
}
