package health.care.booking.services;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentRequest validAppointmentRequest;
    private AppointmentRequest invalidAppointmentRequest;

    private Availability availability;
    private User patient;

    private User caregiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup valid AppointmentRequest with available date
        validAppointmentRequest = new AppointmentRequest(LocalDateTime.of(2025, 1, 20, 10, 0), "100", "1");
        validAppointmentRequest.setAvailabilityId("1");
        validAppointmentRequest.setPatientId("100");
        validAppointmentRequest.setAppointmentDate(LocalDateTime.of(2025, 1, 20, 10, 0));

        // Setup Invalid AppointmentRequest (non-existent availability)
        invalidAppointmentRequest = new AppointmentRequest(LocalDateTime.of(2025, 1, 20, 10, 0), "100","999");
        invalidAppointmentRequest.setAvailabilityId("999"); // Invalid ID
        invalidAppointmentRequest.setPatientId("100");
        invalidAppointmentRequest.setAppointmentDate(LocalDateTime.of(2025, 1, 20, 10, 0));

        // Mock the Availability and User repositories
        availability = new Availability();
        availability.setId("1");
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(List.of(LocalDateTime.of(2025, 1, 20, 10, 0), LocalDateTime.of(2025, 1, 20, 11, 0)));

        patient = new User();
        patient.setId("100");

        caregiver = new User();
        caregiver.setId("100");

        when(availabilityRepository.findById("1")).thenReturn(Optional.of(availability));
        when(userRepository.findById("100")).thenReturn(Optional.of(patient));
    }

    @Test
    void testCreateAppointmentSuccess() {
        // Arrange: Mock the saving of the appointment
        Appointment createdAppointment = new Appointment();
        createdAppointment.setCaregiverId(caregiver);
        createdAppointment.setDateTime(validAppointmentRequest.getAppointmentDate());
        createdAppointment.setStatus(Status.SCHEDULED);
        createdAppointment.setPatientId(patient);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(createdAppointment);

        // Act: Create the appointment
        Appointment appointment = appointmentService.createAppointment(validAppointmentRequest);

        // Assert: Check if the appointment was successfully created
        assertNotNull(appointment);
        assertEquals(validAppointmentRequest.getAppointmentDate(), appointment.getDateTime());
        assertEquals(Status.SCHEDULED, appointment.getStatus());
        assertEquals(patient, appointment.getPatientId());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
        verify(availabilityRepository, times(1)).save(any(Availability.class)); // Verify availibility was updated
    }
        @Test
        void testCreateAppointmentFailureWithNonExistentPatient() {
            // Mock the case where the patient doesn´t exist
        when(userRepository.findById("100")).thenReturn(Optional.empty());

        // Act & Assert: Attempt to create the appointment and expect an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.createAppointment(validAppointmentRequest));
        assertEquals("Patient with ID 100 not found.", exception.getMessage());
        }

        @Test
        void testCreateAppointmentFailureWithInvalidDate() {
            // Arrange: Use an invalid date that is not in the availability slots
            validAppointmentRequest.setAppointmentDate(LocalDateTime.of(2025, 1, 20, 12, 0));

            // Act & Assert: Attempt to create the appointment and expect an exception
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    appointmentService.createAppointment(validAppointmentRequest));
            assertEquals("The specified date does not exist", exception.getMessage());
        }
    }

