
package health.care.booking.services;


/*
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentRequest validAppointmentRequest;
    private AppointmentRequest invalidAppointmentRequest;

    private Availability availability;
    private User patient;

    private User caregiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        // Setup valid AppointmentRequest with available date
        validAppointmentRequest = new AppointmentRequest(LocalDateTime.of(2025, 1, 20, 10, 0), "100", "1", "Runny nose");
        validAppointmentRequest.setAvailabilityId("1");
        validAppointmentRequest.setPatientId("100");
        validAppointmentRequest.setAppointmentDate(LocalDateTime.of(2025, 1, 20, 10, 0));

        // Setup Invalid AppointmentRequest (non-existent availability)
        invalidAppointmentRequest = new AppointmentRequest(LocalDateTime.of(2025, 1, 20, 10, 0), "100","999", "Nothing");
        invalidAppointmentRequest.setAvailabilityId("999"); // Invalid ID
        invalidAppointmentRequest.setPatientId("100");
        invalidAppointmentRequest.setAppointmentDate(LocalDateTime.of(2025, 1, 20, 10, 0));

        // Mock the Availability and User repositories
        availability = new Availability();
        availability.setId("1");
        availability.setCaregiverId(caregiver);
        availability.setAvailableSlots(List.of(LocalDateTime.of(2025, 1, 20, 10, 0), LocalDateTime.of(2025, 1, 20, 11, 0)));

        patient = new User();
        patient.setId("100");

        caregiver = new User();
        caregiver.setId("100");


        when(availabilityRepository.findById("1")).thenReturn(Optional.of(availability));
        when(userRepository.findById("100")).thenReturn(Optional.of(patient));
    }

    @Test
    void testCreateAppointmentSuccess() {
        // Arrange: Mock the saving of the appointment
        Appointment createdAppointment = new Appointment();
        createdAppointment.setCaregiverId(caregiver);
        createdAppointment.setDateTime(validAppointmentRequest.getAppointmentDate());
        createdAppointment.setStatus(Status.SCHEDULED);
        createdAppointment.setPatientId(patient);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(createdAppointment);

        // Act: Create the appointment
        Appointment appointment = appointmentService.createAppointment(validAppointmentRequest);

        // Assert: Check if the appointment was successfully created
        assertNotNull(appointment);
        assertEquals(validAppointmentRequest.getAppointmentDate(), appointment.getDateTime());
        assertEquals(Status.SCHEDULED, appointment.getStatus());
        assertEquals(patient, appointment.getPatientId());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
        verify(availabilityRepository, times(1)).save(any(Availability.class)); // Verify availability was updated
    }
        @Test
        void testCreateAppointmentFailureWithNonExistentPatient() {
            // Mock the case where the patient doesn´t exist
        when(userRepository.findById("100")).thenReturn(Optional.empty());

        // Act & Assert: Attempt to create the appointment and expect an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.createAppointment(validAppointmentRequest));
        assertEquals("Patient with ID 100 not found.", exception.getMessage());
        }

        @Test
        void testCreateAppointmentFailureWithInvalidDate() {
            // Arrange: Use an invalid date that is not in the availability slots
            validAppointmentRequest.setAppointmentDate(LocalDateTime.of(2025, 1, 20, 12, 0));

            // Act & Assert: Attempt to create the appointment and expect an exception
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    appointmentService.createAppointment(validAppointmentRequest));
            assertEquals("The specified date does not exist", exception.getMessage());
        }

        @Test
    void testCancelAppointment() {
        String appointmentId = "apt123";

        // Create a mock appointment object
            Appointment mockAppointment = new Appointment();
            mockAppointment.setId(appointmentId);
            mockAppointment.setStatus(Status.SCHEDULED);

            // Mock the repository´s behavior
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(mockAppointment));
            when(appointmentRepository.save(mockAppointment)).thenReturn(mockAppointment);

            // Call the method
            Appointment result = appointmentService.cancelAppointment(appointmentId);

            // Verify that the appointment's status is updated
            assertEquals(Status.CANCELLED, result.getStatus());
            verify(appointmentRepository, times(1)).save(mockAppointment); // Ensure save was called once

        }

        @Test
    void testCancelAppointment_Failure_AppointmentNotFound() {
        String appointmentId = "apt123";

        // Mock the repository to return an empty Optional, simulating a non-existing appointment
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

            // Call the method and assert that the exception is thrown
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                appointmentService.cancelAppointment(appointmentId);
            });

            // Verify the exception message
            assertEquals("Appointment with ID apt123 not found.", exception.getMessage());
            verify(appointmentRepository, never()).save(any(Appointment.class)); // Ensure save was never called
        }
    }
*/


