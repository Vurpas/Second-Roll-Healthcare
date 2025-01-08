package health.care.booking.dto;

import health.care.booking.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Set;

public class RegisterRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Indexed(unique = true)
    @Email
    private String email;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String dateOfBirth;

    private Set<Role> roles;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, Set<Role> roles, String firstName,
                           String lastName, String email, String city, String street, String phoneNumber,
                           String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.street = street;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public @NotBlank  String getFirstName() {
        return firstName;
    }

    public @NotBlank  String getLastName() {
        return lastName;
    }

    public @NotBlank String getEmail() {
        return email;
    }

    public @NotBlank String getCity() {
        return city;
    }

    public @NotBlank String getStreet() {
        return street;
    }

    public @NotBlank String getPhoneNumber() {
        return phoneNumber;
    }

    public @NotBlank String getDateOfBirth() {
        return dateOfBirth;
    }
}
