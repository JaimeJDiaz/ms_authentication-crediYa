package co.com.pragma.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class UserEntity {
    @Id
    private BigInteger id;
    String firstName;
    String lastName;
    String documentId;
    LocalDate birthDate;
    String address;
    String phone;
    String email;
    String password;
    BigDecimal salary;
    Long roleId;
    String roleName;
}
