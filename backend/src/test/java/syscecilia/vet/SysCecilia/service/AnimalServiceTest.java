package syscecilia.vet.SysCecilia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import syscecilia.vet.SysCecilia.dto.AnimalResponse;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalService Unit Tests")
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalService animalService;

    private Animal animal1;
    private Animal animal2;

    @BeforeEach
    void setUp() {
        animal1 = new Animal();
        animal1.setId(1L);
        animal1.setName("Rex");
        animal1.setSpecies("Dog");
        animal1.setBreed("Golden Retriever");
        animal1.setGender("Male");
        animal1.setBirthDate(LocalDate.of(2020, 5, 15));
        animal1.setColor("Golden");
        animal1.setWeight(new BigDecimal("25.5"));
        animal1.setMicrochipNumber("CHIP001");
        animal1.setOwnerName("John Doe");
        animal1.setOwnerPhone("1234567890");
        animal1.setOwnerEmail("john@example.com");
        animal1.setCreatedAt(LocalDateTime.now());
        animal1.setUpdatedAt(LocalDateTime.now());

        animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Fluffy");
        animal2.setSpecies("Cat");
        animal2.setBreed("Persian");
        animal2.setGender("Female");
        animal2.setBirthDate(LocalDate.of(2021, 3, 20));
        animal2.setColor("White");
        animal2.setWeight(new BigDecimal("4.2"));
        animal2.setMicrochipNumber("CHIP002");
        animal2.setOwnerName("Jane Smith");
        animal2.setOwnerPhone("0987654321");
        animal2.setOwnerEmail("jane@example.com");
        animal2.setCreatedAt(LocalDateTime.now());
        animal2.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should return all animals ordered by name when no filters provided")
    void shouldReturnAllAnimalsOrderedByNameWhenNoFiltersProvided() {
        // Given
        List<Animal> animals = Arrays.asList(animal1, animal2);
        when(animalRepository.findAllByOrderByNameAsc()).thenReturn(animals);

        // When
        List<AnimalResponse> result = animalService.search(null, null, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Fluffy");
        assertThat(result.get(1).getName()).isEqualTo("Rex");
        verify(animalRepository, times(1)).findAllByOrderByNameAsc();
    }

    @Test
    @DisplayName("Should return empty list when no animals exist")
    void shouldReturnEmptyListWhenNoAnimalsExist() {
        // Given
        when(animalRepository.findAllByOrderByNameAsc()).thenReturn(Collections.emptyList());

        // When
        List<AnimalResponse> result = animalService.search(null, null, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(animalRepository, times(1)).findAllByOrderByNameAsc();
    }

    @Test
    @DisplayName("Should return animal by ID when exists")
    void shouldReturnAnimalByIdWhenExists() {
        // Given
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal1));

        // When
        AnimalResponse result = animalService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Rex");
        assertThat(result.getSpecies()).isEqualTo("Dog");
        assertThat(result.getOwnerName()).isEqualTo("John Doe");
        verify(animalRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when animal not found")
    void shouldThrowResourceNotFoundExceptionWhenAnimalNotFound() {
        // Given
        when(animalRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> animalService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Animal not found with id: 999");
        verify(animalRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should return animals by species filter")
    void shouldReturnAnimalsBySpeciesFilter() {
        // Given
        List<Animal> dogs = Collections.singletonList(animal1);
        when(animalRepository.findBySpecies("Dog")).thenReturn(dogs);

        // When
        List<AnimalResponse> result = animalService.search(null, "Dog", null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSpecies()).isEqualTo("Dog");
        verify(animalRepository, times(1)).findBySpecies("Dog");
    }

    @Test
    @DisplayName("Should return empty list when no animals found by species")
    void shouldReturnEmptyListWhenNoAnimalsFoundBySpecies() {
        // Given
        when(animalRepository.findBySpecies("Bird")).thenReturn(Collections.emptyList());

        // When
        List<AnimalResponse> result = animalService.search(null, "Bird", null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(animalRepository, times(1)).findBySpecies("Bird");
    }

    @Test
    @DisplayName("Should return animals by owner name filter")
    void shouldReturnAnimalsByOwnerNameFilter() {
        // Given
        List<Animal> animals = Collections.singletonList(animal1);
        when(animalRepository.findByOwnerNameContainingIgnoreCase("John")).thenReturn(animals);

        // When
        List<AnimalResponse> result = animalService.search(null, null, "John");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOwnerName()).isEqualTo("John Doe");
        verify(animalRepository, times(1)).findByOwnerNameContainingIgnoreCase("John");
    }

    @Test
    @DisplayName("Should return animals by name filter")
    void shouldReturnAnimalsByNameFilter() {
        // Given
        List<Animal> animals = Collections.singletonList(animal1);
        when(animalRepository.findByNameContainingIgnoreCase("Rex")).thenReturn(animals);

        // When
        List<AnimalResponse> result = animalService.search("Rex", null, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Rex");
        verify(animalRepository, times(1)).findByNameContainingIgnoreCase("Rex");
    }

    @Test
    @DisplayName("Should return animals filtered by name and species")
    void shouldReturnAnimalsFilteredByNameAndSpecies() {
        // Given
        List<Animal> animals = Collections.singletonList(animal1);
        when(animalRepository.findByNameContainingIgnoreCase("Rex")).thenReturn(animals);

        // When
        List<AnimalResponse> result = animalService.search("Rex", "Dog", null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Rex");
        assertThat(result.get(0).getSpecies()).isEqualTo("Dog");
        verify(animalRepository, times(1)).findByNameContainingIgnoreCase("Rex");
    }

    @Test
    @DisplayName("Should return animals filtered by all parameters")
    void shouldReturnAnimalsFilteredByAllParameters() {
        // Given
        List<Animal> animals = Collections.singletonList(animal1);
        when(animalRepository.findByNameContainingIgnoreCase("Rex")).thenReturn(animals);

        // When
        List<AnimalResponse> result = animalService.search("Rex", "Dog", "John");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Rex");
        assertThat(result.get(0).getSpecies()).isEqualTo("Dog");
        assertThat(result.get(0).getOwnerName()).isEqualTo("John Doe");
        verify(animalRepository, times(1)).findByNameContainingIgnoreCase("Rex");
    }

    @Test
    @DisplayName("Should convert entity to response DTO correctly")
    void shouldConvertEntityToResponseDTOCorrectly() {
        // Given
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal1));

        // When
        AnimalResponse result = animalService.findById(1L);

        // Then
        assertThat(result.getId()).isEqualTo(animal1.getId());
        assertThat(result.getName()).isEqualTo(animal1.getName());
        assertThat(result.getSpecies()).isEqualTo(animal1.getSpecies());
        assertThat(result.getBreed()).isEqualTo(animal1.getBreed());
        assertThat(result.getGender()).isEqualTo(animal1.getGender());
        assertThat(result.getBirthDate()).isEqualTo(animal1.getBirthDate());
        assertThat(result.getColor()).isEqualTo(animal1.getColor());
        assertThat(result.getWeight()).isEqualTo(animal1.getWeight());
        assertThat(result.getMicrochipNumber()).isEqualTo(animal1.getMicrochipNumber());
        assertThat(result.getOwnerName()).isEqualTo(animal1.getOwnerName());
        assertThat(result.getOwnerPhone()).isEqualTo(animal1.getOwnerPhone());
        assertThat(result.getOwnerEmail()).isEqualTo(animal1.getOwnerEmail());
        assertThat(result.getCreatedAt()).isEqualTo(animal1.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(animal1.getUpdatedAt());
    }
}

