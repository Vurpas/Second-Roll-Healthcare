package health.care.booking.controllers;

import health.care.booking.exceptions.UserNotFoundException;
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
    public ResponseEntity<Availability> createAvailability(@RequestParam String caregiverId, @RequestBody List<LocalDateTime> availabilitySlots){
        Availability availability = availabilityService.createAvailability(caregiverId, availabilitySlots);
        return ResponseEntity.ok(availability);
    }
    //preAuthorized? för att se om user är ADMIN?

    //GET hämta alla availabilities
    @GetMapping()
    public ResponseEntity<List<Availability>> getAllAvailabilities(){
        return ResponseEntity.ok(availabilityService.getAllAvailabilities());
    }


    //PUT uppdatera availability

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
