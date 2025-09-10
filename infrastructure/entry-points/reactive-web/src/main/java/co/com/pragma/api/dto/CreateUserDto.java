package co.com.pragma.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserDto(
        String firstName,
        String lastName,
        String docmumentId,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        BigDecimal salary) {
}
