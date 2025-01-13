package health.care.booking.models;

import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "feedback")
public class Feedback {
    @Id
    private String id;

    // Feedback kan man endast göra på ett specifikt Appointment
    @DBRef
    private Appointment appointmentId;



    // ev ta bort patient finns i appointment men kanske att det påverkar performance..
    @DBRef
    private User patientId;

    private String comment;

    // väldigt osäker på om det här fungerar..
    // men har lovat att hjälpa er om det inte gör det
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



    public Feedback() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Appointment getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointment appointmentId) {
        this.appointmentId = appointmentId;
    }

    public User getPatientId() {
        return patientId;
    }

    public void setPatientId(User patientId) {
        this.patientId = patientId;
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
