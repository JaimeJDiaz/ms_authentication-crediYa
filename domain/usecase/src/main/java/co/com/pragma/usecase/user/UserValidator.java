package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static co.com.pragma.usecase.user.LogMessages.*;


public class UserValidator {

    private static final Logger log = LoggerFactory.getLogger(UserValidator.class);

    private static final BigDecimal MAXSALARY = new BigDecimal("15000000");
    private static final BigDecimal MINSALARY = BigDecimal.ZERO;

    static List<String> errors = new ArrayList<>();

    public static void validateUser(User user) {
        log.info(VALIDATING_USER);

        if (user == null) {
            log.warn(USER_IS_NULL);
            errors.add(USER_NULL);
        } else {
            validateName(user.getFirstName(), user.getLastName());

            validateBirthDate(user.getBirthDate());

            validateAddressAndPhone(user.getAddress(),  user.getPhone());

            validateEmail(user.getEmail());

            validateSalary(user.getSalary());
        }

        if (!errors.isEmpty()) {
            log.error(USER_VALIDATION_FAILED, errors.size(), errors);
            throw new ValidationException(errors);
        }

        log.info(USER_VALIDATION_PASSED);
    }

    private static void validateName(String firstName, String lastName) {
        if (firstName == null || firstName.isEmpty()) {
            log.warn(FIRST_NAME_BLANK);
            errors.add(FIRST_NAME_REQUIRED);
        }
        if (lastName == null || lastName.isEmpty()) {
            log.warn(LAST_NAME_BLANK);
        }
    }

    private static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            log.warn(BIRTH_DATE_REQUIRED);
            errors.add(BIRTH_DATE_REQUIRED);
        }
    }

    private static void validateAddressAndPhone(String address, String phone) {
        if (address == null || address.isBlank()) {
            log.warn(ADDRESS_BLANK);
            errors.add(ADDRESS_REQUIRED);
        }
        if (phone == null || phone.isBlank()) {
            log.warn(PHONE_BLANK);
            errors.add(PHONE_REQUIRED);
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            log.warn(EMAIL_REQUIRED);
            errors.add(EMAIL_REQUIRED);
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            log.warn(EMAIL_INVALID);
            errors.add(EMAIL_INVALID);
        }
    }

    private static void validateSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(MINSALARY) < 0 || salary.compareTo(MAXSALARY) > 0) {
            log.warn(SALARY_OUT_OF_RANGE);
            errors.add(SALARY_OUT_OF_RANGE);
        }
    }

}
