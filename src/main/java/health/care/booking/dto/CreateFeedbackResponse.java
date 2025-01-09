package health.care.booking.dto;

import java.time.LocalDate;

public class CreateFeedbackResponse {
    private String id;
    private String userId;
    //private Appointment appointmentId;
    private String comment;
    private int rating;
    private LocalDate created_at;



    public CreateFeedbackResponse(String id, String userId, /*Appointment appointmentId,*/ String comment, int rating, LocalDate created_at) {
        this.id = id;
        this.userId = userId;
       // this.appointmentId = appointmentId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

  /*  public Appointment getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointment appointmentId) {
        this.appointmentId = appointmentId;
    }

   */

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
