package health.care.booking;

import health.care.booking.dto.UpdateUserDTO;
import health.care.booking.exceptions.UserNotFoundException;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
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

    /**
     * test updateUser to ensure that a user is registered correctly.
     * */
    @Test
    public void TestUpdateUser_Success() {
        // Arrange
        // create a sample User (before update)
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setFirstName("OldFirstName");
        existingUser.setLastName("OldLastName");
        existingUser.setEmail("oldEmail@hotmail.com");
        existingUser.setCity("OldCity");
        existingUser.setStreet("OldStreet");
        existingUser.setPhoneNumber("123456789");

        // create an UpdateUserDTO with the updated information
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("NewFirstName");
        updateUserDTO.setLastName("NewLastName");
        updateUserDTO.setEmail("newEmail@hotmail.com");
        updateUserDTO.setCity("NewCity");
        updateUserDTO.setStreet("NewStreet");
        updateUserDTO.setPhoneNumber("987654321");

        // mock the behavior of userRepository.existsById() and userRepository.findUserById()
        when(userRepository.existsById("1")).thenReturn(true);
        when(userRepository.findUserById("1")).thenReturn(existingUser);

        // mock the behavior of userRepository.save() to return the updated user
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        // call the update user method in UserService
        User result = userService.updateUser("1", updateUserDTO);

        // Assert
        // verify that the updated user has the correct information
        assertNotNull(result.getId(), "Updated user should have an ID.");
        assertEquals("NewFirstName", result.getFirstName(), "first name should be updated.");
        assertEquals("NewLastName", result.getLastName(), "last name should be updated.");
        assertEquals("newEmail@hotmail.com", result.getEmail(), "email should be updated.");
        assertEquals("NewCity", result.getCity(), "city should be updated.");
        assertEquals("NewStreet", result.getStreet(), "street should be updated.");
        assertEquals("987654321", result.getPhoneNumber(), "phone number should be updated.");

        // verify that userRepository.save() was called exactly once with the updated user
        verify(userRepository, times(1)).save(any(User.class));

        // verify that the userRepository.existsById() method was called once
        verify(userRepository, times(1)).existsById("1");

        // verify that the userRepository.findUserById() method was called once
        verify(userRepository, times(1)).findUserById("1");
    }

    /**
     * negative test: updateUser with a non-existent ID, should throw UserNotFoundException
     * */
    @Test
    public void TestUpdateUser_UserNotFound() {
        // Arrange
        // create an UpdateUserDTO with the updated information
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("NewFirstName");
        updateUserDTO.setLastName("NewLastName");
        updateUserDTO.setEmail("newEmail@hotmail.com");
        updateUserDTO.setCity("NewCity");
        updateUserDTO.setStreet("NewStreet");
        updateUserDTO.setPhoneNumber("987654321");

        // mock the behavior of userRepository.existsById() to return false (user not found)
        when(userRepository.existsById("nonExistentId")).thenReturn(false);

        // Act
        // try to update a non-existing user and verify that the exception is thrown
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser("nonExistentId", updateUserDTO);
        });

        // Assert
        // verify that userRepository.existsById() was called once
        verify(userRepository, times(1)).existsById("nonExistentId");

        // ensure that userRepository.save() was not called
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        String userId = "123";

        // Mock the behaviour of the repository
        when(userRepository.existsById(userId)).thenReturn(true);

        // Call the method
        String result = userService.deleteUser(userId);

        // Verify the expected result
        assertEquals("User deleted", result);
        verify(userRepository, times(1)).deleteById(userId);

    }

    @Test
    void testDeleteUser_UserNotFound() {
        String userId = "123";

        // Mock the behaviour of the repository
        when(userRepository.existsById(userId)).thenReturn(false);

        // Call the method and assert that the exception is thrown
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
                userService.deleteUser(userId);
        });

        assertEquals("User with id: 123 was not found.", exception.getMessage());
        verify(userRepository, never()).deleteById(userId); // Ensure delete was never called
    }
}