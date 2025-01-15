package health.care.booking.respository;

import health.care.booking.models.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {

    Availability findAvailabilityById(String userId);

    Availability findAvailabilityByAvailableSlotsContaining(LocalDateTime dateTime);

    boolean existsByAvailableSlots(LocalDateTime dateTime);

    boolean existsByCaregiverId(String caregiverId);

    void deleteByAvailableSlots(LocalDateTime timeSlot);

    //create find
}
