package health.care.booking.respository;

import health.care.booking.models.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NewAvailabilityRepository extends MongoRepository<Availability, String> {
    // custom metod som behöver både id på caregiver och en slot av typen LocalDateTime
    // används när en timeslot ska tas bort vi bokning av tid
    Optional<Availability> findByCaregiverIdAndAvailableSlotsContaining(String caregiverId, LocalDateTime slot);
}
