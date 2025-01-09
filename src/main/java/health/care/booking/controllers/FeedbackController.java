package health.care.booking.controllers;

import health.care.booking.models.Feedback;
import health.care.booking.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    // Post
   /* @PostMapping()
    public ResponseEntity<CreateFeedbackDTO> createFeedback(@RequestBody CreateFeedbackDTO createFeedbackDTO) {
        Feedback feedback = feedbackService.createFeedback(createFeedbackDTO);
        User user = feedback.getPatientId();
        return ResponseEntity.ok().body(new CreateFeedbackDTO(feedback.getId(),user.getId(),user.getUsername(),
                feedback.getAppointmentId(),feedback.getComment(),feedback.getRating()));
    }
    */

    // Get
    @GetMapping()
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }
}
