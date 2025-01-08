package health.care.booking.controllers;

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
@RequestMapping(value ="/availabilities")
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
    public ResponseEntity<Availability> createAvailability(@RequestParam String caregiverId, @RequestBody List<LocalDateTime> availabilitySlots){
        Availability availability = availabilityService.createAvailability(caregiverId, availabilitySlots);
        return ResponseEntity.ok(availability);
    }
    //preAuthorized? för att se om user är ADMIN?

    //GET hämta alla availabilities


    //PUT uppdatera availability

    //GET hämta availability baserat på id
    //behövs det och isåfall när?

    //DELETE ta bort availability baserat på id

    //GET hämta alla availabilities för specifik vårdgivare baserat på userId
}
