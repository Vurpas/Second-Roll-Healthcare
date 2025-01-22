package health.care.booking.services;

import health.care.booking.models.Availability;
import health.care.booking.respository.NewAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class NewAvailabilityService {
    @Autowired
    private NewAvailabilityRepository newAvailabilityRepository;
    @Autowired
    private NewAppointmentService newAppointmentService;

    // tydlig separation of concerns inte EN metod som gör allt
    // utan varje metod utför en uppgift

    // hjälpmetod men ej privat eftersom den här används i den nya BookingCoordinatior servicen
    // för att ta bort en slit behövs ett caregiverId och en slot av typen LocalDateTime
    public void removeTimeSlot(String caregiverId, LocalDateTime slot) {
        Availability availability = newAvailabilityRepository
                // använder custom metoden här
                .findByCaregiverIdAndAvailableSlotsContaining(caregiverId, slot)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));


        // hittas sloten så tar vi helt enkelt bara bort den från availabilityn
        availability.getAvailableSlots().remove(slot);
        // sen ser vi till att spara
        newAvailabilityRepository.save(availability);
    }

    // en anna hjälpmetod som används i den nya BookingCoordinatior servicen
    // kollar om sloten finns och är tillgänglig
    public boolean isSlotAvailable(String caregiverId, LocalDateTime slot) {
        return newAvailabilityRepository
                .findByCaregiverIdAndAvailableSlotsContaining(caregiverId, slot)
                .isPresent();
    }
}
