package health.care.booking.services;


import health.care.booking.dto.UpdateUserDTO;
import health.care.booking.exceptions.EmailNotFoundException;
import health.care.booking.exceptions.UserNotFoundException;
import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // hash the password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // ensure the user has at least the default role
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }

        return userRepository.save(user);
    }

    // update user
    public User updateUser(String userId, UpdateUserDTO updateUserDTO) {

        if (userRepository.existsById(userId)) {
        User updatedUser = userRepository.findUserById(userId);

        updatedUser.setFirstName(updateUserDTO.getFirstName());
        updatedUser.setLastName(updateUserDTO.getLastName());
        updatedUser.setEmail(updateUserDTO.getEmail());
        updatedUser.setCity(updateUserDTO.getCity());
        updatedUser.setStreet(updateUserDTO.getStreet());
        updatedUser.setPhoneNumber(updateUserDTO.getPhoneNumber());

        return userRepository.save(updatedUser);
        } else {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
    }

    // delete user
    public String deleteUser(String userId) {
        if(!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id: " + userId + " was not found.");
        }
        userRepository.deleteById(userId);
        return "User deleted";
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}