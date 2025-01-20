package health.care.booking.services;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AppointmentService {
    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(UserRepository userRepository, AvailabilityRepository availabilityRepository,
                               AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // POST: create an appointment
    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        Availability availability = availabilityRepository.findById(appointmentRequest.getAvailabilityId())
                .orElseThrow(() -> new IllegalArgumentException("Availability with ID " + appointmentRequest.getAvailabilityId() + " not found."));
            Appointment appointment = new Appointment();
            appointment.setCaregiverId(availability.getCaregiverId());
            appointment.setDateTime(appointmentRequest.getAppointmentDate());
            appointment.setStatus(Status.SCHEDULED);
            User patientId = userRepository.findById(appointmentRequest.getPatientId())
                    .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + appointmentRequest.getPatientId() + " not found."));
            appointment.setPatientId(patientId);

            // loops through the available slots and filter out the slots *not* chosen and saves them in a List
            // the chosen slot is then removed from the List
            List<LocalDateTime> availableSlots = availability.getAvailableSlots();
            List<LocalDateTime> dates = availableSlots.stream().filter(element -> !element.isEqual(appointmentRequest.getAppointmentDate())).toList();
            if (dates.size()<availableSlots.size()) {
                availability.setAvailableSlots(dates);
                availabilityRepository.save(availability);
        } else {
                throw new IllegalArgumentException("The specified date does not exist");
            }
            // Save to database with repository
            return appointmentRepository.save(appointment);
    }


    // PUT: Cancel appointment and set appointment status to "CANCELLED"
    public Appointment cancelAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment with ID " + appointmentId + " not found."));
        appointment.setStatus(Status.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    // GET: get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAllAppointmentsByUserId(String userId) {
        User user = userRepository.findUserById(userId);
        List<Appointment> foundAppointments = appointmentRepository.findAllByCaregiverIdOrPatientId(user, user);
        if (foundAppointments == null || foundAppointments.isEmpty()) {
            throw new ObjectNotFoundException("No appointments found for user with id: '" + userId + "'");
        } else
            foundAppointments.sort(Comparator.comparing(Appointment::getDateTime));
        return foundAppointments;
    }

    public List<Appointment> getAllAppointmentsByUserIdAndDate(String userId, String currentDate) {
        User user = userRepository.findUserById(userId);
        List<Appointment> todaysAppointments = new ArrayList<>();
        List<Appointment> foundAppointments = appointmentRepository.findAllByCaregiverIdOrPatientId(user, user);
        for (Appointment a : foundAppointments) {
            if (a.getDateTime().toString().contains(currentDate)) {
                todaysAppointments.add(a);
            }
        }
        todaysAppointments.sort(Comparator.comparing(Appointment::getDateTime));
        return todaysAppointments;
    }
}