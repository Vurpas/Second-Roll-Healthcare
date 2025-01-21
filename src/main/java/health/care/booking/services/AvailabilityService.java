package health.care.booking.services;


import health.care.booking.dto.AvailabilityDTO;
import health.care.booking.exceptions.ObjectNotFoundException;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
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
    @Autowired
    AppointmentRepository appointmentRepository;


    //POST
    //skapa create availability metod
    //behöver caregiverId, och available slots

    // OBS create error handling for unorthorized attempts to create availability
    // and check that entered availability is not already excisting! OBS
   public Availability createAvailability (AvailabilityDTO availabilityDTO) {

      User caregiver =  userRepository.findById(availabilityDTO.getCaregiverId())
                .orElseThrow(() -> new IllegalArgumentException("Caregiver with ID " + availabilityDTO.getCaregiverId() + " not found"));

        Availability availability = new Availability();
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(availabilityDTO.getAvailableSlots());

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
    // First it gets all the availabilities linked to a caregiver, then looks if there's an exact copy of the timeslot
    // If not, the next for loop checks the list of available slots on the same date
    // If there is nothing on that date, it creates the availability, otherwise it just adds to the existing
    // availability with the same date and caregiver
    public void validateCaregiversTimeSlots(String caregiverId, LocalDateTime timeslot) {
        // Från Helena!
        // HUVUDPROBLEM: @RequestParam används i controllern vilket gör att denna metod måste
        // köras en gång PER timeslot... leder till följande problem:

        // PROBLEM 1: för VARJE timeslot som ska läggas till görs en full hämtning
        // av ALL availability-data för caregiver
        // det här gör en full collection scan och hämtar all data när vi egentligen
        // bara behöver kolla ett specifikt datum
        List<Availability> caregiversAvailabilities = availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId);
        User user = userRepository.findUserById(caregiverId);

        // PROBLEM 2: en separat appointment koll måste göras för VARJE timeslot
        // så om man vill lägga till 10 tider = 10 queries........inte så bra.
        // finns ingen anledning att kolla appointments här eftersom:
        // 1. Appointments och Availabilities är separata koncept
        // 2. skapar en onödig koppling mellan de två modellerna
        Appointment appointment = appointmentRepository.findAppointmentByCaregiverIdAndDateTime(user, timeslot);
        if (appointment != null && appointment.getDateTime().equals(timeslot)) {
            throw new IllegalArgumentException("Time slot already exists in a booked appointment");
        }

        // PROBLEM 3: för VARJE timeslot som ska läggas till måste HELA listan av
        // availabilities loopas igenom TVÅ gånger.
        // med separata requests blir detta extremt ineffektivt...
        // kan lösas med en enda databas query
        for (Availability a : caregiversAvailabilities) {
            if (a.getAvailableSlots().contains(timeslot)) {
                throw new IllegalArgumentException("Time slot already exists");
            }
        }

        // PROBLEM 4: toString()-jämförelse är extremt riskabel och måste göras
        // för VARJE timeslot. risk för felaktiga matchningar ökar med antalet requests..
        for (Availability a : caregiversAvailabilities) {
            if (a.getAvailableSlots().toString().contains(timeslot.toLocalDate().toString())) {
                addTimeSlot(a.getId(), timeslot);
                return;
            }

            // använda toString() för datumjämförelse är riskabelt framförallt pga:
            // 1. när du kör toString() på en lista med datum får du en sträng med formatet "[2024-01-19T13:00:00]".
            // att använda contains() på denna sträng är extremt osäkert eftersom det kan ge falska positiva träffar
            // - allt som innehåller datumet kommer matcha, även om det är fel format eller ogiltiga tider.

            // 2. metoden förlitar sig på Javas interna implementation av hur List.toString() och LocalDateTime.toString()
            // formaterar sina strängar. detta kan ändras mellan Java-versioner vilket gör koden skör.

            // 3. man bör alltid jämföra datum med riktiga datumjämförelser (equals() eller isEqual()) istället för strängmanipulation.
            // exempel: timeSlot.toLocalDate().equals(availableSlot.toLocalDate())


        }

        // PROBLEM 5: eftersom varje timeslot hanteras separat finns risk för race conditions:
        // - REQUEST 1: hittar ingen availability för datum X -> börjar skapa ny
        // - REQUEST 2: hittar ingen availability för datum X -> börjar skapa ny
        // - resultat: två dokument för samma datum

        // här KAN faktiskt två dokument med samma datum för en caregiver skapas... så kallat RACE CONDITION
        // det är en direkt konsekvens av:
        // 1. användningen av @RequestParam som tvingar en-och-en hantering
        // 2. den osäkra toString()-jämförelsen
        // 3. saknar unik constraint i databasen för kombinationen caregiverId + datum
        List<LocalDateTime> availableSlots = new ArrayList<>();
        availableSlots.add(timeslot);
        //createAvailability(caregiverId, availableSlots);
    }

    // ADD new timeslot to existing availability
    public void addTimeSlot(String availabilityId, LocalDateTime timeSlot) {
        Availability availability = availabilityRepository.findAvailabilityById(availabilityId);
        availability.getAvailableSlots().add(timeSlot);
        availabilityRepository.save(availability);
    }

    public List<Availability> getAllAvailabilitiesByCaregiverId(String caregiverId) {
        if (!userRepository.existsById(caregiverId)) {
            throw new ObjectNotFoundException("The input ID does not match any caregiver");
        } else if (availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId).isEmpty()) {
            throw new ObjectNotFoundException("No availabilites found for this caregiver ID");
        }
        return availabilityRepository.findAvailabilitiesByCaregiverId(caregiverId);
    }
}
