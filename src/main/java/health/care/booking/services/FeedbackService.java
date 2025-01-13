package health.care.booking.services;

import health.care.booking.dto.CreateFeedbackDTO;
import health.care.booking.exceptions.ServiceException;
import health.care.booking.models.Feedback;
import health.care.booking.models.User;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        Feedback feedback = new Feedback();

        feedback.setPatientId(user);
     //   feedback.setAppointmentId(appointment);
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
       /*  Optional<User> user = Optional.ofNullable(userRepository.findByRoles(ADMIN));
        if (!user.isPresent() ) {
            throw new ServiceException("Action not allowed.");
        }
        
        */
        feedbackRepository.deleteById(id);
        return "Feedback deleted";
    }
}

