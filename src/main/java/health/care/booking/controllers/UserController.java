package health.care.booking.controllers;

import health.care.booking.dto.UpdateUserDTO;
import health.care.booking.exceptions.UserNotFoundException;
import health.care.booking.models.User;
import health.care.booking.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // update user
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUser
    (@RequestBody @Valid UpdateUserDTO updateUserDTO, @PathVariable("userId") String userId) {
        try {
            User updatedUser = userService.updateUser(userId, updateUserDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("User with ID " + userId + " was not found");
        }
    }

    // delete user
    @RequestMapping(value = "/{userId}/delete", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }
}