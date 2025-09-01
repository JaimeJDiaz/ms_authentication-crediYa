package co.com.pragma.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserDto(String firstName, String lastName, LocalDate birthDate, String address, String phone, String email, BigDecimal salary) {
}
