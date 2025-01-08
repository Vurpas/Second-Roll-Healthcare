package health.care.booking.controllers;

import health.care.booking.models.Feedback;
import health.care.booking.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    // Post
    @PostMapping()
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        return feedbackService.createFeedback(feedback);
    }
}
