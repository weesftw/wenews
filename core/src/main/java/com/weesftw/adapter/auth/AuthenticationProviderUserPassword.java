package com.weesftw.adapter.auth;

import com.weesftw.api.service.UserService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;

import static io.micronaut.security.authentication.AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH;

@Slf4j
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private final UserService userService;

    public AuthenticationProviderUserPassword(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        var identity = (String) authenticationRequest.getIdentity();
        var passwd = (String) authenticationRequest.getSecret();
        var user = userService.credentialsIsValid(identity, passwd);
        log.info("User {} is trying to login.", identity);

        return Flux.create(emitter -> {
            if(user != null) {
                emitter.next(AuthenticationResponse.success(identity, List.of(user.getRole().getName())));
                emitter.complete();
            }
            emitter.next(AuthenticationResponse.failure(CREDENTIALS_DO_NOT_MATCH));
        }, FluxSink.OverflowStrategy.ERROR);
    }
}