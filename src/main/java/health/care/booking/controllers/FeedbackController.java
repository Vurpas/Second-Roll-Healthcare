package health.care.booking.controllers;

import health.care.booking.dto.CreateFeedbackDTO;
import health.care.booking.dto.CreateFeedbackResponse;
import health.care.booking.models.Feedback;
import health.care.booking.models.User;
import health.care.booking.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    // Post
    @PostMapping()
    public ResponseEntity<CreateFeedbackResponse> createFeedback(@RequestBody CreateFeedbackDTO createFeedbackDTO) {
        Feedback feedback = feedbackService.createFeedback(createFeedbackDTO);
        User user = feedback.getPatientId();
        return ResponseEntity.ok().body(new CreateFeedbackResponse(feedback.getId(),user.getId(),
               /* feedback.getAppointmentId(),*/feedback.getComment(),feedback.getRating(),feedback.getCreated_at()));
    }


    // Get
    @GetMapping()
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }
}
