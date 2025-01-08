package health.care.booking.controllers;

import health.care.booking.dto.UpdateUserDTO;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository ) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // update user
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO,
                                        @PathVariable("userId") String userId) {
        if (userRepository.existsById((userId))) {

            User updatedUser = userRepository.findUserById(userId);
            updatedUser.setUsername(updateUserDTO.getUsername());
            updatedUser.setFirstName(updateUserDTO.getFirstName());
            updatedUser.setLastName(updateUserDTO.getLastName());
            updatedUser.setEmail(updateUserDTO.getEmail());
            updatedUser.setCity(updateUserDTO.getCity());
            updatedUser.setStreet(updateUserDTO.getStreet());
            updatedUser.setPhoneNumber(updateUserDTO.getPhoneNumber());

            userService.updateUser(updatedUser);
            return ResponseEntity.ok().body("User updated");

        } else {
            return ResponseEntity
                    .badRequest()
                    .body(("user with ID " + userId + " was not found"));
        }
    }
}