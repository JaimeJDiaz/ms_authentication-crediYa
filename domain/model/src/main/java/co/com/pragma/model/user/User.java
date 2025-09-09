package co.com.pragma.model.user;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class User {
    private BigInteger id;
    private String firstName;
    private String lastName;
    private String documentId;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private BigDecimal salary;
    private Integer role;
}
