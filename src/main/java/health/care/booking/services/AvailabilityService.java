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
    public Availability createAvailability (String caregiverId, List<LocalDateTime> availableSlots) {

        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new IllegalArgumentException("Caregiver with ID " + caregiverId + " not found"));

        Availability availability = new Availability();
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(availableSlots);

        return availabilityRepository.save(availability);

    }

    //UPDATE
    //uppdatera availabilities baserat på id
    // TODO: Create error handling for if oldDate does not exist
    public Availability updateAvailability(String availabilityId, LocalDateTime oldDate, LocalDateTime newDate) {
    Availability updatedAvailability = availabilityRepository.findAvailabilityById(availabilityId);
        if (availabilityRepository.existsById(availabilityId)) {
            List<LocalDateTime> availableSlots = availabilityRepository.findAvailabilityById(availabilityId).getAvailableSlots();
            for (LocalDateTime a : availableSlots) {
                if (a.isEqual(oldDate)) {
                    updatedAvailability.getAvailableSlots().set(availableSlots.indexOf(a), newDate);
                    availabilityRepository.save(updatedAvailability);
                }
            }
            return updatedAvailability;
        } else {
            throw new ObjectNotFoundException("Availability with id " + availabilityId + " was not found.");
        }
    }


    // GET
    // Get all availabilites
    public List<Availability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }
    //DELETE
    //ta bort availability baserat på id

}
