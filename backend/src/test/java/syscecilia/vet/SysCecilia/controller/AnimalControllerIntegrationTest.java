package syscecilia.vet.SysCecilia.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import syscecilia.vet.SysCecilia.dto.AnimalResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
@DisplayName("AnimalController Integration Tests")
class AnimalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    private AnimalResponse animalResponse1;
    private AnimalResponse animalResponse2;

    @BeforeEach
    void setUp() {
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
}

