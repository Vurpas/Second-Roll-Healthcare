package health.care.booking.respository;

import health.care.booking.models.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {

    Availability findAvailabilityById(String userId);

    Availability findFirstByAvailableSlots(LocalDateTime date);

    boolean existsByCaregiverId(String caregiverId);

    //create find
}
