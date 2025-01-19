package health.care.booking.controllers;


import health.care.booking.dto.AvailabilityDTO;
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

//skall det vara Restcontroller och RequestMapping anotations här?
@RestController
@RequestMapping(value ="/availability")
public class AvailabilityController {
    //Autowirea availabilityService och AvailabilityRepository?
    @Autowired
    AvailabilityService availabilityService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AvailabilityRepository availabilityRepository;


    @PostMapping()
    //@PreAuthorize("hasRole('ADMIN')")
    // OBS create error handling for entered availability is not already existing
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

    @PatchMapping("/addtimeslot")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addTimeSlot(@RequestParam String caregiverId, @RequestParam LocalDateTime timeSlot) {
        try {
      //      availabilityService.validateCaregiversTimeSlots(caregiverId, timeSlot);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Time slot added: '" + timeSlot + "' ");
    }

    // PUT - Update availability
    // A caregiver can change the time or date on the availability.
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAvailability
    (@RequestParam String availabilityId, @RequestParam LocalDateTime oldDate, @RequestParam LocalDateTime newDate) {
        try {
            Availability updatedAvailability = availabilityService.updateAvailability(availabilityId, oldDate, newDate);
            return ResponseEntity.ok(updatedAvailability);
        } catch (ObjectNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    //DELETE - Two Delete availability methods, one based on Id and one based on Date
    // DELETE ENTIRE AVAILABILITY BASED ON ID
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAvailability(@RequestParam String availabilityId) {
        try {
            return ResponseEntity.ok(availabilityService.deleteAvailability(availabilityId));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE
    // DELETE a SPECIFIC timeslot based on the caregiverID and the timeSlot entered
    @DeleteMapping("/delete/timeslot")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTimeslot(@RequestParam String caregiverId, @RequestParam LocalDateTime timeSlot) {
        try {
            return ResponseEntity.ok(availabilityService.deleteTimeSlot(caregiverId, timeSlot));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //GET hämta alla availabilities för specifik vårdgivare baserat på userId
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllAvailabilitiesByCaregiverId(@PathVariable String id) {
            List<Availability> foundAvailabilities = availabilityService.getAllAvailabilitiesByCaregiverId(id);
            return ResponseEntity.ok(foundAvailabilities);
    }
}
