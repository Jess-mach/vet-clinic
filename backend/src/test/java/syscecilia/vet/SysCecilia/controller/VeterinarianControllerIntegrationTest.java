package syscecilia.vet.SysCecilia.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.model.Veterinarian;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.repository.VeterinarianRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.mvc.problem-details.enabled=false")
@DisplayName("VeterinarianController Integration Tests")
@Transactional
public class VeterinarianControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private AnimalRepository animalRepository;

    private Veterinarian vet1;
    private Veterinarian vet2;
    private Veterinarian vet3;
    private Veterinarian vet4;
    private Animal animal;

    @BeforeEach
    public void setUp() {
        // Clear existing data
        consultationRepository.deleteAll();
        animalRepository.deleteAll();
        veterinarianRepository.deleteAll();

        animal = new Animal();
        animal.setName("Luna");
        animal.setSpecies("Canine");
        animal.setGender("Female");
        animal.setOwnerName("Alice Johnson");
        animal = animalRepository.save(animal);

        // Create test veterinarians
        vet1 = new Veterinarian();
        vet1.setName("Dr. Amelia Rivers");
        vet1.setSpecialtyCode(2); // Ophthalmology
        vet1 = veterinarianRepository.save(vet1);

        vet2 = new Veterinarian();
        vet2.setName("Dr. Noah Bennett");
        vet2.setSpecialtyCode(1); // General Checkup
        vet2 = veterinarianRepository.save(vet2);

        vet3 = new Veterinarian();
        vet3.setName("Dr. Olivia Carter");
        vet3.setSpecialtyCode(3); // Cardiology
        vet3 = veterinarianRepository.save(vet3);

        vet4 = new Veterinarian();
        vet4.setName("Dr. Sophia Hayes");
        vet4.setSpecialtyCode(1); // General Checkup
        vet4 = veterinarianRepository.save(vet4);
    }

    @Test
    @DisplayName("GET /api/veterinarians - Should return all veterinarians sorted by name")
    void testGetAllVeterinarians_Success() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("Dr. Amelia Rivers")))
                .andExpect(jsonPath("$[0].specialtyCode", is(2)))
                .andExpect(jsonPath("$[0].specialty", is("Consulta com oftalmologista")))
                .andExpect(jsonPath("$[1].name", is("Dr. Noah Bennett")))
                .andExpect(jsonPath("$[1].specialtyCode", is(1)))
                .andExpect(jsonPath("$[1].specialty", is("Consulta com clinico geral")))
                .andExpect(jsonPath("$[2].name", is("Dr. Olivia Carter")))
                .andExpect(jsonPath("$[3].name", is("Dr. Sophia Hayes")));
    }

    @Test
    @DisplayName("GET /api/veterinarians?name=Amelia - Should filter veterinarians by name")
    void testGetVeterinariansByName_Success() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .param("name", "Amelia")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Amelia Rivers")))
                .andExpect(jsonPath("$[0].specialtyCode", is(2)))
                .andExpect(jsonPath("$[0].specialty", is("Consulta com oftalmologista")));
    }

    @Test
    @DisplayName("GET /api/veterinarians?name=Dr - Should filter veterinarians by partial name match")
    void testGetVeterinariansByPartialName_Success() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .param("name", "Dr")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("GET /api/veterinarians?specialtyCode=1 - Should filter veterinarians by specialty")
    void testGetVeterinariansBySpecialty_Success() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .param("specialtyCode", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Dr. Noah Bennett")))
                .andExpect(jsonPath("$[0].specialtyCode", is(1)))
                .andExpect(jsonPath("$[0].specialty", is("Consulta com clinico geral")))
                .andExpect(jsonPath("$[1].name", is("Dr. Sophia Hayes")))
                .andExpect(jsonPath("$[1].specialtyCode", is(1)));
    }

    @Test
    @DisplayName("GET /api/veterinarians?name=Olivia&specialtyCode=3 - Should filter by both name and specialty")
    void testGetVeterinariansByNameAndSpecialty_Success() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .param("name", "Olivia")
                        .param("specialtyCode", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Olivia Carter")))
                .andExpect(jsonPath("$[0].specialtyCode", is(3)))
                .andExpect(jsonPath("$[0].specialty", is("Consulta com cardiologista")));
    }

    @Test
    @DisplayName("GET /api/veterinarians?name=NonExistent - Should return empty list when no match found")
    void testGetVeterinariansByName_NoMatch_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .param("name", "NonExistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/veterinarians?specialtyCode=99 - Should return empty list when specialty not found")
    void testGetVeterinariansBySpecialty_NoMatch_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .param("specialtyCode", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/veterinarians - Should return veterinarians with all required fields")
    void testGetAllVeterinarians_VerifyAllFields() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].specialtyCode", notNullValue()))
                .andExpect(jsonPath("$[0].specialty", notNullValue()))
                .andExpect(jsonPath("$[0].createdAt", notNullValue()))
                .andExpect(jsonPath("$[0].updatedAt", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/veterinarians - Should handle case-insensitive name search")
    void testGetVeterinariansByName_CaseInsensitive_Success() throws Exception {
        mockMvc.perform(get("/api/veterinarians")
                        .param("name", "amelia")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Amelia Rivers")));
    }

    @Test
    @DisplayName("GET /api/veterinarians/{id}/availability - Should return full business intervals when no bookings exist")
    void testGetAvailability_NoBookings() throws Exception {
        LocalDate referenceDate = LocalDate.of(2025, 11, 24); // Monday

        mockMvc.perform(get("/api/veterinarians/{id}/availability", vet1.getId())
                        .param("date", referenceDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("2025-11-24")))
                .andExpect(jsonPath("$[0].startTime", is("08:00:00")))
                .andExpect(jsonPath("$[0].endTime", is("12:00:00")))
                .andExpect(jsonPath("$[1].startTime", is("13:00:00")))
                .andExpect(jsonPath("$[1].endTime", is("18:00:00")));
    }

    @Test
    @DisplayName("GET /api/veterinarians/{id}/availability - Should remove booked slots from availability")
    void testGetAvailability_WithBooking() throws Exception {
        LocalDate referenceDate = LocalDate.of(2025, 11, 24);
        scheduleConsultation(vet1, referenceDate, 10);

        mockMvc.perform(get("/api/veterinarians/{id}/availability", vet1.getId())
                        .param("date", referenceDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].startTime", is("08:00:00")))
                .andExpect(jsonPath("$[0].endTime", is("10:00:00")))
                .andExpect(jsonPath("$[1].startTime", is("11:00:00")))
                .andExpect(jsonPath("$[1].endTime", is("12:00:00")))
                .andExpect(jsonPath("$[2].startTime", is("13:00:00")))
                .andExpect(jsonPath("$[2].endTime", is("18:00:00")));
    }

    @Test
    @DisplayName("GET /api/veterinarians/{id}/availability - Should skip weekends")
    void testGetAvailability_SkipsWeekends() throws Exception {
        mockMvc.perform(get("/api/veterinarians/{id}/availability", vet2.getId())
                        .param("date", "2025-11-22") // Saturday
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date", is("2025-11-24")))
                .andExpect(jsonPath("$[0].startTime", is("08:00:00")));
    }

    private void scheduleConsultation(Veterinarian veterinarian, LocalDate date, int hour) {
        Consultation consultation = new Consultation();
        consultation.setAnimal(animal);
        consultation.setVeterinarian(veterinarian);
        consultation.setConsultationDate(LocalDateTime.of(date, LocalTime.of(hour, 0)));
        consultation.setReasonCode(1);
        consultation.setStatus("SCHEDULED");
        consultationRepository.save(consultation);
    }
}

