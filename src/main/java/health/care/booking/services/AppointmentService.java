package health.care.booking.services;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;


    public List<LocalDateTime> showAvailableSlots(Availability availability) {
        List<LocalDateTime> availableSlots = availability.getAvailableSlots();
        return availability.getAvailableSlots();
    }

    // POST: create appointment
    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        Optional<Availability> availability = availabilityRepository.findById(appointmentRequest.getAvailabilityId());
        if (availability.isPresent()) {
            Appointment appointment = new Appointment();
            appointment.setCaregiverId(availability.get().getCaregiverId());
            appointment.setDateTime(appointmentRequest.getAppointmentDate());
            appointment.setStatus(Status.SCHEDULED);
            Optional<User> patientId = userRepository.findById(appointmentRequest.getPatientId());
            appointment.setPatientId(patientId.get()); // error handling is needed here
            // need to update availability array
            // loop through array in availability and remove matching date

            // Save to database with repository for appointment
            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Availability for given ID not found");
        }
    }

    // EDIT: update appointment

    // DELETE: delete an appointment

    // GET: get all appointments for a given id (caregiver and patient)


}
