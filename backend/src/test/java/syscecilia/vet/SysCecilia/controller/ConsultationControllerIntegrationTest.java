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
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.model.ConsultationReasonType;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.mvc.problem-details.enabled=false")
@DisplayName("ConsultationController Integration Tests")
@Transactional
public class ConsultationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    private Animal animal1;
    private Animal animal2;
    private Consultation consultation1;
    private Consultation consultation2;
    private Consultation consultation3;
    private Consultation consultation4;

    @BeforeEach
    public void setUp() {
        // Limpar dados anteriores
        consultationRepository.deleteAll();
        animalRepository.deleteAll();

        // Criar animals
        animal1 = new Animal();
        animal1.setName("Rex");
        animal1.setSpecies("Dog");
        animal1.setBreed("Golden Retriever");
        animal1.setGender("Male");
        animal1.setOwnerName("John Doe");
        animal1 = animalRepository.save(animal1);

        animal2 = new Animal();
        animal2.setName("Luna");
        animal2.setSpecies("Cat");
        animal2.setBreed("Siamese");
        animal2.setGender("Female");
        animal2.setOwnerName("Jane Smith");
        animal2 = animalRepository.save(animal2);

        // Criar consultations com datas em intervalos específicos para testes
        consultation1 = new Consultation();
        consultation1.setAnimal(animal1);
        consultation1.setConsultationDate(LocalDateTime.of(2025, 11, 10, 10, 0));
        consultation1.setVeterinarianName("Dr. Silva");
        consultation1.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        consultation1.setDescription("General health examination");
        consultation1.setDiagnosis("Healthy");
        consultation1.setStatus("COMPLETED");
        consultation1.setCreatedAt(LocalDateTime.of(2025, 11, 10, 9, 0));
        consultation1 = consultationRepository.save(consultation1);

        consultation2 = new Consultation();
        consultation2.setAnimal(animal1);
        consultation2.setConsultationDate(LocalDateTime.of(2025, 11, 12, 14, 30));
        consultation2.setVeterinarianName("Dr. Santos");
        consultation2.setReasonCode(ConsultationReasonType.VACCINATION.getId());
        consultation2.setDescription("Annual vaccination applied");
        consultation2.setDiagnosis("Vaccinated");
        consultation2.setStatus("COMPLETED");
        consultation2.setCreatedAt(LocalDateTime.of(2025, 11, 12, 13, 0));
        consultation2 = consultationRepository.save(consultation2);

        consultation3 = new Consultation();
        consultation3.setAnimal(animal2);
        consultation3.setConsultationDate(LocalDateTime.of(2025, 11, 16, 11, 0));
        consultation3.setVeterinarianName("Dr. Silva");
        consultation3.setReasonCode(ConsultationReasonType.EXAMS.getId());
        consultation3.setDescription("Dental procedure");
        consultation3.setDiagnosis("Cleaned");
        consultation3.setStatus("COMPLETED");
        consultation3.setCreatedAt(LocalDateTime.of(2025, 11, 16, 10, 0));
        consultation3 = consultationRepository.save(consultation3);

        consultation4 = new Consultation();
        consultation4.setAnimal(animal1);
        consultation4.setConsultationDate(LocalDateTime.of(2025, 12, 1, 14, 0));
        consultation4.setVeterinarianName("Dr. Costa");
        consultation4.setReasonCode(ConsultationReasonType.FOLLOW_UP.getId());
        consultation4.setDescription("Future appointment");
        consultation4.setStatus("SCHEDULED");
        consultation4.setCreatedAt(LocalDateTime.of(2025, 11, 20, 10, 0));
        consultation4 = consultationRepository.save(consultation4);
    }

    @Test
    @DisplayName("Should find consultation by ID")
    public void testFindById() throws Exception {
        mockMvc.perform(get("/api/consultations/{id}", consultation1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(consultation1.getId().intValue())))
                .andExpect(jsonPath("$.animal.name", is("Rex")))
                .andExpect(jsonPath("$.veterinarianName", is("Dr. Silva")))
                .andExpect(jsonPath("$.reason", is(ConsultationReasonType.GENERAL_CHECKUP.getDescription())))
                .andExpect(jsonPath("$.reasonCode", is(ConsultationReasonType.GENERAL_CHECKUP.getId())));
    }

    @Test
    @DisplayName("Should return 404 when consultation ID not found")
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/consultations/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should list all consultations with pagination")
    public void testFindAllWithPagination() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.totalElements", is(4)))
                .andExpect(jsonPath("$.totalPages", is(1)));
    }

    @Test
    @DisplayName("Should filter consultations by animal name")
    public void testFilterByAnimalName() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("animalName", "Rex")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].animal.name", is("Rex")))
                .andExpect(jsonPath("$.content[1].animal.name", is("Rex")))
                .andExpect(jsonPath("$.content[2].animal.name", is("Rex")));
    }

    @Test
    @DisplayName("Should filter consultations by animal name (case-insensitive)")
    public void testFilterByAnimalNameCaseInsensitive() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("animalName", "rex")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].animal.name", is("Rex")));
    }

    @Test
    @DisplayName("Should filter consultations by partial animal name")
    public void testFilterByAnimalNamePartial() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("animalName", "Lun")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].animal.name", is("Luna")));
    }

    @Test
    @DisplayName("Should filter consultations by owner name")
    public void testFilterByOwnerName() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("ownerName", "John Doe")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].animal.ownerName", is("John Doe")))
                .andExpect(jsonPath("$.content[1].animal.ownerName", is("John Doe")))
                .andExpect(jsonPath("$.content[2].animal.ownerName", is("John Doe")));
    }

    @Test
    @DisplayName("Should filter consultations by veterinarian name")
    public void testFilterByVeterinarianName() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("veterinarianName", "Dr. Silva")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].veterinarianName", is("Dr. Silva")))
                .andExpect(jsonPath("$.content[1].veterinarianName", is("Dr. Silva")));
    }

    @Test
    @DisplayName("Should filter consultations by status")
    public void testFilterByStatus() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("status", "COMPLETED")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].status", is("COMPLETED")))
                .andExpect(jsonPath("$.content[1].status", is("COMPLETED")))
                .andExpect(jsonPath("$.content[2].status", is("COMPLETED")));
    }

    @Test
    @DisplayName("Should filter consultations by reason")
    public void testFilterByReason() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("reason", ConsultationReasonType.VACCINATION.getDescription())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].reason", is(ConsultationReasonType.VACCINATION.getDescription())))
                .andExpect(jsonPath("$.content[0].reasonCode", is(ConsultationReasonType.VACCINATION.getId())));
    }

    @Test
    @DisplayName("Should filter consultations by description (partial match)")
    public void testFilterByDescription() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("description", "examination")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].description", is("General health examination")));
    }

    @Test
    @DisplayName("Should filter consultations by creation date range")
    public void testFilterByDateRange() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("createdAtStart", "2025-11-10T00:00:00")
                .param("createdAtEnd", "2025-11-30T23:59:59")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @Test
    @DisplayName("Should apply multiple filters simultaneously")
    public void testMultipleFiltersSimultaneously() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("animalName", "Rex")
                .param("veterinarianName", "Dr. Silva")
                .param("reason", ConsultationReasonType.GENERAL_CHECKUP.getDescription())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].animal.name", is("Rex")))
                .andExpect(jsonPath("$.content[0].veterinarianName", is("Dr. Silva")))
                .andExpect(jsonPath("$.content[0].reason", is(ConsultationReasonType.GENERAL_CHECKUP.getDescription())))
                .andExpect(jsonPath("$.content[0].reasonCode", is(ConsultationReasonType.GENERAL_CHECKUP.getId())));
    }

    @Test
    @DisplayName("Should apply all filters together")
    public void testAllFiltersAppliedTogether() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("animalName", "Rex")
                .param("ownerName", "John")
                .param("veterinarianName", "Dr. Silva")
                .param("status", "COMPLETED")
                .param("reason", ConsultationReasonType.GENERAL_CHECKUP.getDescription())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].animal.name", is("Rex")))
                .andExpect(jsonPath("$.content[0].reasonCode", is(ConsultationReasonType.GENERAL_CHECKUP.getId())));
    }

    @Test
    @DisplayName("Should return empty result when no matches found")
    public void testNoResultsFound() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("animalName", "NonExistent")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    @DisplayName("Should support pagination with different page sizes")
    public void testPaginationWithDifferentPageSizes() throws Exception {
        // Página 1 com 2 itens
        mockMvc.perform(get("/api/consultations")
                .param("page", "0")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.number", is(0)));

        // Página 2
        mockMvc.perform(get("/api/consultations")
                .param("page", "1")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Test
    @DisplayName("Should sort by consultation date descending")
    public void testSortByConsultationDateDesc() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("sort", "consultationDate,desc")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].reason", is(ConsultationReasonType.FOLLOW_UP.getDescription())))
                .andExpect(jsonPath("$.content[1].reason", is(ConsultationReasonType.EXAMS.getDescription())))
                .andExpect(jsonPath("$.content[2].reason", is(ConsultationReasonType.VACCINATION.getDescription())))
                .andExpect(jsonPath("$.content[3].reason", is(ConsultationReasonType.GENERAL_CHECKUP.getDescription())));
    }

    @Test
    @DisplayName("Should sort by consultation date ascending")
    public void testSortByConsultationDateAsc() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("sort", "createdAt,asc")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].reason", is(ConsultationReasonType.GENERAL_CHECKUP.getDescription())))
                .andExpect(jsonPath("$.content[1].reason", is(ConsultationReasonType.VACCINATION.getDescription())))
                .andExpect(jsonPath("$.content[2].reason", is(ConsultationReasonType.EXAMS.getDescription())))
                .andExpect(jsonPath("$.content[3].reason", is(ConsultationReasonType.FOLLOW_UP.getDescription())));
    }

    @Test
    @DisplayName("Should handle complex filter and pagination together")
    public void testComplexFilterAndPaginationTogether() throws Exception {
        mockMvc.perform(get("/api/consultations")
                .param("veterinarianName", "Dr. Silva")
                .param("page", "0")
                .param("size", "1")
                .param("sort", "consultationDate,desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.content[0].reason", is(ConsultationReasonType.EXAMS.getDescription())));
    }

    @Test
    @DisplayName("Should cancel consultation successfully")
    public void testCancelConsultation() throws Exception {
        mockMvc.perform(patch("/api/consultations/{id}/cancel", consultation4.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(consultation4.getId().intValue())))
                .andExpect(jsonPath("$.status", is("CANCELLED")));

        // Verificar que o status foi realmente alterado no banco
        Consultation updated = consultationRepository.findById(consultation4.getId()).orElseThrow();
        assertEquals("CANCELLED", updated.getStatus());
    }

    @Test
    @DisplayName("Should return 404 when consultation not found for cancellation")
    public void testCancelConsultationNotFound() throws Exception {
        mockMvc.perform(patch("/api/consultations/{id}/cancel", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 422 when trying to cancel already cancelled consultation")
    public void testCancelConsultationAlreadyCancelled() throws Exception {
        // Primeiro cancelar a consulta
        consultation4.setStatus("CANCELLED");
        consultationRepository.save(consultation4);

        mockMvc.perform(patch("/api/consultations/{id}/cancel", consultation4.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return 422 when trying to cancel completed consultation")
    public void testCancelConsultationCompleted() throws Exception {
        mockMvc.perform(patch("/api/consultations/{id}/cancel", consultation1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }
}

