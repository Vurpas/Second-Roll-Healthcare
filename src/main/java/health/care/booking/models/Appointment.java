package health.care.booking.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Optional;

@Document(collection = "appointment")
public class Appointment {
    @Id
    private String id;

    @DBRef
    private Optional<User> patientId;

    @DBRef
    private User caregiverId;

    // datum och tid, vill ni så kan ni ändra till något annat
    // tex ett fält för datum ett för tid det är upp till er
    private LocalDateTime dateTime;

    // använder Enum Status klassen
    private Status status;

    public Appointment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Optional<User> getPatientId() {
        return patientId;
    }

    public void setPatientId(Optional<User> patientId) {
        this.patientId = patientId;
    }

    public User getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(User caregiverId) {
        this.caregiverId = caregiverId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
