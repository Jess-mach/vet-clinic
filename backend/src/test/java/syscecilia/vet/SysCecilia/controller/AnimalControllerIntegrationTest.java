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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import syscecilia.vet.SysCecilia.dto.AnimalRequest;
import syscecilia.vet.SysCecilia.dto.AnimalResponse;
import syscecilia.vet.SysCecilia.config.TestConfig;
import syscecilia.vet.SysCecilia.exception.BusinessException;
import syscecilia.vet.SysCecilia.exception.GlobalExceptionHandler;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.service.AnimalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
@Import({GlobalExceptionHandler.class, TestConfig.class})
@TestPropertySource(properties = "spring.mvc.problem-details.enabled=false")
@DisplayName("AnimalController Integration Tests")
class AnimalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    private ObjectMapper objectMapper;
    private AnimalResponse animalResponse1;
    private AnimalResponse animalResponse2;

    @BeforeEach
    void setUp() {
        // Configure ObjectMapper with Java 8 time support
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        animalResponse1 = new AnimalResponse();
        animalResponse1.setId(1L);
        animalResponse1.setName("Rex");
        animalResponse1.setSpecies("Dog");
        animalResponse1.setBreed("Golden Retriever");
        animalResponse1.setGender("Male");
        animalResponse1.setBirthDate(LocalDate.of(2020, 5, 15));
        animalResponse1.setColor("Golden");
        animalResponse1.setWeight(new BigDecimal("25.5"));
        animalResponse1.setMicrochipNumber("CHIP001");
        animalResponse1.setOwnerName("John Doe");
        animalResponse1.setOwnerPhone("1234567890");
        animalResponse1.setOwnerEmail("john@example.com");
        animalResponse1.setCreatedAt(LocalDateTime.now());
        animalResponse1.setUpdatedAt(LocalDateTime.now());

        animalResponse2 = new AnimalResponse();
        animalResponse2.setId(2L);
        animalResponse2.setName("Fluffy");
        animalResponse2.setSpecies("Cat");
        animalResponse2.setBreed("Persian");
        animalResponse2.setGender("Female");
        animalResponse2.setBirthDate(LocalDate.of(2021, 3, 20));
        animalResponse2.setColor("White");
        animalResponse2.setWeight(new BigDecimal("4.2"));
        animalResponse2.setMicrochipNumber("CHIP002");
        animalResponse2.setOwnerName("Jane Smith");
        animalResponse2.setOwnerPhone("0987654321");
        animalResponse2.setOwnerEmail("jane@example.com");
        animalResponse2.setCreatedAt(LocalDateTime.now());
        animalResponse2.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("GET /api/animals - Should return all animals successfully when no filters")
    void shouldReturnAllAnimalsSuccessfullyWhenNoFilters() throws Exception {
        // Given
        List<AnimalResponse> animals = Arrays.asList(animalResponse1, animalResponse2);
        when(animalService.search(null, null, null)).thenReturn(animals);

        // When/Then
        mockMvc.perform(get("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Fluffy"));

        verify(animalService, times(1)).search(null, null, null);
    }

    @Test
    @DisplayName("GET /api/animals - Should return empty list when no animals exist")
    void shouldReturnEmptyListWhenNoAnimalsExist() throws Exception {
        // Given
        when(animalService.search(null, null, null)).thenReturn(Collections.emptyList());

        // When/Then
        mockMvc.perform(get("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(animalService, times(1)).search(null, null, null);
    }

    @Test
    @DisplayName("GET /api/animals/{id} - Should return animal by ID successfully")
    void shouldReturnAnimalByIdSuccessfully() throws Exception {
        // Given
        when(animalService.findById(1L)).thenReturn(animalResponse1);

        // When/Then
        mockMvc.perform(get("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.species").value("Dog"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.ownerName").value("John Doe"))
                .andExpect(jsonPath("$.microchipNumber").value("CHIP001"));

        verify(animalService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/animals/{id} - Should return 404 when animal not found")
    void shouldReturn404WhenAnimalNotFound() throws Exception {
        // Given
        when(animalService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Animal not found with id: 999"));

        // When/Then
        mockMvc.perform(get("/api/animals/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Animal not found with id: 999"));

        verify(animalService, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET /api/animals/{id} - Should return 400 when ID is invalid")
    void shouldReturn400WhenIdIsInvalid() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/animals/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(animalService, never()).findById(any());
    }

    @Test
    @DisplayName("GET /api/animals?species=Dog - Should return animals by species filter")
    void shouldReturnAnimalsBySpeciesFilter() throws Exception {
        // Given
        List<AnimalResponse> dogs = Collections.singletonList(animalResponse1);
        when(animalService.search(null, "Dog", null)).thenReturn(dogs);

        // When/Then
        mockMvc.perform(get("/api/animals")
                        .param("species", "Dog")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].species").value("Dog"));

        verify(animalService, times(1)).search(null, "Dog", null);
    }

    @Test
    @DisplayName("GET /api/animals?ownerName=John - Should return animals by owner name filter")
    void shouldReturnAnimalsByOwnerNameFilter() throws Exception {
        // Given
        List<AnimalResponse> animals = Collections.singletonList(animalResponse1);
        when(animalService.search(null, null, "John")).thenReturn(animals);

        // When/Then
        mockMvc.perform(get("/api/animals")
                        .param("ownerName", "John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ownerName").value("John Doe"));

        verify(animalService, times(1)).search(null, null, "John");
    }

    @Test
    @DisplayName("GET /api/animals?name=Rex - Should return animals by name filter")
    void shouldReturnAnimalsByNameFilter() throws Exception {
        // Given
        List<AnimalResponse> animals = Collections.singletonList(animalResponse1);
        when(animalService.search("Rex", null, null)).thenReturn(animals);

        // When/Then
        mockMvc.perform(get("/api/animals")
                        .param("name", "Rex")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Rex"));

        verify(animalService, times(1)).search("Rex", null, null);
    }

    @Test
    @DisplayName("GET /api/animals?name=Rex&species=Dog - Should return animals with multiple filters")
    void shouldReturnAnimalsWithMultipleFilters() throws Exception {
        // Given
        List<AnimalResponse> animals = Collections.singletonList(animalResponse1);
        when(animalService.search("Rex", "Dog", null)).thenReturn(animals);

        // When/Then
        mockMvc.perform(get("/api/animals")
                        .param("name", "Rex")
                        .param("species", "Dog")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[0].species").value("Dog"));

        verify(animalService, times(1)).search("Rex", "Dog", null);
    }

    @Test
    @DisplayName("GET /api/animals?name=Rex&species=Dog&ownerName=John - Should return animals with all filters")
    void shouldReturnAnimalsWithAllFilters() throws Exception {
        // Given
        List<AnimalResponse> animals = Collections.singletonList(animalResponse1);
        when(animalService.search("Rex", "Dog", "John")).thenReturn(animals);

        // When/Then
        mockMvc.perform(get("/api/animals")
                        .param("name", "Rex")
                        .param("species", "Dog")
                        .param("ownerName", "John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[0].species").value("Dog"))
                .andExpect(jsonPath("$[0].ownerName").value("John Doe"));

        verify(animalService, times(1)).search("Rex", "Dog", "John");
    }

    @Test
    @DisplayName("POST /api/animals - Should create animal successfully")
    void shouldCreateAnimalSuccessfully() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setBreed("Labrador");
        request.setGender("Male");
        request.setBirthDate(LocalDate.of(2021, 1, 15));
        request.setColor("Black");
        request.setWeight(new BigDecimal("30.5"));
        request.setMicrochipNumber("CHIP003");
        request.setOwnerName("Alice Johnson");
        request.setOwnerPhone("9876543210");
        request.setOwnerEmail("alice@example.com");

        AnimalResponse createdResponse = new AnimalResponse();
        createdResponse.setId(3L);
        createdResponse.setName("Max");
        createdResponse.setSpecies("Dog");
        createdResponse.setBreed("Labrador");
        createdResponse.setGender("Male");
        createdResponse.setBirthDate(LocalDate.of(2021, 1, 15));
        createdResponse.setColor("Black");
        createdResponse.setWeight(new BigDecimal("30.5"));
        createdResponse.setMicrochipNumber("CHIP003");
        createdResponse.setOwnerName("Alice Johnson");
        createdResponse.setOwnerPhone("9876543210");
        createdResponse.setOwnerEmail("alice@example.com");
        createdResponse.setCreatedAt(LocalDateTime.now());
        createdResponse.setUpdatedAt(LocalDateTime.now());

        when(animalService.create(any(AnimalRequest.class))).thenReturn(createdResponse);

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "/api/animals/3"))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.species").value("Dog"))
                .andExpect(jsonPath("$.breed").value("Labrador"))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.ownerName").value("Alice Johnson"))
                .andExpect(jsonPath("$.microchipNumber").value("CHIP003"));

        verify(animalService, times(1)).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should return 400 when required fields are missing")
    void shouldReturn400WhenRequiredFieldsAreMissing() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        // Missing required fields: species, gender, ownerName

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.species").exists())
                .andExpect(jsonPath("$.errors.gender").exists())
                .andExpect(jsonPath("$.errors.ownerName").exists());

        verify(animalService, never()).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should return 400 when name is blank")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("   ");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        verify(animalService, never()).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should return 400 when email format is invalid")
    void shouldReturn400WhenEmailFormatIsInvalid() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");
        request.setOwnerEmail("invalid-email");

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.ownerEmail").exists());

        verify(animalService, never()).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should return 422 when microchip number already exists")
    void shouldReturn422WhenMicrochipNumberAlreadyExists() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setMicrochipNumber("CHIP001");
        request.setOwnerName("John Doe");

        when(animalService.create(any(AnimalRequest.class)))
                .thenThrow(new BusinessException("Microchip number already exists: CHIP001"));

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Business Rule Violation"))
                .andExpect(jsonPath("$.detail").value("Microchip number already exists: CHIP001"));

        verify(animalService, times(1)).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should create animal successfully without optional fields")
    void shouldCreateAnimalSuccessfullyWithoutOptionalFields() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Bella");
        request.setSpecies("Cat");
        request.setGender("Female");
        request.setOwnerName("Bob Smith");

        AnimalResponse createdResponse = new AnimalResponse();
        createdResponse.setId(4L);
        createdResponse.setName("Bella");
        createdResponse.setSpecies("Cat");
        createdResponse.setGender("Female");
        createdResponse.setOwnerName("Bob Smith");
        createdResponse.setCreatedAt(LocalDateTime.now());
        createdResponse.setUpdatedAt(LocalDateTime.now());

        when(animalService.create(any(AnimalRequest.class))).thenReturn(createdResponse);

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.name").value("Bella"))
                .andExpect(jsonPath("$.species").value("Cat"))
                .andExpect(jsonPath("$.breed").doesNotExist())
                .andExpect(jsonPath("$.microchipNumber").doesNotExist());

        verify(animalService, times(1)).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should return 400 when weight is negative")
    void shouldReturn400WhenWeightIsNegative() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");
        request.setWeight(new BigDecimal("-10.5"));

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.weight").exists());

        verify(animalService, never()).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should return 400 when birth date is in the future")
    void shouldReturn400WhenBirthDateIsInTheFuture() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");
        request.setBirthDate(LocalDate.now().plusDays(1));

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.birthDate").exists());

        verify(animalService, never()).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should update animal successfully")
    void shouldUpdateAnimalSuccessfully() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Rex Updated");
        request.setSpecies("Dog");
        request.setBreed("German Shepherd");
        request.setGender("Male");
        request.setBirthDate(LocalDate.of(2020, 5, 15));
        request.setColor("Brown");
        request.setWeight(new BigDecimal("28.5"));
        request.setMicrochipNumber("CHIP001");
        request.setOwnerName("John Doe");
        request.setOwnerPhone("1234567890");
        request.setOwnerEmail("john@example.com");

        AnimalResponse updatedResponse = new AnimalResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Rex Updated");
        updatedResponse.setSpecies("Dog");
        updatedResponse.setBreed("German Shepherd");
        updatedResponse.setGender("Male");
        updatedResponse.setBirthDate(LocalDate.of(2020, 5, 15));
        updatedResponse.setColor("Brown");
        updatedResponse.setWeight(new BigDecimal("28.5"));
        updatedResponse.setMicrochipNumber("CHIP001");
        updatedResponse.setOwnerName("John Doe");
        updatedResponse.setOwnerPhone("1234567890");
        updatedResponse.setOwnerEmail("john@example.com");
        updatedResponse.setCreatedAt(LocalDateTime.now());
        updatedResponse.setUpdatedAt(LocalDateTime.now());

        when(animalService.update(eq(1L), any(AnimalRequest.class))).thenReturn(updatedResponse);

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rex Updated"))
                .andExpect(jsonPath("$.species").value("Dog"))
                .andExpect(jsonPath("$.breed").value("German Shepherd"))
                .andExpect(jsonPath("$.color").value("Brown"))
                .andExpect(jsonPath("$.weight").value(28.5));

        verify(animalService, times(1)).update(eq(1L), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should return 404 when animal not found")
    void shouldReturn404WhenUpdatingNonExistentAnimal() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");

        when(animalService.update(eq(999L), any(AnimalRequest.class)))
                .thenThrow(new ResourceNotFoundException("Animal not found with id: 999"));

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Animal not found with id: 999"));

        verify(animalService, times(1)).update(eq(999L), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should return 400 when required fields are missing")
    void shouldReturn400WhenUpdatingWithMissingRequiredFields() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        // Missing required fields: species, gender, ownerName

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.species").exists())
                .andExpect(jsonPath("$.errors.gender").exists())
                .andExpect(jsonPath("$.errors.ownerName").exists());

        verify(animalService, never()).update(anyLong(), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should return 422 when microchip number already exists")
    void shouldReturn422WhenUpdatingWithDuplicateMicrochip() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setMicrochipNumber("CHIP002");
        request.setOwnerName("John Doe");

        when(animalService.update(eq(1L), any(AnimalRequest.class)))
                .thenThrow(new BusinessException("Microchip number already exists: CHIP002"));

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Business Rule Violation"))
                .andExpect(jsonPath("$.detail").value("Microchip number already exists: CHIP002"));

        verify(animalService, times(1)).update(eq(1L), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should return 400 when ID is invalid")
    void shouldReturn400WhenUpdatingWithInvalidId() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(animalService, never()).update(anyLong(), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should update animal with valid email format")
    void shouldUpdateAnimalWithValidEmailFormat() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Rex");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");
        request.setOwnerEmail("john.doe@example.com");

        AnimalResponse updatedResponse = new AnimalResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Rex");
        updatedResponse.setSpecies("Dog");
        updatedResponse.setGender("Male");
        updatedResponse.setOwnerName("John Doe");
        updatedResponse.setOwnerEmail("john.doe@example.com");
        updatedResponse.setCreatedAt(LocalDateTime.now());
        updatedResponse.setUpdatedAt(LocalDateTime.now());

        when(animalService.update(eq(1L), any(AnimalRequest.class))).thenReturn(updatedResponse);

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerEmail").value("john.doe@example.com"));

        verify(animalService, times(1)).update(eq(1L), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should return 400 when email format is invalid")
    void shouldReturn400WhenUpdatingWithInvalidEmailFormat() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");
        request.setOwnerEmail("invalid-email");

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.ownerEmail").exists());

        verify(animalService, never()).update(anyLong(), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should update animal when keeping same microchip")
    void shouldUpdateAnimalWhenKeepingSameMicrochip() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Rex Updated");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setMicrochipNumber("CHIP001");
        request.setOwnerName("John Doe");

        AnimalResponse updatedResponse = new AnimalResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Rex Updated");
        updatedResponse.setSpecies("Dog");
        updatedResponse.setGender("Male");
        updatedResponse.setMicrochipNumber("CHIP001");
        updatedResponse.setOwnerName("John Doe");
        updatedResponse.setCreatedAt(LocalDateTime.now());
        updatedResponse.setUpdatedAt(LocalDateTime.now());

        when(animalService.update(eq(1L), any(AnimalRequest.class))).thenReturn(updatedResponse);

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rex Updated"))
                .andExpect(jsonPath("$.microchipNumber").value("CHIP001"));

        verify(animalService, times(1)).update(eq(1L), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should return 400 when weight is negative")
    void shouldReturn400WhenUpdatingWithNegativeWeight() throws Exception {
        // Given
        AnimalRequest request = new AnimalRequest();
        request.setName("Max");
        request.setSpecies("Dog");
        request.setGender("Male");
        request.setOwnerName("John Doe");
        request.setWeight(new BigDecimal("-10.5"));

        String requestJson = objectMapper.writeValueAsString(request);

        // When/Then
        mockMvc.perform(put("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.weight").exists());

        verify(animalService, never()).update(anyLong(), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("PUT /api/animals/{id} - Should return 400 with detailed error when birthDate format is invalid")
    void shouldReturn400WithDetailedErrorWhenInvalidBirthDateFormat() throws Exception {
        // Given - Sending raw JSON with invalid date format
        String invalidRequestJson = "{\"name\":\"Maria Cecilia\",\"species\":\"Dog\",\"gender\":\"Neutered\"," +
                "\"ownerName\":\"Wagner Costa\",\"breed\":\"Chettos\",\"color\":\"Sujeira\"," +
                "\"microchipNumber\":\"0001\",\"ownerPhone\":\"11934002606\",\"ownerEmail\":\"wg.o.costa@gmail.com\"," +
                "\"birthDate\":\"26-05-2020\",\"weight\":25.5}";

        // When/Then
        mockMvc.perform(put("/api/animals/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.birthDate").exists());

        verify(animalService, never()).update(anyLong(), any(AnimalRequest.class));
    }

    @Test
    @DisplayName("POST /api/animals - Should return 400 with detailed error when birthDate format is invalid")
    void shouldReturn400WithDetailedErrorOnCreateWhenInvalidBirthDateFormat() throws Exception {
        // Given - Sending raw JSON with invalid date format (DD-MM-YYYY instead of YYYY-MM-DD)
        String invalidRequestJson = "{\"name\":\"Maria Cecilia\",\"species\":\"Dog\",\"gender\":\"Neutered\"," +
                "\"ownerName\":\"Wagner Costa\",\"breed\":\"Chettos\",\"color\":\"Sujeira\"," +
                "\"microchipNumber\":\"0001\",\"ownerPhone\":\"11934002606\",\"ownerEmail\":\"wg.o.costa@gmail.com\"," +
                "\"birthDate\":\"26-05-2020\",\"weight\":25.5}";

        // When/Then
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.birthDate").exists());

        verify(animalService, never()).create(any(AnimalRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/animals/{id} - Should delete animal successfully")
    void shouldDeleteAnimalSuccessfully() throws Exception {
        // Given
        doNothing().when(animalService).delete(1L);

        // When/Then
        mockMvc.perform(delete("/api/animals/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(animalService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/animals/{id} - Should return 404 when animal not found")
    void shouldReturn404WhenDeletingNonExistentAnimal() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Animal not found with id: 999"))
                .when(animalService).delete(999L);

        // When/Then
        mockMvc.perform(delete("/api/animals/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Animal not found with id: 999"));

        verify(animalService, times(1)).delete(999L);
    }

    @Test
    @DisplayName("DELETE /api/animals/{id} - Should return 400 when ID is invalid")
    void shouldReturn400WhenDeletingWithInvalidId() throws Exception {
        // When/Then
        mockMvc.perform(delete("/api/animals/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(animalService, never()).delete(any());
    }

    @Test
    @DisplayName("DELETE /api/animals/{id} - Should return 400 when ID is negative")
    void shouldReturn400WhenDeletingWithNegativeId() throws Exception {
        // When/Then
        mockMvc.perform(delete("/api/animals/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(animalService, never()).delete(any());
    }
}

