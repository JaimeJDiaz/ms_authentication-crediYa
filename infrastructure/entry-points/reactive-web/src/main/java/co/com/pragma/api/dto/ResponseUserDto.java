package co.com.pragma.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResponseUserDto(
        BigDecimal id,
        String firstName,
        String lastName,
        String documentId,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        BigDecimal salary) {
}
