package health.care.booking.services;

import health.care.booking.dto.CreateFeedbackDTO;
import health.care.booking.exceptions.ServiceException;
import health.care.booking.models.Appointment;
import health.care.booking.models.Feedback;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackService {
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;

    public FeedbackService(UserRepository userRepository, FeedbackRepository feedbackRepository,
                              AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // Create feedback
    public Feedback createFeedback(CreateFeedbackDTO createFeedbackDTO) {
        User patient = userRepository.findById(createFeedbackDTO.getPatientId())
                .orElseThrow(() -> new ServiceException("User not found"));

        Appointment appointment = appointmentRepository.findAppointmentById(createFeedbackDTO.getAppointmentId())
                .orElseThrow(() -> new ServiceException("Appointment not found"));

        int number = createFeedbackDTO.getRating();

        if (number <= 0 || number > 6) {
            throw new ServiceException("Please select a rating between 1 - 6!");
        } else {
            createFeedbackDTO.setRating(number);
        }


        Feedback feedback = new Feedback();

        feedback.setPatientId(patient);
        feedback.setAppointmentId(appointment);
        feedback.setComment(createFeedbackDTO.getComment());
        feedback.setRating(createFeedbackDTO.getRating());
        feedback.setCreated_at(LocalDate.now());

        return feedbackRepository.save(feedback);
    }


    // Get all posted feedbacks
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    // Delete Feedback
    public String deleteFeedback(String id) {
        feedbackRepository.deleteById(id);
        return "Feedback deleted";
    }

}