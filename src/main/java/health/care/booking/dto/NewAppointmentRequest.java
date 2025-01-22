package health.care.booking.dto;

import java.time.LocalDateTime;

public class NewAppointmentRequest {
    private String caregiverId;
    private String patientId;
    private LocalDateTime selectedSlot;

    public NewAppointmentRequest() {
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
}
