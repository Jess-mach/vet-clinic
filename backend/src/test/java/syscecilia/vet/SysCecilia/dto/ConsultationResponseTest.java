package syscecilia.vet.SysCecilia.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConsultationResponse DTO Tests")
class ConsultationResponseTest {

    @Test
    @DisplayName("Should create ConsultationResponse with no-args constructor")
    void shouldCreateConsultationResponseWithNoArgsConstructor() {
        // When
        ConsultationResponse response = new ConsultationResponse();
        
        // Then
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getAnimal());
        assertNull(response.getConsultationDate());
        assertNull(response.getVeterinarianName());
        assertNull(response.getReason());
        assertNull(response.getDescription());
        assertNull(response.getDiagnosis());
        assertNull(response.getTreatmentPrescribed());
        assertNull(response.getObservations());
        assertNull(response.getNextAppointmentDate());
        assertNull(response.getStatus());
        assertNull(response.getCreatedAt());
        assertNull(response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create ConsultationResponse with all args constructor")
    void shouldCreateConsultationResponseWithAllArgsConstructor() {
        // Given
        Long id = 1L;
        AnimalBasicInfo animal = new AnimalBasicInfo(1L, "Rex", "Dog", "Golden Retriever", "John Doe");
        LocalDateTime consultationDate = LocalDateTime.of(2024, 1, 15, 10, 30);
        String veterinarianName = "Dr. Silva";
        String reason = "Routine checkup";
        String description = "Regular health examination";
        String diagnosis = "Healthy";
        String treatmentPrescribed = "Vitamins";
        String observations = "Animal in good health";
        LocalDateTime nextAppointmentDate = LocalDateTime.of(2024, 7, 15, 10, 30);
        String status = "COMPLETED";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        
        // When
        ConsultationResponse response = new ConsultationResponse(
            id, animal, consultationDate, veterinarianName, reason, description,
            diagnosis, treatmentPrescribed, observations, nextAppointmentDate,
            status, createdAt, updatedAt
        );
        
        // Then
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(animal, response.getAnimal());
        assertEquals(consultationDate, response.getConsultationDate());
        assertEquals(veterinarianName, response.getVeterinarianName());
        assertEquals(reason, response.getReason());
        assertEquals(description, response.getDescription());
        assertEquals(diagnosis, response.getDiagnosis());
        assertEquals(treatmentPrescribed, response.getTreatmentPrescribed());
        assertEquals(observations, response.getObservations());
        assertEquals(nextAppointmentDate, response.getNextAppointmentDate());
        assertEquals(status, response.getStatus());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get id")
    void shouldSetAndGetId() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        Long id = 10L;
        
        // When
        response.setId(id);
        
        // Then
        assertEquals(id, response.getId());
    }

    @Test
    @DisplayName("Should set and get animal")
    void shouldSetAndGetAnimal() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        AnimalBasicInfo animal = new AnimalBasicInfo(2L, "Fluffy", "Cat", "Persian", "Jane Smith");
        
        // When
        response.setAnimal(animal);
        
        // Then
        assertEquals(animal, response.getAnimal());
    }

    @Test
    @DisplayName("Should set and get consultation date")
    void shouldSetAndGetConsultationDate() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        LocalDateTime date = LocalDateTime.of(2024, 3, 20, 14, 0);
        
        // When
        response.setConsultationDate(date);
        
        // Then
        assertEquals(date, response.getConsultationDate());
    }

    @Test
    @DisplayName("Should set and get veterinarian name")
    void shouldSetAndGetVeterinarianName() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        String name = "Dr. Johnson";
        
        // When
        response.setVeterinarianName(name);
        
        // Then
        assertEquals(name, response.getVeterinarianName());
    }

    @Test
    @DisplayName("Should set and get reason")
    void shouldSetAndGetReason() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        String reason = "Vaccination";
        
        // When
        response.setReason(reason);
        
        // Then
        assertEquals(reason, response.getReason());
    }

    @Test
    @DisplayName("Should set and get description")
    void shouldSetAndGetDescription() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        String description = "Annual vaccination procedure";
        
        // When
        response.setDescription(description);
        
        // Then
        assertEquals(description, response.getDescription());
    }

    @Test
    @DisplayName("Should set and get diagnosis")
    void shouldSetAndGetDiagnosis() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        String diagnosis = "No issues detected";
        
        // When
        response.setDiagnosis(diagnosis);
        
        // Then
        assertEquals(diagnosis, response.getDiagnosis());
    }

    @Test
    @DisplayName("Should set and get treatment prescribed")
    void shouldSetAndGetTreatmentPrescribed() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        String treatment = "Antibiotic - 10 days";
        
        // When
        response.setTreatmentPrescribed(treatment);
        
        // Then
        assertEquals(treatment, response.getTreatmentPrescribed());
    }

    @Test
    @DisplayName("Should set and get observations")
    void shouldSetAndGetObservations() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        String observations = "Monitor weight gain";
        
        // When
        response.setObservations(observations);
        
        // Then
        assertEquals(observations, response.getObservations());
    }

    @Test
    @DisplayName("Should set and get next appointment date")
    void shouldSetAndGetNextAppointmentDate() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        LocalDateTime nextDate = LocalDateTime.of(2024, 12, 25, 9, 0);
        
        // When
        response.setNextAppointmentDate(nextDate);
        
        // Then
        assertEquals(nextDate, response.getNextAppointmentDate());
    }

    @Test
    @DisplayName("Should set and get status")
    void shouldSetAndGetStatus() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        String status = "SCHEDULED";
        
        // When
        response.setStatus(status);
        
        // Then
        assertEquals(status, response.getStatus());
    }

    @Test
    @DisplayName("Should set and get created at")
    void shouldSetAndGetCreatedAt() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        LocalDateTime createdAt = LocalDateTime.now();
        
        // When
        response.setCreatedAt(createdAt);
        
        // Then
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updated at")
    void shouldSetAndGetUpdatedAt() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        LocalDateTime updatedAt = LocalDateTime.now();
        
        // When
        response.setUpdatedAt(updatedAt);
        
        // Then
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // Given & When
        ConsultationResponse response = new ConsultationResponse(
            null, null, null, null, null, null, null, null, null, null, null, null, null
        );
        
        // Then
        assertNull(response.getId());
        assertNull(response.getAnimal());
        assertNull(response.getConsultationDate());
        assertNull(response.getVeterinarianName());
        assertNull(response.getReason());
        assertNull(response.getDescription());
        assertNull(response.getDiagnosis());
        assertNull(response.getTreatmentPrescribed());
        assertNull(response.getObservations());
        assertNull(response.getNextAppointmentDate());
        assertNull(response.getStatus());
        assertNull(response.getCreatedAt());
        assertNull(response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create with minimal required fields")
    void shouldCreateWithMinimalRequiredFields() {
        // Given
        ConsultationResponse response = new ConsultationResponse();
        Long id = 5L;
        String veterinarianName = "Dr. Brown";
        String status = "PENDING";
        
        // When
        response.setId(id);
        response.setVeterinarianName(veterinarianName);
        response.setStatus(status);
        
        // Then
        assertEquals(id, response.getId());
        assertEquals(veterinarianName, response.getVeterinarianName());
        assertEquals(status, response.getStatus());
        assertNull(response.getAnimal());
        assertNull(response.getConsultationDate());
    }
}

