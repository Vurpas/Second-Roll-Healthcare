package health.care.booking.controllers;


import health.care.booking.dto.AppointmentRequest;
import health.care.booking.dto.AppointmentResponse;
import health.care.booking.models.Appointment;
import health.care.booking.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/appointment")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping()
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Appointment appointment = appointmentService.createAppointment(appointmentRequest);
        return ResponseEntity.ok(new AppointmentResponse(appointment));
    }

    // UPDATE: update appointment

    // DELETE: delete appointment

    // GET: get all appointments for a given id (caregiver and patient)

    // GET: Get ALL appointments
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getAllAppointments() {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(allAppointments);
    }
    //GET all appointments based on USERNAME

}