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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    // POST: create an appointment
    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        // availability och appointment har ingen relation i databasen, varför ska dom då vara beroende av varandra i requesten?
        // dom båda använder samma datatyp för att förenkla bokningsprocessen.
        // det här blir en onödig koll i databasen dessutom tar servern in ALL data och sen filtrerar den.
        Availability availability = availabilityRepository.findById(appointmentRequest.getAvailabilityId())
                .orElseThrow(() -> new IllegalArgumentException("Availability with ID " + appointmentRequest.getAvailabilityId() + " not found."));
            Appointment appointment = new Appointment();
            // det saknas checks på om caregiver faktiskt finns i databasen.
            // det bör ske innan Appointment initieras
            appointment.setCaregiverId(availability.getCaregiverId());
            appointment.setDateTime(appointmentRequest.getAppointmentDate());
            appointment.setStatus(Status.SCHEDULED);
            // den här kontrollen bör ske först i metoden tillsammans med en koll om caregiver
            // finns i databasen
            User patientId = userRepository.findById(appointmentRequest.getPatientId())
                    .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + appointmentRequest.getPatientId() + " not found."));
            appointment.setPatientId(patientId);

            // loops through the available slots and filter out the slots *not* chosen and saves them in a List
            // the chosen slot is then removed from the List
            // det här bör inte hanteras av den här metode få det bryter mot separation of concerns
            // det bör finnas en util metod för Availability att ta bort en "slot"
            // koden blir hårt kopplad och skapar onödiga beorende. svårt att testa isolerat...
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

    // det är inte ett fel att appointments inte finns, det kan absolut vara så och bör inte ses som
    // något vi ska hantera här, det bör hanterar i klienten tex nåt i stil med:
    // "inga bokningar än!"
    public List<Appointment> getAllAppointmentsByUserId(String userId) {
        User user = userRepository.findUserById(userId);
        List<Appointment> foundAppointments = appointmentRepository.findAllByCaregiverIdOrPatientId(user, user);
        if (foundAppointments == null || foundAppointments.isEmpty()) {
            throw new ObjectNotFoundException("No appointments found for user with id: '" + userId + "'");
        } else
            foundAppointments.sort(Comparator.comparing(Appointment::getDateTime));
        return foundAppointments;
    }
}