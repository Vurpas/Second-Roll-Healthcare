package health.care.booking.services;

import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService();
        userDetailsService.setUserRepository(userRepository); // Set the repository in the service
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        String username = "testUser";
        Role userRole = Role.USER;

        // Create a set with a single role
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        //Create a user with a single role (USER)
        User user = new User(username, "password123", roles, "Janne", "Jannesson", "janne.jannesson@jan.se", "Janstad", "Jannegatan 1", "0701234567", "20220710");

        // Mock the user repository to return the user
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert that the UserDetails object is not null and matches expected values
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());

        // Assert that the authority is correctly assigned
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + userRole.name())));

        // Print authorities for debugging
        System.out.println("Authorities: " + userDetails.getAuthorities());
    }

    @Test
    void testLoadUserByUsername_Failure_UserNotFound() {

        // Arrange
        String username = "nonExistentUser";

        // Mock the user repository to return an empty Optional, simulating a user not found
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        // Verify that the UsernamNotFoundException is thrown when a non-existent user is requested
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        }, " Expected UsernameNotFoundException to be thrown, but it wasn´t");
    }
}



