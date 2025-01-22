package health.care.booking.respository;

import health.care.booking.models.Appointment;
import health.care.booking.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String > {
   Optional<Appointment> findAppointmentById(String appointmentId);
   Appointment findAppointmentByCaregiverIdAndDateTime(User caregiverId, LocalDateTime dateTime);
   List<Appointment> findAllByCaregiverIdOrPatientId(User caregiverId, User patientId);

   Appointment getAppointmentById(String appointmentId);

}
