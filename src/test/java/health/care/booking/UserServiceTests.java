package health.care.booking;

import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    // mock user repository
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // inject mocks into user service
    @InjectMocks
    private UserService userService;

    // @BeforeEach ensures that this initial method is automatically run before each test method
    // this makes sure every test is started with the same base conditions, basically "cleans up" before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * test registerUser to ensure that a user is registered correctly.
     * */
    @Test
    public void TestRegisterUser_Success() {
        // Arrange
        // create sample User
        User user = new User();
        user.setUsername("TestUser");

        // set and encode mock password
        user.setPassword("12345678");
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setFirstName("Test");
        user.setLastName("Testsson");
        user.setEmail("testmail@hotmail.com");
        user.setCity("TestCity");
        user.setStreet("TestStreet");
        user.setDateOfBirth("00-00-00");

        // user that represents the saved user in db
        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setUsername(user.getUsername());
        savedUser.setPassword(user.getPassword());
        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        savedUser.setEmail(user.getEmail());
        savedUser.setCity(user.getCity());
        savedUser.setStreet(user.getStreet());
        savedUser.setDateOfBirth(user.getDateOfBirth());

        // mock the behaviour of userRepository.save() and return the saved user
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        // call the register user method in UserService
        User result = userService.registerUser(user);

        // Assert
        // verify that the user was saved correctly
        assertNotNull(result.getId(), "Saved user should have an ID.");
        assertEquals("TestUser", result.getUsername(), "username should match.");
        assertEquals(encodedPassword, result.getPassword(), "password should match.");
        assertEquals("Test", result.getFirstName(), "first name should match.");
        assertEquals("Testsson", result.getLastName(), "last name should match.");
        assertEquals("testmail@hotmail.com", result.getEmail(), "email should match.");
        assertEquals("TestCity", result.getCity(), "city should match.");
        assertEquals("TestStreet", result.getStreet(), "street should match.");
        assertEquals("00-00-00", result.getDateOfBirth(), "date of birth should match.");

        // verify that userRepository.save() only gets called once with a User object
        verify(userRepository, times(1)).save(any(User.class));
    }
}