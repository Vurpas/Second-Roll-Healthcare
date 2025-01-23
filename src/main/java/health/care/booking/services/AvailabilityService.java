package health.care.booking.services;


import health.care.booking.dto.AvailabilityDTO;
import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
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
    @Autowired
    AppointmentRepository appointmentRepository;


    //POST Create availability

    public Availability createAvailability (AvailabilityDTO availabilityDTO) {

        User caregiver = userRepository.findById(availabilityDTO.getCaregiverId())
                .orElseThrow(() -> new IllegalArgumentException("Caregiver with ID " + availabilityDTO.getCaregiverId() + " not found"));

        Availability availability = new Availability();
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(availabilityDTO.getAvailableSlots());

        return availabilityRepository.save(availability);
    }

    //UPDATE
    //uppdatera availabilities baserat på id
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

    public List<Availability> getAllAvailabilitiesByCaregiverId(String caregiverId) {
        userRepository.findById(caregiverId)
                .orElseThrow(() -> new IllegalArgumentException("Caregiver not found"));
        return availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId);
    }

    // Check if the time slot exists and is available
    public boolean isSlotAvailable(String caregiverId, LocalDateTime slot) {
        return availabilityRepository
                .findByCaregiverIdAndAvailableSlotsContaining(caregiverId, slot)
                .isPresent();
    }

    // DELETE time slot
    public void removeTimeSlot(String caregiverId, LocalDateTime slot) {
        Availability availability = availabilityRepository
                .findByCaregiverIdAndAvailableSlotsContaining(caregiverId, slot)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        // If time slot is found, remove from the availability
        availability.getAvailableSlots().remove(slot);
        availabilityRepository.save(availability);
    }
}

