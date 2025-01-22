package health.care.booking.services;

import health.care.booking.dto.NewAppointmentRequest;
import health.care.booking.models.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookingCoordinatorService {
    @Autowired
    private NewAvailabilityService newAvailabilityService;

    @Autowired
    private NewAppointmentService newAppointmentService;

    // ny klass som hanterar att koordinera en bokning. själva sparandet av en Appointment
    // till databasen sköter NewAppointmentService, den här klassen ser bara till att "sätta ihop"
    // allt.
    public Appointment bookAppointment(NewAppointmentRequest request) {
        // först kollar vi om sloten är tillgänglig
        if (!newAvailabilityService.isSlotAvailable(request.getCaregiverId(), request.getSelectedSlot())) {
            throw new IllegalStateException("Selected slot is no longer available");
        }

        // skapar ett nytt appointment objekt med hjälp av NewAppointmentServicen
        Appointment appointment = newAppointmentService.newAppointment(request);
        // efter att det skapats ser vi till att ta bort sloten från availabilityn
        // det här görs genom NewAvailabilityService
        newAvailabilityService.removeTimeSlot(request.getCaregiverId(), request.getSelectedSlot());

        // vi behåller lite av Mias ursprungliga tanke men vi bygger det löst kopplat istället
        // mycket enklare att testa varje del separat och inga tighta beroenden skapas
        // vi skiter i att sköta all från frontend vilket hade vart en snabb lösning men
        // väldigt riskabelt eftersom det kan innebära race conditions och icke atomär operation.

        return appointment;
    }
}
