package health.care.booking.services;


import health.care.booking.exceptions.ObjectNotFoundException;
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

    // OBS create error handling for unorthorized attempts to create availability
    // and check that entered availability is not already excisting! OBS
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
    // TODO: Create error handling for if oldDate does not exist
    public Availability updateAvailability(String availabilityId, LocalDateTime oldDate, LocalDateTime newDate) {

        if (availabilityRepository.existsById(availabilityId)) {
            Availability updatedAvailability = availabilityRepository.findAvailabilityById(availabilityId);
            List<LocalDateTime> availableSlots = updatedAvailability.getAvailableSlots();
            availableSlots.set(availableSlots.indexOf(oldDate) + 1, newDate);

            return availabilityRepository.save(updatedAvailability);
        } else {
            throw new ObjectNotFoundException("Availability with id " + availabilityId + " was not found.");
        }
    }
    //GET
    //hämta availabilities baserat på caregiverId(userId)

    //DELETE
    //ta bort availability baserat på id

}
