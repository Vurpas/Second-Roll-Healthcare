package health.care.booking.dto;

import java.time.LocalDateTime;

public class AppointmentRequest {

    public LocalDateTime appointmentDate;
    public String patientId;
    public String availabilityId;
    public String symptoms;

    public AppointmentRequest(LocalDateTime appointmentDate, String patientId, String availabilityId, String symptoms) {
        this.availabilityId = availabilityId;
        this.appointmentDate = appointmentDate;
        this.patientId = patientId;
        this.symptoms = symptoms;
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


    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
