package co.com.pragma.api.dto;

public record LoginRequest(
        String email,
        String password
) {
}
