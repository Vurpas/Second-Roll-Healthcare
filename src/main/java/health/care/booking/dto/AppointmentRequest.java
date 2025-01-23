package health.care.booking.dto;

import java.time.LocalDateTime;

public class AppointmentRequest {

    private String caregiverId;
    private String patientId;
    private LocalDateTime selectedSlot;
    public String symptoms;

    public AppointmentRequest() {
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(LocalDateTime selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
