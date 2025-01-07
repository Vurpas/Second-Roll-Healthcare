package health.care.booking.services;

import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailablityService {

    @Autowired
    UserRepository userRepository;


    //POST
    //skapa create availability metod
    //behöver caregiverId, och available slots

    //GET
    //hämta alla availabilities

    //UPDATE
    //uppdatera availabilities baserat på id

    //GET
    //hämta availabilities baserat på caregiverId(userId)

    //DELETE
    //ta bort availability baserat på id

}
