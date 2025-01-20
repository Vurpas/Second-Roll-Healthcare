package health.care.booking;

import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.models.Role;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static health.care.booking.models.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTests {
    //mocka repository
    @Mock
    private UserRepository userRepository;
    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * test createAvailability method to ensure that availabilty is created correct
     * */
    @Test
    public void testCreateAvailability_Success() {
        // arrange
        // create sample availability data for user and availabilitySlots

        // example caregiverId
        String caregiverId = "12345";
        // mock availableSlots
        List<LocalDateTime> availableSlots = Arrays.asList(
                LocalDateTime.of(2025,1,13,10,0),
                LocalDateTime.of(2025,1,13,13,0)
        );

        // creating a mock caregiver as a User object
        User mockCareGiver = new User();
        mockCareGiver.setId(caregiverId);

        // creating a mock availability object
        Availability mockAvailability = new Availability();
        mockAvailability.setCaregiverId(mockCareGiver);
        mockAvailability.setAvailableSlots(availableSlots);

        // mocking the behavior of userRepository.findById and return a mock caregiver
        when(userRepository.findById(caregiverId)).thenReturn(Optional.of(mockCareGiver));
        // mocking the behavior of availabilityRepository.save and return the mock availability
        when(availabilityRepository.save(any(Availability.class))).thenReturn(mockAvailability);


        // act
        // calling the method during test
        Availability result = availabilityService.createAvailability(caregiverId, availableSlots);

        // assert
        // verify the results
        // check that the result is not null
        assertNotNull(result);
        // check that caregiverId has a match
        assertEquals(caregiverId, result.getCaregiverId().getId());
        // check that availableSlots match
        assertEquals(availableSlots, result.getAvailableSlots());

        // verify that the findById method is called just one time with correct caregiverId
        verify(userRepository, times(1)).findById(caregiverId);
        // verify that the save method is called just one time with any availability object
        verify(availabilityRepository, times(1)).save(any(Availability.class));
    }

    @Test
    public void testCreateAvailability_CaregiverNotFound() {
        // arrange
        // setting up test data

        //non-existing caregiverId
        String caregiverId = "nonexistent";
        List<LocalDateTime> availableSlots = Arrays.asList(
                LocalDateTime.of(2025,1,13,10,0),
                LocalDateTime.of(2025,1,13,13,0)
        );
        // mock the behavior of userRepository.findById to return an empty Optional
        when(userRepository.findById(caregiverId)).thenReturn(Optional.empty());

        // act & assert
        // verify that an exception is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                // calls the method
                availabilityService.createAvailability(caregiverId, availableSlots)
        );

        // check exception message
        assertEquals("Caregiver with ID nonexistent not found", exception.getMessage());

        // verify that the findById method only gets called one time
        verify(userRepository, times(1)).findById(caregiverId);

        // verify that the save method in availability repository never gets called
        verifyNoInteractions(availabilityRepository);
    }

    @Test
    void testUpdateAvailability_Success() {
        String availabilityId = "avail123";
        LocalDateTime oldDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime newDate = LocalDateTime.of(2025, 1, 16, 12, 0);

        // Mock the repository to return an Availability object with slots
        Availability mockAvailability = mock(Availability.class);
        List<LocalDateTime> availableSlots = Arrays.asList(oldDate, LocalDateTime.of(2025, 1, 16, 14, 0));
        when(availabilityRepository.existsById(availabilityId)).thenReturn(true);
        when(availabilityRepository.findAvailabilityById(availabilityId)).thenReturn(mockAvailability);
        when(mockAvailability.getAvailableSlots()).thenReturn(availableSlots);

        // Call the method
        Availability result = availabilityService.updateAvailability(availabilityId,oldDate,newDate);

        // Verify that theavailability was updated and saved
        assertNotNull(result);
        assertTrue(result.getAvailableSlots().contains(newDate));
        verify(availabilityRepository, times(1)).save(mockAvailability);
    }

    @Test
    void testUpdateAvailability_AvailabilityNotFound() {
        String availabilityID = "avail123";
        LocalDateTime oldDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime newDate = LocalDateTime.of(2025, 1, 16, 12, 0);

        // Mock the repository to simulate the absence of the availability
        when(availabilityRepository.existsById(availabilityID)).thenReturn(false);

        // Call the method and assert that the exception is thrown
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            availabilityService.updateAvailability(availabilityID, oldDate, newDate);
        });

        assertEquals("Availability with id avail123 was not found.", exception.getMessage());
        verify(availabilityRepository, never()).save(any(Availability.class)); // Ensure save was never called
    }

    @Test
    void testUpdateAvailability_OldDateNotFound() {
        String availabilityId = "avail123";
        LocalDateTime oldDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime newDate = LocalDateTime.of(2025, 1, 16, 12, 0);

        // Mock the repository to return an Availability object with slots
        Availability mockAvailability = mock(Availability.class);
        List<LocalDateTime> availableSlots = Arrays.asList(LocalDateTime.of(2025, 1, 16, 14, 0));
        when(availabilityRepository.existsById(availabilityId)).thenReturn(true);
        when(availabilityRepository.findAvailabilityById(availabilityId)).thenReturn(mockAvailability);
        when(mockAvailability.getAvailableSlots()).thenReturn(availableSlots);

        // Call the method and assert that the slots were not updated
        Availability result = availabilityService.updateAvailability(availabilityId, oldDate, newDate);

        assertNotNull(result);
        assertFalse(result.getAvailableSlots().contains(newDate)); // Ensure the old slot was not replaced
        verify(availabilityRepository, never()).save(mockAvailability); // Ensure save was not called
    }

    @Test
    void testDeleteAvailability_Success() {
        // Arrange
        String availabilityId = "123";
        when(availabilityRepository.existsById(availabilityId)).thenReturn(true);

        // Act
        String result = availabilityService.deleteAvailability(availabilityId);

        // Assert
        assertEquals("Availability deleted", result);
        verify(availabilityRepository, times(1)).deleteById(availabilityId);
    }

    @Test
    void testDeleteAvailability_Failure_AvailabilityNotFound() {
        // Arrange
        String availabilityId = "123";
        when(availabilityRepository.existsById(availabilityId)).thenReturn(false);

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            availabilityService.deleteAvailability(availabilityId);
        });
        assertEquals("Availability with id: 123 was not found.", exception.getMessage());
    }

    @Test
    void testDeleteTimeSlot_Success() {
        // Arrange
        String caregiverId = "caregiver123";
        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);
        Availability availability = new Availability();
        availability.setAvailableSlots(new ArrayList<>(List.of(timeSlot)));

        when(availabilityRepository.existsByCaregiverId(caregiverId)).thenReturn(true);
        when(availabilityRepository.existsByAvailableSlots(timeSlot)).thenReturn(true);
        when(availabilityRepository.findAvailabilityByAvailableSlotsContaining(timeSlot)).thenReturn(availability);

        // Act
        String result = availabilityService.deleteTimeSlot(caregiverId, timeSlot);

        // Assert
        assertEquals("Time slot deleted", result);
        verify(availabilityRepository, times(1)).deleteByAvailableSlots(timeSlot);
    }

    @Test
    void testDeleteTimeSlot_Failure_CaregiverNotFound() {
        // Arrange
        String caregiverId = "caregiver123";
        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);

        when(availabilityRepository.existsByCaregiverId(caregiverId)).thenReturn(false);

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            availabilityService.deleteTimeSlot(caregiverId, timeSlot);
        });
        assertEquals("No availabilities for the caregiver with id: caregiver123 was found.", exception.getMessage());
    }

    @Test
    void testDeleteTimeSlot_Failure_TimeSlotNotFound() {
        // Arrange
        String caregiverId = "caregiver123";
        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);

        when(availabilityRepository.existsByCaregiverId(caregiverId)).thenReturn(true);
        when(availabilityRepository.existsByAvailableSlots(timeSlot)).thenReturn(false);

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            availabilityService.deleteTimeSlot(caregiverId, timeSlot);
        });
        assertEquals("This time slot: '2025-01-17T10:00' was not found.", exception.getMessage());
    }

    @Test
    void testValidateCaregiversTimeSlots_Success() {
        // Arrange
        String caregiverId = "caregiver123";
        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);
        List<Availability> caregiversAvailabilities = new ArrayList<>();
        caregiversAvailabilities.add(new Availability(caregiverId, new ArrayList<>(List.of(timeSlot))));

        when(availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId)).thenReturn(caregiversAvailabilities);
        when(appointmentRepository.findAppointmentByCaregiverIdAndDateTime(any(), eq(timeSlot))).thenReturn(null);

        // Act
        availabilityService.validateCaregiversTimeSlots(caregiverId, timeSlot);

        // Assert
        verify(availabilityRepository, times(1)).save(any(Availability.class));
    }

    @Test
    void testValidateCaregiversTimeSlots_Failure_TimeSlotAlreadyInAppointment() {
        // Arrange
        String caregiverId = "caregiver123";
        Role role = Role.USER;

        // Create a set with a single role (USER)
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);
        User user = new User("Sören123", "PW1223", roles, "Sören", "Sörensson", "soren@soren.se", "Sörenstad", "Sörenvägen 1", "0721231212", "19500512");
        Appointment existingAppointment = new Appointment(user, timeSlot);

        when(availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId)).thenReturn(new ArrayList<>());
        when(appointmentRepository.findAppointmentByCaregiverIdAndDateTime(user, timeSlot)).thenReturn(existingAppointment);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            availabilityService.validateCaregiversTimeSlots(caregiverId, timeSlot);
        });
        assertEquals("Time slot already exists in a booked appointment", exception.getMessage());
    }

    @Test
    void testValidateCaregiversTimeSlots_Failure_TimeSlotAlreadyInAvailability() {
        // Arrange
        String caregiverId = "caregiver123";
        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);
        List<Availability> caregiversAvailabilities = new ArrayList<>();
        caregiversAvailabilities.add(new Availability(caregiverId, new ArrayList<>(List.of(timeSlot))));

        when(availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId)).thenReturn(caregiversAvailabilities);
        when(appointmentRepository.findAppointmentByCaregiverIdAndDateTime(any(), eq(timeSlot))).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            availabilityService.validateCaregiversTimeSlots(caregiverId, timeSlot);
        });
        assertEquals("Time slot already exists", exception.getMessage());
    }

    @Test
    void testAddTimeSlot_Success() {
        // Arrange
        String availabilityId = "availability123";
        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);
        Availability availability = new Availability();
        availability.setAvailableSlots(new ArrayList<>());

        when(availabilityRepository.findAvailabilityById(availabilityId)).thenReturn(availability);

        // Act
        availabilityService.addTimeSlot(availabilityId, timeSlot);

        // Assert
        assertTrue(availability.getAvailableSlots().contains(timeSlot));
        verify(availabilityRepository, times(1)).save(availability);
    }

    @Test
    void testAddTimeSlot_Failure_AvailabilityNotFound() {
        // Arrange
        String availabilityId = "availability123";
        LocalDateTime timeSlot = LocalDateTime.of(2025, 1, 17, 10, 0);

        when(availabilityRepository.findAvailabilityById(availabilityId)).thenReturn(null);

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            availabilityService.addTimeSlot(availabilityId, timeSlot);
        });
        assertEquals("Availability with id: availability123 was not found.", exception.getMessage());
    }











}

