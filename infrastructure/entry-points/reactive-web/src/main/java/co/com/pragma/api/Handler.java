package co.com.pragma.api;

import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> listenUpdateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(userUseCase::updateUser)
                .flatMap(updatedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedUser));
    }

    public Mono<ServerResponse> listenGetUser(ServerRequest serverRequest) {
        try {
            BigInteger userId = new BigInteger(serverRequest.pathVariable("id"));
            return userUseCase.getUser(userId)
                    .flatMap(user -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(user))
                    .switchIfEmpty(ServerResponse.notFound().build());
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("ID inválido");
        }
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> listenDeleteUser(ServerRequest serverRequest) {
        return Mono.just(serverRequest.pathVariable("id"))
                .map(BigInteger::new)
                .flatMap(userUseCase::deleteUser)
                .then(ServerResponse.noContent().build());
    }
}