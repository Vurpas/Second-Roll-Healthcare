package health.care.booking.dto;

import health.care.booking.models.Appointment;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private String appointmentId;
    private String patientId;
    private String caregiverId;
    private LocalDateTime dateTime;

    public AppointmentResponse(Appointment appointment) {
        this.appointmentId = appointment.getId();
        this.patientId = appointment.getPatientId().getId();
        this.caregiverId = appointment.getCaregiverId().getId();
        this.dateTime = appointment.getDateTime();
    }

    // Getters and setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}