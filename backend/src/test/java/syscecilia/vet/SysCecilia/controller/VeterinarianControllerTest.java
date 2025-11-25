package syscecilia.vet.SysCecilia.controller;

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
import syscecilia.vet.SysCecilia.dto.VeterinarianAvailabilityResponse;
import syscecilia.vet.SysCecilia.dto.VeterinarianResponse;
import syscecilia.vet.SysCecilia.exception.GlobalExceptionHandler;
import syscecilia.vet.SysCecilia.service.VeterinarianService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeterinarianController.class)
@Import({GlobalExceptionHandler.class, TestConfig.class})
@TestPropertySource(properties = "spring.mvc.problem-details.enabled=false")
@DisplayName("VeterinarianController Unit Tests")
class VeterinarianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeterinarianService veterinarianService;
    private VeterinarianResponse veterinarian1;
    private VeterinarianResponse veterinarian2;
    private VeterinarianResponse veterinarian3;
    private VeterinarianAvailabilityResponse morningAvailability;
    private VeterinarianAvailabilityResponse afternoonAvailability;

    @BeforeEach
    void setUp() {
        veterinarian1 = new VeterinarianResponse();
        veterinarian1.setId(1L);
        veterinarian1.setName("Dr. Amelia Rivers");
        veterinarian1.setSpecialtyCode(2);
        veterinarian1.setSpecialty("Consulta com oftalmologista");
        veterinarian1.setCreatedAt(LocalDateTime.now());
        veterinarian1.setUpdatedAt(LocalDateTime.now());

        veterinarian2 = new VeterinarianResponse();
        veterinarian2.setId(2L);
        veterinarian2.setName("Dr. Noah Bennett");
        veterinarian2.setSpecialtyCode(1);
        veterinarian2.setSpecialty("Consulta com clinico geral");
        veterinarian2.setCreatedAt(LocalDateTime.now());
        veterinarian2.setUpdatedAt(LocalDateTime.now());

        veterinarian3 = new VeterinarianResponse();
        veterinarian3.setId(3L);
        veterinarian3.setName("Dr. Olivia Carter");
        veterinarian3.setSpecialtyCode(3);
        veterinarian3.setSpecialty("Consulta com cardiologista");
        veterinarian3.setCreatedAt(LocalDateTime.now());
        veterinarian3.setUpdatedAt(LocalDateTime.now());

        morningAvailability = new VeterinarianAvailabilityResponse(
                LocalDate.of(2025, 11, 25),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                "America/Sao_Paulo"
        );

        afternoonAvailability = new VeterinarianAvailabilityResponse(
                LocalDate.of(2025, 11, 25),
                LocalTime.of(13, 0),
                LocalTime.of(18, 0),
                "America/Sao_Paulo"
        );
    }

    @Test
    @DisplayName("GET /api/veterinarians - Should return all veterinarians when no filters provided")
    void testFindAll_NoFilters_ReturnsAllVeterinarians() throws Exception {
        // Arrange
        List<VeterinarianResponse> veterinarians = Arrays.asList(veterinarian1, veterinarian2, veterinarian3);
        when(veterinarianService.findAll(null, null)).thenReturn(veterinarians);

        // Act & Assert
        mockMvc.perform(get("/api/veterinarians")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Amelia Rivers")))
                .andExpect(jsonPath("$[0].specialtyCode", is(2)))
                .andExpect(jsonPath("$[0].specialty", is("Consulta com oftalmologista")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Dr. Noah Bennett")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Dr. Olivia Carter")));

        verify(veterinarianService, times(1)).findAll(null, null);
    }

    @Test
    @DisplayName("GET /api/veterinarians?name=Amelia - Should return filtered veterinarians by name")
    void testFindAll_WithNameFilter_ReturnsFilteredVeterinarians() throws Exception {
        // Arrange
        List<VeterinarianResponse> filteredVets = Collections.singletonList(veterinarian1);
        when(veterinarianService.findAll(eq("Amelia"), eq(null))).thenReturn(filteredVets);

        // Act & Assert
        mockMvc.perform(get("/api/veterinarians")
                        .param("name", "Amelia")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Amelia Rivers")))
                .andExpect(jsonPath("$[0].specialtyCode", is(2)));

        verify(veterinarianService, times(1)).findAll("Amelia", null);
    }

    @Test
    @DisplayName("GET /api/veterinarians?specialtyCode=1 - Should return filtered veterinarians by specialty")
    void testFindAll_WithSpecialtyFilter_ReturnsFilteredVeterinarians() throws Exception {
        // Arrange
        List<VeterinarianResponse> filteredVets = Collections.singletonList(veterinarian2);
        when(veterinarianService.findAll(eq(null), eq(1))).thenReturn(filteredVets);

        // Act & Assert
        mockMvc.perform(get("/api/veterinarians")
                        .param("specialtyCode", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Noah Bennett")))
                .andExpect(jsonPath("$[0].specialtyCode", is(1)))
                .andExpect(jsonPath("$[0].specialty", is("Consulta com clinico geral")));

        verify(veterinarianService, times(1)).findAll(null, 1);
    }

    @Test
    @DisplayName("GET /api/veterinarians?name=Olivia&specialtyCode=3 - Should return filtered veterinarians by both filters")
    void testFindAll_WithBothFilters_ReturnsFilteredVeterinarians() throws Exception {
        // Arrange
        List<VeterinarianResponse> filteredVets = Collections.singletonList(veterinarian3);
        when(veterinarianService.findAll(eq("Olivia"), eq(3))).thenReturn(filteredVets);

        // Act & Assert
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

        verify(veterinarianService, times(1)).findAll("Olivia", 3);
    }

    @Test
    @DisplayName("GET /api/veterinarians - Should return empty list when no veterinarians found")
    void testFindAll_NoMatches_ReturnsEmptyList() throws Exception {
        // Arrange
        when(veterinarianService.findAll(any(), any())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/veterinarians")
                        .param("name", "NonExistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(veterinarianService, times(1)).findAll("NonExistent", null);
    }

    @Test
    @DisplayName("GET /api/veterinarians - Should handle service errors gracefully")
    void testFindAll_ServiceError_ReturnsInternalServerError() throws Exception {
        // Arrange
        when(veterinarianService.findAll(any(), any()))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        mockMvc.perform(get("/api/veterinarians")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(veterinarianService, times(1)).findAll(null, null);
    }

    @Test
    @DisplayName("GET /api/veterinarians/{id}/availability - Should return availability intervals")
    void testGetAvailability_ReturnsIntervals() throws Exception {
        List<VeterinarianAvailabilityResponse> intervals = Arrays.asList(morningAvailability, afternoonAvailability);
        when(veterinarianService.findAvailability(1L, null)).thenReturn(intervals);

        mockMvc.perform(get("/api/veterinarians/{id}/availability", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("2025-11-25")))
                .andExpect(jsonPath("$[0].startTime", is("08:00:00")))
                .andExpect(jsonPath("$[0].endTime", is("10:00:00")))
                .andExpect(jsonPath("$[0].timezone", is("America/Sao_Paulo")))
                .andExpect(jsonPath("$[1].startTime", is("13:00:00")));

        verify(veterinarianService, times(1)).findAvailability(1L, null);
    }

    @Test
    @DisplayName("GET /api/veterinarians/{id}/availability - Should validate veterinarian id")
    void testGetAvailability_InvalidId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/veterinarians/{id}/availability", -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(veterinarianService, never()).findAvailability(anyLong(), any());
    }
}

