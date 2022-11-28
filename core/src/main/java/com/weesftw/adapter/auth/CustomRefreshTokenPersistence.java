package com.weesftw.adapter.auth;

import com.weesftw.api.exception.PersistenceException;
import com.weesftw.api.model.Token;
import com.weesftw.api.repository.TokenRepository;
import com.weesftw.api.repository.UserRepository;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.sql.SQLException;

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT;

@Slf4j
@Singleton
public class CustomRefreshTokenPersistence implements RefreshTokenPersistence {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public CustomRefreshTokenPersistence(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void persistToken(RefreshTokenGeneratedEvent event) {
        if (event != null &&
                event.getRefreshToken() != null &&
                event.getAuthentication() != null &&
                event.getAuthentication().getName() != null) {
            String payload = event.getRefreshToken();
            try {
                tokenRepository.save(Token.builder()
                        .user(userRepository.get(event.getAuthentication().getName()))
                        .refreshToken(payload)
                        .revoked(false)
                        .build());
            } catch (SQLException e) {
                throw new PersistenceException(e);
            }
        }
    }

    @Override
    public Publisher<Authentication> getAuthentication(String refreshToken) {
        return Flux.create(emitter -> {
            try {
                log.info("[Auth] refreshToken: {}", refreshToken);
                var token = tokenRepository.get(refreshToken);
                if (token != null) {
                    if (token.isRevoked()) {
                        emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null));
                    } else {
                        emitter.next(Authentication.build(token.getUser().getUsername()));
                        emitter.complete();
                    }
                } else {
                    emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null));
                }
            } catch (SQLException e) {
                emitter.error(new RuntimeException(e));
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}