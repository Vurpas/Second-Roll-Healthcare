package health.care.booking.controllers;


import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/appointment")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    // POST: create appointment
    @PostMapping()
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
    Appointment appointment = appointmentService.createAppointment(appointmentRequest);
    return ResponseEntity.ok(appointment);
    }







}
