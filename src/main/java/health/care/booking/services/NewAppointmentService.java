package health.care.booking.services;

import health.care.booking.dto.NewAppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    // valde annat namn är createApp... så det inte skulle bli helt förvirrande
    public Appointment newAppointment(NewAppointmentRequest request) {
        // kollar först om caregiver och patient finns i databasen
        User caregiver = userRepository.findById(request.getCaregiverId())
                .orElseThrow(() -> new IllegalArgumentException("Caregiver not found with ID: " + request.getCaregiverId()));

        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + request.getPatientId()));

        Appointment appointment = new Appointment();
        appointment.setCaregiverId(caregiver);
        appointment.setPatientId(patient);
        // sätter sloten baserat på det som kommer ifrån requesten
        appointment.setDateTime(request.getSelectedSlot());

        return appointmentRepository.save(appointment);
    }
}
