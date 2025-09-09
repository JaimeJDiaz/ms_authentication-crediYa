package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

    String lastName;LocalDate birthDate;

    @Column("document_id")
    String documentId;

    String address;

    String phone;

    String email;

    BigDecimal salary;

    Integer role;
}

