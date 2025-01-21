package health.care.booking.respository;

import health.care.booking.models.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {

    // exempel
    @Query("{'caregiverId': ?0, 'weekNumber': ?1, 'year': ?2}")
    Availability findByCaregiverIdAndWeekAndYear(String caregiverId, int weekNumber, int year);

    @Query(value = "{'availableSlots': {$elemMatch: {$lt: ?0}}}", delete = true)
    void deleteByAllSlotsBeforeDate(LocalDateTime date);

   // -------

    Availability findAvailabilityById(String userId);

    List<Availability> findAvailabilitiesByCaregiverId(String userId);

    Availability findAvailabilityByAvailableSlotsContaining(LocalDateTime dateTime);

    boolean existsByAvailableSlots(LocalDateTime dateTime);

    boolean existsByCaregiverId(String caregiverId);

    void deleteByAvailableSlots(LocalDateTime timeSlot);

    //create find
}
