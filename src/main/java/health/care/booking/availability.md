## Vad ska ni göra och vilka problem står ni inför?
## Hur ska datan användas?

### 1. Varför är det viktigt att det ska finnas en availability per dag? 
Datan sätts INTE så i frontend. HUR vi använder datan ska bestämma HUR vi ska modellera den.
Man klickar i sig veckovis? eller månadsvis? beror på vilken kalender?

Ska vi bygga kod som är bunden till att man sätter data veckovis eller dagsvis pga en specifik
kalender?
Vad händer om den kalendern går åt skogen? Eller kunde VILL byta?

SVAR: alltså ska vi INTE fokusera på månader dagar whatever. Då blir vårt system extremt 
hårt kopplat.

### 2. Vilka utmaningar har vi egentligen?
1. Det ska inte ligga en massa gamla dokument i databasen med datum som passerat... 
2. det ska vara smidigt att kunna uppdatera en availability
3. när ett möte bokas ska availabilityn tas bort ifrån caregiver

### LÖSNINGAR:
1. En automatisk rensning körs som tar bort allt som är tidigare än dagens datum
2. Man ska få uppdatera som man vill eller ta bort... detta bör ske baserat på AVAILABILITY ID
3. Man skapar en metod som tar bort EN slot ifrån en lista med avaialabilitys detta baseras på AVAILABILITY ID
4. Använder INTE requestparam utan @PathVariable som är best practice och standard för det ni ska göra.
5. För att visa att ni är lite proffs kan vi använda @Transactional som ser till att vi genomför automära operationer
@Transactional säkerställer att en grupp databasoperationer antingen genomförs helt och hållet eller inte alls - detta kallas "atomicitet".


#### skulle behöva göra två custom repository metoder:
1. findByCaregiverIdAndAvailableSlots
2. deleteByAvailableSlotsLessThan
Exempel:
```Optional<Availability> findByCaregiverIdAndAvailableSlots(String caregiverId, LocalDateTime timeSlot);```

```@Query(value = "{'availableSlots': {$lt: ?0}}", delete = true)
    void deleteByAvailableSlotsLessThan(LocalDateTime date);
   ```
MongoDB stödjer cron, kort sagt grejer vi kan köra automatiskt och ställa in när det ska köras:
```@Scheduled(cron = "0 0 1 * * *") 
// Kör 01:00 varje dag
```

Ditt super vapen blir att ALLTID komma ihåg:
# KISS"-principen (Keep It Simple and Stupid)
ofta är den enklaste lösningen den bästa.. men det är absolut inte lätt att komma på enkla lösningar

    
