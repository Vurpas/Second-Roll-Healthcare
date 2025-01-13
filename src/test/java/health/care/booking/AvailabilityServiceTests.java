package health.care.booking;

import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AvailabilityServiceTests {
    //mocka repository
    @Mock
    private UserRepository userRepository;
    @Mock
    private AvailabilityRepository availabilityRepository;

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












}

