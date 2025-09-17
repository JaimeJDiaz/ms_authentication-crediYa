package co.com.pragma.api;

import co.com.pragma.api.dto.LoginRequest;
import co.com.pragma.api.dto.LoginResponse;
import co.com.pragma.api.security.JwtUtil;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@PreAuthorize("denyAll()")
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final TransactionalOperator transactionalOperator;
    private final JwtUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(user -> transactionalOperator.transactional(userUseCase.saveUser(user)))
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> listenUpdateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(user -> transactionalOperator.transactional(userUseCase.updateUser(user)))
                .flatMap(updatedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedUser));
    }

    public Mono<ServerResponse> listenGetUser(ServerRequest serverRequest) {
        try {
            BigInteger userId = new BigInteger(serverRequest.pathVariable("id"));
            return transactionalOperator.transactional(userUseCase.getUser(userId))
                    .flatMap(user -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(user))
                    .switchIfEmpty(ServerResponse.notFound().build());
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("ID inválid");
        }
    }

    public Mono<ServerResponse> listenGetUserByDocumentId(ServerRequest serverRequest) {
        String documentId = serverRequest.pathVariable("documentId");
        return transactionalOperator.transactional(userUseCase.getUserByIdentification(documentId))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        Flux<User> userFlux = transactionalOperator.transactional(userUseCase.getAllUsers());
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFlux, User.class);
    }

    @PreAuthorize("hasRole('ROLE_INTERNAL_SERVICE')")
    public Mono<ServerResponse> listenGetUserByIdentification(ServerRequest serverRequest) {
        String identification = serverRequest.pathVariable("identificacion");
        log.info("[listenGetUserByIdentification] Request recibida para identificacion: {}", identification);
        return transactionalOperator.transactional(userUseCase.getUserByIdentification(identification))
                .doOnNext(user -> log.info("[listenGetUserByIdentification] Usuario encontrado para identificacion: {}", identification))
                .doOnError(e -> log.error("[listenGetUserByIdentification] Error al buscar usuario para identificacion: {} - {}", identification, e.getMessage()))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PreAuthorize("permitAll()")
    public Mono<ServerResponse> listenLoginUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(loginRequest -> transactionalOperator.transactional(
                                userUseCase.login(loginRequest.email(), loginRequest.password()))
                        .flatMap(user -> {
                            String token = jwtUtil.generateToken(user); // 👈 Genera el JWT
                            LoginResponse response = new LoginResponse(token);
                            return ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(response);
                        }))
                .switchIfEmpty(ServerResponse.status(401).bodyValue("Credenciales inválidas"));
    }

}