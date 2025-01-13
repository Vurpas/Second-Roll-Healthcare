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

@Service
public class AppointmentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    // POST: create appointment
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
            // Save to database with repository for appointment
            return appointmentRepository.save(appointment);
    }


    // EDIT: update appointment

    // DELETE: delete an appointment

    // GET: get all appointments for a given id (caregiver and patient)

}