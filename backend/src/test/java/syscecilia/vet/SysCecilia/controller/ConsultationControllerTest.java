package syscecilia.vet.SysCecilia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import syscecilia.vet.SysCecilia.config.TestConfig;
import syscecilia.vet.SysCecilia.dto.AnimalBasicInfo;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.exception.GlobalExceptionHandler;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.service.ConsultationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultationController.class)
@Import({GlobalExceptionHandler.class, TestConfig.class})
@TestPropertySource(properties = "spring.mvc.problem-details.enabled=false")
@DisplayName("ConsultationController Tests")
public class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultationService consultationService;

    private ConsultationResponse testConsultation;
    private AnimalBasicInfo testAnimal;

    @BeforeEach
    public void setUp() {
        testAnimal = new AnimalBasicInfo(1L, "Rex", "Dog", "Golden Retriever", "John Doe");
        
        testConsultation = new ConsultationResponse(
                1L,
                testAnimal,
                LocalDateTime.of(2025, 11, 15, 14, 30),
                "Dr. Silva",
                "Routine checkup",
                "General health examination performed",
                "Healthy",
                "Continue with regular diet",
                "Schedule next checkup in 6 months",
                LocalDateTime.of(2026, 5, 15, 14, 30),
                "COMPLETED",
                LocalDateTime.of(2025, 11, 15, 15, 30, 0),
                LocalDateTime.of(2025, 11, 15, 15, 30, 0)
        );
    }

    @Test
    @DisplayName("Should find all consultations")
    public void testFindAllConsultations() throws Exception {
        when(consultationService.findAll()).thenReturn(Arrays.asList(testConsultation));

        mockMvc.perform(get("/api/consultations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].veterinarianName", is("Dr. Silva")))
                .andExpect(jsonPath("$[0].reason", is("Routine checkup")))
                .andExpect(jsonPath("$[0].animal.name", is("Rex")))
                .andExpect(jsonPath("$[0].animal.species", is("Dog")));

        verify(consultationService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find consultations by animal ID")
    public void testFindAllConsultationsByAnimalId() throws Exception {
        when(consultationService.findAllByAnimalId(1L)).thenReturn(Arrays.asList(testConsultation));

        mockMvc.perform(get("/api/consultations")
                .param("animalId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].animal.id", is(1)))
                .andExpect(jsonPath("$[0].veterinarianName", is("Dr. Silva")));

        verify(consultationService, times(1)).findAllByAnimalId(1L);
    }

    @Test
    @DisplayName("Should return 404 when animal not found")
    public void testFindAllConsultationsByAnimalIdNotFound() throws Exception {
        when(consultationService.findAllByAnimalId(9999L))
                .thenThrow(new ResourceNotFoundException("Animal not found with id: 9999"));

        mockMvc.perform(get("/api/consultations")
                .param("animalId", "9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(consultationService, times(1)).findAllByAnimalId(9999L);
    }

    @Test
    @DisplayName("Should return empty list when no consultations exist")
    public void testFindAllConsultationsEmpty() throws Exception {
        when(consultationService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/consultations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(consultationService, times(1)).findAll();
    }
}

