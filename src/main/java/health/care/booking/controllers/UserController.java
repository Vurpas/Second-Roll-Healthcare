package health.care.booking.controllers;

import health.care.booking.dto.UpdateUserDTO;
import health.care.booking.exceptions.UserNotFoundException;
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

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // update user
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO,
                                        @PathVariable("userId") String userId) {
        try {
            User updatedUser = userService.updateUser(userId, updateUserDTO);
            return ResponseEntity.ok(updatedUser.getUsername() + " was successfully updated");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("User with ID " + userId + " was not found");
        }
    }
}