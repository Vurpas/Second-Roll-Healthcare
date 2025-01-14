package health.care.booking.services;

import health.care.booking.dto.CreateFeedbackDTO;
import health.care.booking.exceptions.ServiceException;
import health.care.booking.models.Appointment;
import health.care.booking.models.Feedback;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FeedbackServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private CreateFeedbackDTO createFeedbackDTO;
    private User mockPatient;
    private Appointment mockAppointment;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock data
        mockPatient = new User();
        mockPatient.setId("1");
        mockAppointment = new Appointment();
        mockAppointment.setId("1");

        // Setup a sample CreateFeedbackDTO
        createFeedbackDTO = new CreateFeedbackDTO();
        createFeedbackDTO.setPatientId("1");
        createFeedbackDTO.setAppointmentId("1");
        createFeedbackDTO.setRating(5); // valid rating
        createFeedbackDTO.setComment("Great service!");
    }

    @Test
    public void testCreateFeedbackSuccess() {
        // Arrange: Mock the repository calls
        when(userRepository.findById(createFeedbackDTO.getPatientId())).thenReturn(Optional.of(mockPatient));
        when(appointmentRepository.findAppointmentById(createFeedbackDTO.getAppointmentId())).thenReturn(Optional.of(mockAppointment));
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act: Call the method under test
        Feedback feedback = feedbackService.createFeedback(createFeedbackDTO);

        // Assert: Check the result
        assertNotNull(feedback);
        assertEquals(mockPatient, feedback.getPatientId());
        assertEquals(mockAppointment, feedback.getAppointmentId());
        assertEquals(createFeedbackDTO.getComment(), feedback.getComment());
        assertEquals(createFeedbackDTO.getRating(), feedback.getRating());
    }

    @Test(expected = ServiceException.class)
    public void testCreateFeedback_UserNotFound() {
        // Arrange: Mock the reposiroty call for a user that doesn´t exist
        when(userRepository.findById(createFeedbackDTO.getPatientId())).thenReturn(Optional.empty());

        // Act: Call the method under test
        feedbackService.createFeedback(createFeedbackDTO);
    }

    @Test(expected = ServiceException.class)
    public void testCreateFeedback_AppointmentNotFound() {
        // Arrange: Mock the repository call for an appointment that doesn´t exist
        when(userRepository.findById(createFeedbackDTO.getPatientId())).thenReturn(Optional.of(new User()));
        when(appointmentRepository.findAppointmentById(createFeedbackDTO.getAppointmentId())).thenReturn(Optional.empty());

        // Act: Call the method under test
        feedbackService.createFeedback(createFeedbackDTO);
    }

    @Test(expected = ServiceException.class)
    public void testCreateFeedback_InvalidRating_LessThan1() {
        // Arrange: Set invalid rating less than 1
        createFeedbackDTO.setRating(0);

        // Act: Call the method under test
        feedbackService.createFeedback(createFeedbackDTO);
    }

    @Test(expected = ServiceException.class)
    public void testCreateFeedback_InvalidRating_greaterThan6() {
        // Arrange: Set invalid rating greater than 6
        createFeedbackDTO.setRating(7);

        // Act: Call the method under test
        feedbackService.createFeedback(createFeedbackDTO);
    }
}
