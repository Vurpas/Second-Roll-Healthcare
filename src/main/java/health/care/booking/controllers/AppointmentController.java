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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> createAppointment (@RequestBody AppointmentRequest appointmentRequest) {
        try
        {Appointment appointment = appointmentService.createAppointment(appointmentRequest);
            return ResponseEntity.ok(AppointmentResponse.of(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: change the appointment status to CANCELLED
    @PutMapping(value="/cancel/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancelAppointment (@PathVariable String appointmentId) {
        try {
            Appointment appointment = appointmentService.cancelAppointment(appointmentId);
                return ResponseEntity.ok(AppointmentResponse.of(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // GET: Get ALL appointments
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getAllAppointments() {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(allAppointments);
    }

    //GET all appointments based on USERNAME
}