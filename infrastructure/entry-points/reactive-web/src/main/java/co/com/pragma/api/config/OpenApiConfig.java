package co.com.pragma.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Management API",
                version = "1.0.0",
                description = "Documentation for the user management services"
        )
)
public class OpenApiConfig {
    @Bean
    public OperationCustomizer customizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (handlerMethod.getBeanType().getName().contains("Handler")) {
                if (operation.getOperationId() == null) {
                    operation.setOperationId(handlerMethod.getMethod().getName());
                }
                if (operation.getTags() == null || operation.getTags().isEmpty()) {
                    operation.addTagsItem("Users");
                }
            }
            return operation;
        };
    }
}