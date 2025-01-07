package health.care.booking.services;

import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;


public class AppointmentService {
    @Autowired
    private UserRepository userRepository;

    private Appointment appointment;
    private Availability availability;


    public List<LocalDateTime> getAvailableSlots (Availability availability) {
        List availableSlots = availability.getAvailableSlots();
      return availability.getAvailableSlots();
    }

    public void createAppointment () {

    }
}
