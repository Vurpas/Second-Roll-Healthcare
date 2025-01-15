package health.care.booking.services;


import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    // Delete FULL availability based on ID
    public String deleteAvailability(String availabilityId) {
        if(!availabilityRepository.existsById(availabilityId)) {
            throw new ObjectNotFoundException("Availability with id: " + availabilityId + " was not found.");
        }
        availabilityRepository.deleteById(availabilityId);
        return "Availability deleted";
    }

    // DELETE specific time slot
    public String deleteTimeSlot(String caregiverId, LocalDateTime timeSlot) {
        if (!availabilityRepository.existsByCaregiverId(caregiverId)) {
            throw new ObjectNotFoundException("No availabilities for the caregiver with id: " + caregiverId + " was found.");
        } else if (!availabilityRepository.existsByAvailableSlots(timeSlot)) {
            throw new ObjectNotFoundException("This time slot: ''" + timeSlot + "'' was not found.");
        } else {
            int index = availabilityRepository.findAvailabilityByAvailableSlotsContaining(timeSlot).getAvailableSlots().indexOf(timeSlot);
            List<LocalDateTime> availableSlots = availabilityRepository
                    .findAvailabilityByAvailableSlotsContaining(timeSlot).getAvailableSlots();
            availableSlots.remove(index);
        }
        availabilityRepository.deleteByAvailableSlots(timeSlot);
        return "Time slot deleted";
    }

    // VALIDATE if time slot exists for the caregiver that made the request
    public void validateCaregiversTimeSlots(String caregiverId, LocalDateTime timeslot) {
        List<Availability> caregiversAvailabilities = availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId);

        for (Availability a : caregiversAvailabilities) {
            if (a.getAvailableSlots().contains(timeslot)) {
                throw new IllegalArgumentException("Time slot already exists");
            }
        }
        for (Availability a : caregiversAvailabilities) {
            if (a.getAvailableSlots().toString().contains(timeslot.toLocalDate().toString())) {
                addTimeSlot(a.getId(), timeslot);
                return;
            }
        }
        List<LocalDateTime> availableSlots = new ArrayList<>();
        availableSlots.add(timeslot);
        createAvailability(caregiverId, availableSlots);
    }

    // ADD new timeslot to existing availability
    public void addTimeSlot(String availabilityId, LocalDateTime timeSlot) {
        Availability availability = availabilityRepository.findAvailabilityById(availabilityId);
        availability.getAvailableSlots().add(timeSlot);
        availabilityRepository.save(availability);
    }
}
