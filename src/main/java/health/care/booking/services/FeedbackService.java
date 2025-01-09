package health.care.booking.services;

import health.care.booking.dto.CreateFeedbackDTO;
import health.care.booking.exceptions.ServiceException;
import health.care.booking.models.Appointment;
import health.care.booking.models.Feedback;
import health.care.booking.models.User;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    UserRepository userRepository;

    // Create feedback
    public Feedback createFeedback(CreateFeedbackDTO createFeedbackDTO) {
        User user = userRepository.findById(createFeedbackDTO.getUserId())
                .orElseThrow(() -> new ServiceException("User not found"));

        // här ska appointmentRepository in

        Appointment appointment = new Appointment();
        Feedback feedback = new Feedback();

        feedback.setPatientId(user);
     //   feedback.setAppointmentId(appointment);
        feedback.setComment(createFeedbackDTO.getComment());
        feedback.setRating(createFeedbackDTO.getRating());
        feedback.setCreated_at(createFeedbackDTO.getCreated_at());

        return feedbackRepository.save(feedback);
    }


// Get all posted feedbacks
public List<Feedback> getAllFeedbacks() {
    return feedbackRepository.findAll();
}
}