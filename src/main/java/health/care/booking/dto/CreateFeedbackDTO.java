package health.care.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

public class CreateFeedbackDTO {
    @NotBlank
    private String patientId;
    @NotBlank
    private String appointmentId;


    @NotBlank
    @Size(min = 1, max = 1000)
    private String comment;

    @Size(min = 1, max = 5)
    private int rating;
    @CreatedDate
    private LocalDate created_at;

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }









    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String userId) {
        this.patientId = userId;
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
