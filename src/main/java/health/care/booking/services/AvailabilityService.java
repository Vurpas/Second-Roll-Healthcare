package health.care.booking.services;


import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AvailabilityRepository availabilityRepository;



    //POST
    //skapa create availability metod
    //behöver caregiverId, och available slots
    public Availability createAvailability (String caregiverId, List<LocalDateTime> availabilitySlots) {

        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new IllegalArgumentException("Caregiver with ID " + caregiverId + " not found"));

        Availability availability = new Availability();
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(availabilitySlots);

        return availabilityRepository.save(availability);

    }

    //GET
    //hämta alla availabilities

    //UPDATE
    //uppdatera availabilities baserat på id

    //GET
    //hämta availabilities baserat på caregiverId(userId)

    //DELETE
    //ta bort availability baserat på id

}
