package health.care.booking.services;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookingCoordinatorService {
    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private AppointmentService appointmentService;

    // Class that handles the booking of appointments to make sure appointment and availability is kept separate
    public Appointment bookAppointment(AppointmentRequest appointmentRequest) {
        // Check if slot is available
        if (!availabilityService.isSlotAvailable(appointmentRequest.getCaregiverId(), appointmentRequest.getSelectedSlot())) {
            throw new IllegalStateException("Selected slot is no longer available");
        }

        // Create new appointment object
        Appointment appointment = appointmentService.createAppointment(appointmentRequest);

        // Removes selected slot from availabilities
        availabilityService.removeTimeSlot(appointmentRequest.getCaregiverId(), appointmentRequest.getSelectedSlot());

        return appointment;
    }
}
