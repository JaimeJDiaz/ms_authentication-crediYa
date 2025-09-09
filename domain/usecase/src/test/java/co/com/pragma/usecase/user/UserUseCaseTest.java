package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.LogPort;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.UserEmailAlreadyExistsException;
import co.com.pragma.usecase.user.exceptions.UserNotFoundException;
import co.com.pragma.usecase.user.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserValidator validator;

    @Mock
    private LogPort logPort;

    @InjectMocks
    private UserUseCase useCase;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setFirstName("Jaime");
        sampleUser.setLastName("Díaz");
        sampleUser.setBirthDate(LocalDate.of(2001, 9, 12));
        sampleUser.setAddress("My address");
        sampleUser.setPhone("1234567890");
        sampleUser.setEmail("jaime@example.com");
        sampleUser.setSalary(new BigDecimal("5000000"));
    }

    @Test
    void shouldSaveUserWhenEmailIsNew() {
        doNothing().when(validator).validateUser(sampleUser);
        when(repository.findByEmail("jaime@example.com")).thenReturn(Mono.empty());
        when(repository.saveUser(sampleUser)).thenReturn(Mono.just(sampleUser));

        StepVerifier.create(useCase.saveUser(sampleUser))
                .expectNext(sampleUser)
                .verifyComplete();

        verify(validator).validateUser(sampleUser);
        verify(repository).findByEmail("jaime@example.com");
        verify(repository).saveUser(sampleUser);
    }

    @Test
    void shouldFailToSaveUserWhenEmailExists() {
        when(repository.findByEmail(sampleUser.getEmail()))
                .thenReturn(Mono.just(sampleUser)); // Simula que ya existe

        doNothing().when(validator).validateUser(sampleUser);

        StepVerifier.create(useCase.saveUser(sampleUser))
                .expectError(UserEmailAlreadyExistsException.class)
                .verify();

        verify(repository).findByEmail(sampleUser.getEmail());
        verify(repository, never()).saveUser(sampleUser);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User userToUpdate = new User(BigInteger.TWO,"Jaime", "Diaz", "12365478", LocalDate.of(2002, 10, 5), "my address", "1234567890", "asd@asdf.com", BigDecimal.TEN, 2);
        User updatedUser = new User(BigInteger.TWO,"Jaime Updated", "Diaz", "12365478", LocalDate.of(2002, 10, 5), "my address", "1234567890", "asd@asdf.com", BigDecimal.TEN, 2);

        doNothing().when(validator).validateUser(any(User.class));
        when(repository.findById(any(BigInteger.class))).thenReturn(Mono.just(sampleUser));
        when(repository.update(any(User.class))).thenReturn(Mono.just(updatedUser));

        StepVerifier.create(useCase.updateUser(userToUpdate))
                .expectNext(updatedUser)
                .verifyComplete();
    }

    @Test
    void shouldFailImmediatelyWhenValidationThrowsException() {
        List<String> errors = List.of("First name is required");
        ValidationException exception = new ValidationException(errors);
        User invalidUser = new User();
        doThrow(exception).when(validator).validateUser(invalidUser);


        StepVerifier.create(useCase.saveUser(invalidUser))
                .expectError(ValidationException.class)
                .verify();

        verify(repository, never()).findByEmail(any());
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(
                new User(BigInteger.ONE, "Jose", "Diaz", "12365478", LocalDate.of(2002, 10, 5), "my address", "1234567890", "aqwe@asdf.com", BigDecimal.TEN, 2),
                new User(BigInteger.TWO,"Jaime", "Diaz", "12365478", LocalDate.of(2002, 10, 5), "my address", "1234567890", "asd@asdf.com", BigDecimal.TEN, 2)
        );

        when(repository.findAll()).thenReturn(Flux.fromIterable(users));

        StepVerifier.create(useCase.getAllUsers())
                .expectNextSequence(users)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNoUsersExist() {
        when(repository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.getAllUsers())
                .verifyComplete();
    }

    @Test
    void shouldReturnUserWhenEmailExists() {
        when(repository.findByEmail(sampleUser.getEmail())).thenReturn(Mono.just(sampleUser));

        StepVerifier.create(useCase.getUserByEmail(sampleUser.getEmail()))
                .expectNext(sampleUser)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";
        when(repository.findByEmail(anyString())).thenReturn(Mono.empty());

        // Act & Assert
        // El StepVerifier ahora espera que se lance una excepción
        StepVerifier.create(useCase.getUserByEmail(email))
                .expectError(UserNotFoundException.class)
                .verify();
    }


}