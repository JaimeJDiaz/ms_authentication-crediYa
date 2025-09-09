package co.com.pragma.api;

import co.com.pragma.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.math.BigInteger;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @RouterOperations({
            @RouterOperation(path = "/api/v1/usuarios", produces = "application/json", method = RequestMethod.POST,
                    beanClass = Handler.class, beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Guarda un nuevo usuario",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = User.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario guardado exitosamente",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "409", description = "El email ya existe")
                            }
                    )
            ),
            @RouterOperation(path = "/api/v1/usuarios/update", produces = "application/json", method = RequestMethod.POST,
                    beanClass = Handler.class, beanMethod = "listenUpdateUser",
                    operation = @Operation(
                            operationId = "updateUser",
                            summary = "Actualiza un usuario existente",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = User.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            ),
            @RouterOperation(path = "/api/v1/usuarios/{id}", produces = "application/json", method = RequestMethod.GET,
                    beanClass = Handler.class, beanMethod = "listenGetUser",
                    operation = @Operation(
                            operationId = "getUserById",
                            summary = "Obtiene un usuario por su ID",
                            tags = {"Usuarios"},
                            parameters = @Parameter(name = "id", description = "ID del usuario", required = true, schema = @Schema(implementation = BigInteger.class)),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            ),
            @RouterOperation(path = "/api/v1/usuarios", produces = "application/json", method = RequestMethod.GET,
                    beanClass = Handler.class, beanMethod = "listenGetAllUsers",
                    operation = @Operation(
                            operationId = "getAllUsers",
                            summary = "Obtiene todos los usuarios",
                            tags = {"Usuarios"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                                            content = @Content(schema = @Schema(implementation = User[].class)))
                            }
                    )
            ),
            @RouterOperation(path = "/api/v1/usuarios/{id}", produces = "application/json", method = RequestMethod.DELETE,
                    beanClass = Handler.class, beanMethod = "listenDeleteUser",
                    operation = @Operation(
                            operationId = "deleteUser",
                            summary = "Elimina un usuario por su ID",
                            tags = {"Usuarios"},
                            parameters = @Parameter(name = "id", description = "ID del usuario a eliminar", required = true, schema = @Schema(implementation = BigInteger.class)),
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            ),
            @RouterOperation(path = "/api/v1/usuarios/documento/{documentId}", produces = "application/json", method = RequestMethod.GET,
                    beanClass = Handler.class, beanMethod = "listenGetUserByDocumentId",
                    operation = @Operation(
                            operationId = "getUserByDocumentId",
                            summary = "Obtiene un usuario por su documento",
                            tags = {"Usuarios"},
                            parameters = @Parameter(name = "documentId", description = "Documento del usuario", required = true, schema = @Schema(implementation = String.class)),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            )
    })

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route()
                .POST("/api/v1/usuarios", handler::listenSaveUser)
                .POST("/api/v1/usuarios/update", handler::listenUpdateUser)
                .GET("/api/v1/usuarios/{id}", handler::listenGetUser)
                .GET("/api/v1/usuarios", handler::listenGetAllUsers)
                .DELETE("/api/v1/usuarios/{id}", handler::listenDeleteUser)
                .GET("/api/v1/usuarios/documento/{documentId}", handler::listenGetUserByDocumentId)
                .build();
    }
}
