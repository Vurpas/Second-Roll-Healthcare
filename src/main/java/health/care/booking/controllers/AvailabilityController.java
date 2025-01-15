package health.care.booking.controllers;


import health.care.booking.exceptions.UserNotFoundException;
import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Availability;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    //POST availability
    //postmapping anotation?
    @PostMapping("/create")
    //@PreAuthorize("hasRole('USER')")

    // OBS create error handling for unorthorized attempts to create availability
    // and check that entered availability is not already excisting! OBS
    public ResponseEntity<Availability> createAvailability(@RequestParam String caregiverId, @RequestBody List<LocalDateTime> availableSlots){
        Availability availability = availabilityService.createAvailability(caregiverId, availableSlots);
        return ResponseEntity.ok(availability);
    }
    //preAuthorized? för att se om user är ADMIN?

    //GET hämta alla availabilities

    @GetMapping("/all")
    public ResponseEntity<?> getAllAvailabilities() {
        List<Availability> allAvailabilities = availabilityService.getAllAvailabilities();
        return ResponseEntity.ok(allAvailabilities);
    }

    // PUT - Update availability
    // A caregiver can change the time or date on the availability.
    //
    // TODO: Discuss the future of the availability setup. Maybe one caregiver should have only ONE availability
    //  where the list of availableSlots is being filled/updated by the caretaker, instead of creating a new
    //  availability and a new list for every time the caretaker adds more availableSlots.

    @PutMapping("/update")
    public ResponseEntity<?> updateAvailability
    (@RequestParam String availabilityId, @RequestParam LocalDateTime oldDate, @RequestParam LocalDateTime newDate) {
        try {
            Availability updatedAvailability = availabilityService.updateAvailability(availabilityId, oldDate, newDate);
            return ResponseEntity.ok(updatedAvailability);
        } catch (ObjectNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }
    //GET hämta availability baserat på id
    //behövs det och isåfall när?

    //DELETE - Two Delete availability methods, one based on Id and one based on Date
    @DeleteMapping()
    public ResponseEntity<?> deleteAvailability(@RequestParam String availabilityId) {
        try {
            Availability deleteAvailability = availabilityService.deleteAvailability(availabilityId);
            return ResponseEntity.ok("Availability deleted");
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.badRequest().body("Availability with ID " + availabilityId + " was not found");
        }
    }

    //GET hämta alla availabilities för specifik vårdgivare baserat på userId
}
