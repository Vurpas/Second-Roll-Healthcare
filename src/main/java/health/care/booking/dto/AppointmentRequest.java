package health.care.booking.dto;

import java.time.LocalDateTime;

public class AppointmentRequest {

    private final LocalDateTime appointmentDate;
    private final String patientId;
    private final String availabilityId;

    public AppointmentRequest(LocalDateTime appointmentDate, String patientId, String availabilityId) {
        this.appointmentDate = appointmentDate;
        this.patientId = patientId;
        this.availabilityId = availabilityId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getAvailabilityId() {
        return availabilityId;
    }
}
