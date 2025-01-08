package health.care.booking.respository;

import health.care.booking.models.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    // skall den extenda Mongrepository som i secondroll?
    // med <Availability, String>

    //göra en findByCaregiverId(userId)?
}
