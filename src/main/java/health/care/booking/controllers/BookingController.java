package health.care.booking.controllers;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.services.BookingCoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingCoordinatorService bookingCoordinatorService;

    // ny controller för att hantera bokning genom BookingCoordinatiorService
    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        try {
            Appointment appointment = bookingCoordinatorService.bookAppointment(appointmentRequest);
            return ResponseEntity.ok(appointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // om man vill ha "snyggare" syntax, OBS! har ej testat... det kan ni göra om ni vill
        // eller ta bort det
       /* return Optional.of(appointmentRequest)
                .map(bookingCoordinatorService::bookAppointment)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to create booking"));*/

    }
}
