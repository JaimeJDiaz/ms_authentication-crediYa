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

    public void validateUser(User user) {
        List<String> errors = new ArrayList<>();
        if (user == null) {
            errors.add(USER_NULL);
        } else {
            if (validateFirstName(user.getFirstName())) errors.add(FIRST_NAME_REQUIRED);
            if (validateLastName(user.getLastName())) errors.add(LAST_NAME_REQUIRED);
            if (validateBirthDate(user.getBirthDate())) errors.add(BIRTH_DATE_REQUIRED);
            if (validateAddress(user.getAddress())) errors.add(ADDRESS_REQUIRED);
            if (validatePhone(user.getPhone())) errors.add(PHONE_REQUIRED);
            if (validateEmailNotNull(user.getEmail())) errors.add(EMAIL_REQUIRED);
            if (validateEmail(user.getEmail())) errors.add(EMAIL_INVALID);
            if (validateSalary(user.getSalary())) errors.add(SALARY_OUT_OF_RANGE);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private boolean validateFirstName(String firstName) {
        return (firstName == null || firstName.isEmpty());
    }

    private boolean validateLastName(String lastName){
        return (lastName == null || lastName.isEmpty());
    }

    private boolean validateBirthDate(LocalDate birthDate) {
        return (birthDate == null);
    }

    private boolean validateAddress(String address) {
        return (address == null || address.isBlank());
    }

    private boolean validatePhone(String phone) {
        return (phone == null || phone.isBlank());
    }

    private boolean validateEmailNotNull(String email) {
        return (email == null || email.isBlank());
    }

    private boolean validateEmail(String email) {
        return (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
    }

    private boolean validateSalary(BigDecimal salary) {
        return (salary == null || salary.compareTo(MINSALARY) < 0 || salary.compareTo(MAXSALARY) > 0);
    }
}
