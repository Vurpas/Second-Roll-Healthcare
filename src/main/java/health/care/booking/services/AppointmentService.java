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
    private AvailabilityService availabilityService;

    // POST: create appointment
    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        Optional<Availability> availability = availabilityRepository.findById(appointmentRequest.getAvailabilityId());
        if (availability.isPresent()) {
            Availability currentAvailability = availability.get();
            Appointment appointment = new Appointment();
            appointment.setCaregiverId(currentAvailability.getCaregiverId());
            appointment.setDateTime(appointmentRequest.getAppointmentDate());
            appointment.setStatus(Status.SCHEDULED);
            Optional<User> patientId = userRepository.findById(appointmentRequest.getPatientId());
            appointment.setPatientId(patientId.get());  // error handling is needed here

            // loops through the available slots and filter out the slots *not* chosen and saves them in a List
            // the chosen slot is then removed from the List
            List<LocalDateTime> availableSlots = currentAvailability.getAvailableSlots();
            System.out.println(availableSlots);
            System.out.println(appointmentRequest.getAppointmentDate());
            List<LocalDateTime> dates = availableSlots.stream().filter(element -> !element.isEqual(appointmentRequest.getAppointmentDate())).toList();
            currentAvailability.setAvailableSlots(dates);
            availabilityRepository.save(currentAvailability);
            System.out.println(dates);
            System.out.println(appointmentRequest.getAppointmentDate());


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