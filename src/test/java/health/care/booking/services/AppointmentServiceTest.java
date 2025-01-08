package health.care.booking.services;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentRepository appointmentRepository;

    private LocalDateTime now = LocalDateTime.now();

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAppointment() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser("Mia", "Mjau", "Kattegatt", Set.of(Role.USER))));
        when(appointmentRepository.save(any())).thenReturn(new Appointment());
        AppointmentRequest appointmentRequest = new AppointmentRequest(now, "123", "23");
        appointmentService.createAppointment(appointmentRequest);

        verify(appointmentRepository, times(1)).save(any());

    }


    // Mock user method
    private static User mockUser (String firstName, String lastName, String username, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("MjauMjau123");
        user.setRoles(roles);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }



}