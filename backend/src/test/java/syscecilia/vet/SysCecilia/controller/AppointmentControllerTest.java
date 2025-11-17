package syscecilia.vet.SysCecilia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import syscecilia.vet.SysCecilia.dto.AppointmentRequest;
import syscecilia.vet.SysCecilia.dto.AppointmentResponse;
import syscecilia.vet.SysCecilia.exception.GlobalExceptionHandler;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.service.AppointmentService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@Import({GlobalExceptionHandler.class, TestConfig.class})
@TestPropertySource(properties = "spring.mvc.problem-details.enabled=false")
@DisplayName("AppointmentController Tests")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    private ObjectMapper objectMapper;
    private AppointmentRequest appointmentRequest;
    private AppointmentResponse appointmentResponse;

    @BeforeEach
    void setUp() {
        // Configure ObjectMapper with Java 8 time support
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAnimalId(1L);
        appointmentRequest.setAppointmentDate(LocalDateTime.now().plusDays(7));
        appointmentRequest.setVeterinarianName("Dr. Silva");
        appointmentRequest.setReason("Routine checkup");
        appointmentRequest.setNotes("First visit for this animal");

        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo(1L, "Rex", "Dog", "Golden Retriever", "John Doe");
        appointmentResponse = new AppointmentResponse(
                1L,
                animalBasicInfo,
                appointmentRequest.getAppointmentDate(),
                appointmentRequest.getVeterinarianName(),
                appointmentRequest.getReason(),
                appointmentRequest.getNotes(),
                "SCHEDULED",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /api/appointments - Should create appointment successfully")
    void shouldCreateAppointmentSuccessfully() throws Exception {
        // Given
        when(appointmentService.create(any(AppointmentRequest.class))).thenReturn(appointmentResponse);

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "/api/appointments/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.veterinarianName").value("Dr. Silva"))
                .andExpect(jsonPath("$.reason").value("Routine checkup"))
                .andExpect(jsonPath("$.notes").value("First visit for this animal"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.animal.name").value("Rex"))
                .andExpect(jsonPath("$.animal.species").value("Dog"));

        verify(appointmentService, times(1)).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 400 when required fields are missing")
    void shouldReturn400WhenRequiredFieldsAreMissing() throws Exception {
        // Given
        AppointmentRequest invalidRequest = new AppointmentRequest();
        // Missing required fields: animalId, appointmentDate, veterinarianName, reason

        String requestJson = objectMapper.writeValueAsString(invalidRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.animalId").exists())
                .andExpect(jsonPath("$.errors.appointmentDate").exists())
                .andExpect(jsonPath("$.errors.veterinarianName").exists())
                .andExpect(jsonPath("$.errors.reason").exists());

        verify(appointmentService, never()).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 400 when animal ID is invalid")
    void shouldReturn400WhenAnimalIdIsInvalid() throws Exception {
        // Given
        appointmentRequest.setAnimalId(0L);

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.animalId").exists());

        verify(appointmentService, never()).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 400 when appointment date is in the past")
    void shouldReturn400WhenAppointmentDateIsInThePast() throws Exception {
        // Given
        appointmentRequest.setAppointmentDate(LocalDateTime.now().minusDays(1));

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.appointmentDate").exists());

        verify(appointmentService, never()).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 400 when veterinarian name is blank")
    void shouldReturn400WhenVeterinarianNameIsBlank() throws Exception {
        // Given
        appointmentRequest.setVeterinarianName("   ");

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.veterinarianName").exists());

        verify(appointmentService, never()).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 400 when reason is blank")
    void shouldReturn400WhenReasonIsBlank() throws Exception {
        // Given
        appointmentRequest.setReason("   ");

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.reason").exists());

        verify(appointmentService, never()).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 400 when veterinarian name exceeds max length")
    void shouldReturn400WhenVeterinarianNameExceedsMaxLength() throws Exception {
        // Given
        appointmentRequest.setVeterinarianName("A".repeat(101));

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.veterinarianName").exists());

        verify(appointmentService, never()).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 400 when reason exceeds max length")
    void shouldReturn400WhenReasonExceedsMaxLength() throws Exception {
        // Given
        appointmentRequest.setReason("A".repeat(256));

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.reason").exists());

        verify(appointmentService, never()).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should create appointment successfully without notes")
    void shouldCreateAppointmentSuccessfullyWithoutNotes() throws Exception {
        // Given
        appointmentRequest.setNotes(null);
        appointmentResponse.setNotes(null);

        when(appointmentService.create(any(AppointmentRequest.class))).thenReturn(appointmentResponse);

        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.notes").doesNotExist());

        verify(appointmentService, times(1)).create(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/appointments - Should return 404 when animal not found")
    void shouldReturn404WhenAnimalNotFound() throws Exception {
        // Given
        when(appointmentService.create(any(AppointmentRequest.class)))
                .thenThrow(new ResourceNotFoundException("Animal not found with id: 999"));

        appointmentRequest.setAnimalId(999L);
        String requestJson = objectMapper.writeValueAsString(appointmentRequest);

        // When/Then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Animal not found with id: 999"));

        verify(appointmentService, times(1)).create(any(AppointmentRequest.class));
    }
}

