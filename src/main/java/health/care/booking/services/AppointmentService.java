package health.care.booking.services;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class AppointmentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;


    public List<LocalDateTime> showAvailableSlots(Availability availability) {
        List<LocalDateTime> availableSlots = availability.getAvailableSlots();
        return availability.getAvailableSlots();
    }

    // POST
    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        // Will get from availabilityService when available :)
        // User caregiverId = availability.getCaregiverId();

        String patientId = appointmentRequest.getPatientId();
        Appointment appointment = new Appointment();
        appointment.setCaregiverId(new User()); // temporary
        appointment.setDateTime(appointmentRequest.getAppointmentDate());
        appointment.setStatus(Status.SCHEDULED);

        // When findById is implemented. Or, reiterate and use username instead
        Optional<User> user = userRepository.findById(appointmentRequest.getPatientId());
        appointment.setPatientId(user);

        // Save to database with repository for appointment
        return appointmentRepository.save(appointment);
    }
}
