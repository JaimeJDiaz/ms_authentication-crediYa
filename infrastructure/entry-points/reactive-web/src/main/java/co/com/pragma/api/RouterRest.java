package co.com.pragma.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/usuarios", handler::listenSaveUser)
                .POST("/api/v1/usuarios/update", handler::listenUpdateUser)
                .GET("/api/v1/usuarios/{id}", handler::listenGetUser)
                .GET("/api/v1/usuarios", handler::listenGetAllUsers)
                .GET("/api/v1/usuarios/identificacion/{identificacion}", handler::listenGetUserByIdentification)
                .POST("/api/v1/usuarios/login", handler::listenLoginUser)
                .build();
    }
}

