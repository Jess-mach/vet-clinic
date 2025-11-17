package syscecilia.vet.SysCecilia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import static org.mockito.ArgumentMatchers.*;
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
    @DisplayName("Should find consultation by ID")
    public void testFindConsultationById() throws Exception {
        when(consultationService.findById(1L)).thenReturn(testConsultation);

        mockMvc.perform(get("/api/consultations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.veterinarianName", is("Dr. Silva")))
                .andExpect(jsonPath("$.reason", is("Routine checkup")))
                .andExpect(jsonPath("$.animal.name", is("Rex")))
                .andExpect(jsonPath("$.animal.species", is("Dog")));

        verify(consultationService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return 404 when consultation not found by ID")
    public void testFindConsultationByIdNotFound() throws Exception {
        when(consultationService.findById(9999L))
                .thenThrow(new ResourceNotFoundException("Consultation not found with id: 9999"));

        mockMvc.perform(get("/api/consultations/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(consultationService, times(1)).findById(9999L);
    }

    @Test
    @DisplayName("Should find all consultations with pagination (no filters)")
    public void testFindAllConsultationsWithPagination() throws Exception {
        Page<ConsultationResponse> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "consultationDate")),
                1
        );

        when(consultationService.findByFilters(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/consultations")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].veterinarianName", is("Dr. Silva")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)));

        verify(consultationService, times(1)).findByFilters(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        );
    }

    @Test
    @DisplayName("Should find consultations filtered by animal name")
    public void testFindConsultationsByAnimalName() throws Exception {
        Page<ConsultationResponse> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10),
                1
        );

        when(consultationService.findByFilters(
                eq("Rex"), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/consultations")
                .param("animalName", "Rex")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].animal.name", is("Rex")));

        verify(consultationService, times(1)).findByFilters(
                eq("Rex"), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        );
    }

    @Test
    @DisplayName("Should find consultations filtered by veterinarian name")
    public void testFindConsultationsByVeterinarianName() throws Exception {
        Page<ConsultationResponse> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10),
                1
        );

        when(consultationService.findByFilters(
                isNull(), isNull(), eq("Dr. Silva"), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/consultations")
                .param("veterinarianName", "Dr. Silva")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].veterinarianName", is("Dr. Silva")));

        verify(consultationService, times(1)).findByFilters(
                isNull(), isNull(), eq("Dr. Silva"), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        );
    }

    @Test
    @DisplayName("Should find consultations filtered by status")
    public void testFindConsultationsByStatus() throws Exception {
        Page<ConsultationResponse> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10),
                1
        );

        when(consultationService.findByFilters(
                isNull(), isNull(), isNull(), eq("COMPLETED"), isNull(), isNull(),
                isNull(), isNull(), any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/consultations")
                .param("status", "COMPLETED")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].status", is("COMPLETED")));

        verify(consultationService, times(1)).findByFilters(
                isNull(), isNull(), isNull(), eq("COMPLETED"), isNull(), isNull(),
                isNull(), isNull(), any()
        );
    }

    @Test
    @DisplayName("Should find consultations with multiple filters applied together")
    public void testFindConsultationsWithMultipleFilters() throws Exception {
        Page<ConsultationResponse> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10),
                1
        );

        when(consultationService.findByFilters(
                eq("Rex"), eq("John Doe"), eq("Dr. Silva"), eq("COMPLETED"), 
                isNull(), isNull(), isNull(), isNull(), any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/consultations")
                .param("animalName", "Rex")
                .param("ownerName", "John Doe")
                .param("veterinarianName", "Dr. Silva")
                .param("status", "COMPLETED")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].animal.name", is("Rex")))
                .andExpect(jsonPath("$.content[0].animal.ownerName", is("John Doe")))
                .andExpect(jsonPath("$.content[0].veterinarianName", is("Dr. Silva")))
                .andExpect(jsonPath("$.content[0].status", is("COMPLETED")));

        verify(consultationService, times(1)).findByFilters(
                eq("Rex"), eq("John Doe"), eq("Dr. Silva"), eq("COMPLETED"),
                isNull(), isNull(), isNull(), isNull(), any()
        );
    }

    @Test
    @DisplayName("Should return empty page when no consultations match filters")
    public void testFindConsultationsEmptyResult() throws Exception {
        Page<ConsultationResponse> emptyPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 10),
                0
        );

        when(consultationService.findByFilters(
                eq("NonExistent"), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        )).thenReturn(emptyPage);

        mockMvc.perform(get("/api/consultations")
                .param("animalName", "NonExistent")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));

        verify(consultationService, times(1)).findByFilters(
                eq("NonExistent"), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), any()
        );
    }
}

