package co.com.pragma.usecase.user;

public class LogMessages {
    // UserUseCase
    public static final String SAVING_USER = "Attempting to save user with email: {}";
    public static final String USER_ALREADY_EXISTS = "User with email {} already exists";
    public static final String USER_SAVED = "User saved successfully";
    public static final String ERROR_SAVING_USER = "Error saving user: {}";

    public static final String UPDATING_USER = "Updating user";
    public static final String USER_UPDATED = "User updated successfully";
    public static final String ERROR_UPDATING_USER = "Error updating user: {}";

    public static final String DELETING_USER = "Deleting user with ID: {}";
    public static final String USER_DELETED = "User deleted successfully: {}";
    public static final String ERROR_DELETING_USER = "Error deleting user with ID {}: {}";

    public static final String FETCHING_USER_BY_ID = "Fetching user by ID: {}";
    public static final String USER_FOUND = "User found: {}";
    public static final String ERROR_FETCHING_USER_BY_ID = "Error fetching user by ID {}: {}";

    public static final String FETCHING_USER_BY_EMAIL = "Fetching user by email: {}";
    public static final String ERROR_FETCHING_USER_BY_EMAIL = "Error fetching user by email {}: {}";

    public static final String FETCHING_ALL_USERS = "Fetching all users";
    public static final String ALL_USERS_FETCHED = "All users fetched successfully";
    public static final String ERROR_FETCHING_ALL_USERS = "Error fetching all users: {}";

    // UserValidator
    public static final String VALIDATING_USER = "Starting user validation";
    public static final String USER_VALIDATION_PASSED = "User validation passed";
    public static final String USER_VALIDATION_FAILED = "User validation failed with {} error(s): {}";

    public static final String USER_NULL = "User object must not be null";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String BIRTH_DATE_REQUIRED = "Birth date is required";
    public static final String ADDRESS_REQUIRED = "Address is required";
    public static final String PHONE_REQUIRED = "Phone is required";
    public static final String EMAIL_REQUIRED = "Valid email is required";
    public static final String DOCUMENT_ID_REQUIRED = "Document ID is required";

    public static final String FIRST_NAME_BLANK = "Validation failed: first name is blank";
    public static final String LAST_NAME_BLANK = "Validation failed: last name is blank";
    public static final String BIRTH_DATE_NULL = "Validation failed: birth date is null";
    public static final String ADDRESS_BLANK = "Validation failed: address is blank";
    public static final String PHONE_BLANK = "Validation failed: phone is blank";
    public static final String EMAIL_INVALID = "Validation failed: email is invalid";
    public static final String SALARY_OUT_OF_RANGE = "Validation failed: salary out of range ({})";
    public static final String USER_IS_NULL = "Validation failed: user is null";


    private LogMessages() {
        // Prevent instantiation
    }
}
