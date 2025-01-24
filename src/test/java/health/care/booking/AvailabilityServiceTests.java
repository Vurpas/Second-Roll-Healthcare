package health.care.booking;

import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import health.care.booking.dto.AvailabilityDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Collections.list;
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

    private AvailabilityDTO availabilityDTO;

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
        // Arrange
        User caregiver = new User();
        caregiver.setId("1");
        caregiver.setFirstName("John");
        caregiver.setLastName("Doe");
        List<LocalDateTime> list = new ArrayList<>();

        // Ensure availabilityDTO is properly initialized
        availabilityDTO = new AvailabilityDTO(caregiver.getId(), list);
        availabilityDTO.setCaregiverId("1"); // Set valid caregiver ID
        availabilityDTO.setAvailableSlots(list); // Set available slots

        // Mock behavior for userRepository
        when(userRepository.findById("1")).thenReturn(Optional.of(caregiver));

        // Mock behavior for availabilityRepository
        Availability availability = new Availability();
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(availabilityDTO.getAvailableSlots());

        when(availabilityRepository.save(any(Availability.class))).thenReturn(availability);

        // Act
        Availability result = availabilityService.createAvailability(availabilityDTO);

        // Assert
        assertNotNull(result);
        assertEquals(caregiver, result.getCaregiverId());
        assertEquals(availabilityDTO.getAvailableSlots(), result.getAvailableSlots());
        verify(userRepository, times(1)).findById("1");
        verify(availabilityRepository, times(1)).save(any(Availability.class));
    }


    @Test
    public void testCreateAvailability_CaregiverNotFound() {
        // Arrange
        User caregiver = new User();
        caregiver.setId("1");
        caregiver.setFirstName("John");
        caregiver.setLastName("Doe");
        List<LocalDateTime> list = new ArrayList<>();

        // Ensure availabilityDTO is properly initialized
        availabilityDTO = new AvailabilityDTO(caregiver.getId(), list);
        availabilityDTO.setCaregiverId("1"); // Set valid caregiver ID
        availabilityDTO.setAvailableSlots(list); // Set available slots

        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            availabilityService.createAvailability(availabilityDTO);
        });

        // Assert
        assertEquals("Caregiver with ID 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById("1");
        verify(availabilityRepository, times(0)).save(any(Availability.class));
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
    public void testDeleteAvailability_Success() {

        String availabilityId = "avail123";

        // Arrange
        when(availabilityRepository.existsById(availabilityId)).thenReturn(true);

        // Act
        String result = availabilityService.deleteAvailability(availabilityId);

        // Assert
        assertEquals("Availability deleted", result);
        verify(availabilityRepository, times(1)).deleteById(availabilityId);
    }

    @Test
    public void testDeleteAvailability_NotFound() {
        // Arrange
        String availabilityId = "avail123";
        when(availabilityRepository.existsById(availabilityId)).thenReturn(false);

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            availabilityService.deleteAvailability(availabilityId);
        });

        // Assert
        assertEquals("Availability with id: " + availabilityId + " was not found.", exception.getMessage());
        verify(availabilityRepository, times(0)).deleteById(availabilityId);  // delete should not be called
    }

    private String caregiverId;
    private LocalDateTime slot;
    private Availability availability;

    private User caregiver;


    @Test
    public void testRemoveTimeSlot_Success() {
        caregiverId = "caregiver123"; // Example caregiverId

        List<LocalDateTime> availableSlots =new ArrayList<>(Arrays.asList(LocalDateTime.of(2025, 1, 16, 14, 0)));
        slot = LocalDateTime.of(2025, 1, 23, 10, 0);

        caregiver = new User();
        caregiver.setId(caregiverId);

        // Create an availability object with a time slot
        availability = new Availability();
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(availableSlots);
        availability.getAvailableSlots().add(slot); // Add the time slot

            // Arrange: Mock the repository to return availability with the slot
            when(availabilityRepository.findByCaregiverIdAndAvailableSlotsContaining(caregiverId, slot))
                    .thenReturn(Optional.of(availability));

        // Mock the save behavior: Ensure save is called and reflect the updated list
        when(availabilityRepository.save(any(Availability.class)))
                .thenReturn(availability); // Return the updated availability object after removal

        // Act: Call removeTimeSlot
            availabilityService.removeTimeSlot(caregiverId, slot);

        // Assert: Verify the time slot was removed and repository.save was called
        // Verify that the slot was removed and only the other slot remains
        assertEquals(1, availability.getAvailableSlots().size()); // List should contain exactly 1 slot
        assertTrue(availability.getAvailableSlots().contains(LocalDateTime.of(2025, 1, 16, 14, 0))); // The remaining slot
        verify(availabilityRepository, times(1)).save(availability); // Verify that the save method was called once
    }

    @Test
    public void testRemoveTimeSlot_Fail_SlotNotFound() {
        // Setup the test data
        caregiverId = "caregiver123"; // Example caregiverId

        // Define the available slot and the slot to remove (we will try to remove a slot that is not in the list)
        List<LocalDateTime> availableSlots = new ArrayList<>(Arrays.asList(LocalDateTime.of(2025, 1, 16, 14, 0))); // Slot that exists
        LocalDateTime slotToRemove = LocalDateTime.of(2025, 1, 23, 10, 0); // Slot we want to remove but doesn't exist in the list

        // Create the Availability and User objects
        caregiver = new User(); // Create a new User for the caregiver
        caregiver.setId(caregiverId); // Assuming you have an ID field in your User class

        availability = new Availability(); // Create a new Availability object
        availability.setCaregiverId(caregiver); // Set the caregiver reference
        availability.setAvailableSlots(availableSlots); // Set the available slots

        // Log the available slots before removal
        System.out.println("Before removal - Slots: " + availability.getAvailableSlots());
        System.out.println("Slot to remove: " + slotToRemove);

        // Arrange: Mock the repository to simulate the case when the slot is not found
        when(availabilityRepository.findByCaregiverIdAndAvailableSlotsContaining(caregiverId, slotToRemove))
                .thenReturn(Optional.empty()); // Simulate that the slot is not found


        // Act and Assert: Verify that the exception is thrown when trying to remove a non-existing slot
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            availabilityService.removeTimeSlot(caregiverId, slotToRemove); // This should throw an exception
        });

        // Check the exception message
        assertEquals("Slot not found", thrown.getMessage()); // Check that the error message is correct

        // Verify that the repository save was not called (since the removal didn't happen)
        verify(availabilityRepository, times(0)).save(any(Availability.class)); // save should not be called

        // Additional Debugging: Verify that the mock method was indeed called
        verify(availabilityRepository, times(1)).findByCaregiverIdAndAvailableSlotsContaining(caregiverId, slotToRemove); // Ensure the mock was triggered

        // Debugging: Print the slots again to confirm the state during the test
        System.out.println("After mock repository call - Slots: " + availability.getAvailableSlots());

    }
}


