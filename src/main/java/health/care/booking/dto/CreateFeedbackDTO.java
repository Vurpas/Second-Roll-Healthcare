package health.care.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateFeedbackDTO {
    @NotBlank
    private String userId;
    @NotBlank
    private String appointmentId;
    @NotBlank
    @Size(min = 1, max = 1000)
    private String comment;
    @NotBlank
    @Size(min = 1, max = 5)
    private int rating;






    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
