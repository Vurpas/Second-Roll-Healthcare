package health.care.booking.dto;

import health.care.booking.models.Appointment;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

public class CreateFeedbackResponse {
    private String id;
    private String patientId;
    private Appointment appointmentId;
    private String comment;
    private int rating;
    @CreatedDate
    private LocalDate created_at;



    public CreateFeedbackResponse(String id, String patientId, Appointment appointmentId, String comment, int rating, LocalDate created_at) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.comment = comment;
        this.rating= rating;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Appointment getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointment appointmentId) {
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

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }
}
