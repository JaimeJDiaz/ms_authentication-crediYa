package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.exceptions.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static co.com.pragma.usecase.user.LogMessages.*;


public class UserValidator {

   private static final BigDecimal MAXSALARY = new BigDecimal("15000000");
    private static final BigDecimal MINSALARY = BigDecimal.ZERO;

    static List<String> errors = new ArrayList<>();

    public static void validateUser(User user) {
        if (user == null) {
            errors.add(USER_NULL);
        } else {
            validateName(user.getFirstName(), user.getLastName());
            validateBirthDate(user.getBirthDate());
            validateAddressAndPhone(user.getAddress(),  user.getPhone());
            validateEmail(user.getEmail());
            validateSalary(user.getSalary());
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private static void validateName(String firstName, String lastName) {
        if (firstName == null || firstName.isEmpty()) {
            errors.add(FIRST_NAME_REQUIRED);
        }
        if (lastName == null || lastName.isEmpty()) {
            errors.add(LAST_NAME_REQUIRED);
        }
    }

    private static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            errors.add(BIRTH_DATE_REQUIRED);
        }
    }

    private static void validateAddressAndPhone(String address, String phone) {
        if (address == null || address.isBlank()) {
           errors.add(ADDRESS_REQUIRED);
        }
        if (phone == null || phone.isBlank()) {
            errors.add(PHONE_REQUIRED);
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            errors.add(EMAIL_REQUIRED);
        } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            errors.add(EMAIL_INVALID);
        }
    }

    private static void validateSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(MINSALARY) < 0 || salary.compareTo(MAXSALARY) > 0) {
            errors.add(SALARY_OUT_OF_RANGE);
        }
    }
}
