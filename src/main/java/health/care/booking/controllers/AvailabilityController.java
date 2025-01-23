package health.care.booking.controllers;


import health.care.booking.dto.AvailabilityDTO;
import health.care.booking.dto.RemoveTimeSlotRequest;
import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Availability;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value ="/availability")
public class AvailabilityController {
    @Autowired
    AvailabilityService availabilityService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AvailabilityRepository availabilityRepository;

    // POST: Create availability
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Availability> createAvailability(@RequestBody AvailabilityDTO availabilityDTO){
        Availability availability = availabilityService.createAvailability(availabilityDTO);
        return ResponseEntity.ok(availability);
    }

    //GET all availabilities
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllAvailabilities() {
        List<Availability> allAvailabilities = availabilityService.getAllAvailabilities();
        return ResponseEntity.ok(allAvailabilities);
    }

    // PUT - Update availability
    // A caregiver can change the time or date on the availability.
    @PutMapping("/update/{availabilityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAvailability
    (@PathVariable String availabilityId, @RequestBody LocalDateTime oldDate, @RequestBody LocalDateTime newDate) {
        try {
            Availability updatedAvailability = availabilityService.updateAvailability(availabilityId, oldDate, newDate);
            return ResponseEntity.ok(updatedAvailability);
        } catch (ObjectNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    //DELETE - Two Delete availability methods, one based on Id and one based on Date
    // DELETE ENTIRE AVAILABILITY BASED ON ID
    @DeleteMapping("/deleteavailability/{availabilityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAvailability(@PathVariable String availabilityId) {
        try {
            return ResponseEntity.ok(availabilityService.deleteAvailability(availabilityId));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE delete single timeslot, needed in caregiver Calendar
    @DeleteMapping("/removetimeslot")
    public ResponseEntity<String> removeTimeslot(@RequestBody RemoveTimeSlotRequest removeTimeSlotRequest) {
 try{
     availabilityService.removeTimeSlot(removeTimeSlotRequest.getCaregiverId(), removeTimeSlotRequest.getSelectedSlot());
     return ResponseEntity.ok("Timeslot successfully removed ");
 } catch (IllegalArgumentException e) {
     return ResponseEntity.badRequest().body(e.getMessage());
 }catch (Exception e) {
     return ResponseEntity.internalServerError().body("unexpected error occurred");
 }
    }

    @GetMapping("/{caregiverId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllAvailabilitiesByCaregiverId(@PathVariable String caregiverId) {
        List<Availability> foundAvailabilities = availabilityService.getAllAvailabilitiesByCaregiverId(caregiverId);
        return ResponseEntity.ok(foundAvailabilities);
    }
}
