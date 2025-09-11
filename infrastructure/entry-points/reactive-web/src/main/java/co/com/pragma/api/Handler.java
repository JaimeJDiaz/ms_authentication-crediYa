package co.com.pragma.api;

import co.com.pragma.api.dto.LoginRequest;
import co.com.pragma.api.dto.LoginResponse;
import co.com.pragma.api.security.JwtUtil;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final TransactionalOperator transactionalOperator;
    private final JwtUtil jwtUtil;

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

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        Flux<User> userFlux = transactionalOperator.transactional(userUseCase.getAllUsers());
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFlux, User.class);
    }

    public Mono<ServerResponse> listenGetUserByIdentification(ServerRequest serverRequest) {
        String identification = serverRequest.pathVariable("identificacion");
        return transactionalOperator.transactional(userUseCase.getUserByIdentification(identification))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

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