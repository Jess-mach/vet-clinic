package syscecilia.vet.SysCecilia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import syscecilia.vet.SysCecilia.dto.AnimalResponse;
import syscecilia.vet.SysCecilia.dto.PageResponse;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalService Pagination Tests")
public class AnimalServicePaginationTest {

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalService animalService;

    private Animal animal1;
    private Animal animal2;
    private Animal animal3;

    @BeforeEach
    void setUp() {
        animal1 = new Animal();
        animal1.setId(1L);
        animal1.setName("Bella");
        animal1.setSpecies("Dog");
        animal1.setBreed("Golden Retriever");
        animal1.setGender("Female");
        animal1.setBirthDate(LocalDate.of(2020, 5, 15));
        animal1.setColor("Golden");
        animal1.setWeight(new BigDecimal("25.5"));
        animal1.setMicrochipNumber("CHIP001");
        animal1.setOwnerName("John Doe");
        animal1.setOwnerPhone("1234567890");
        animal1.setOwnerEmail("john@example.com");
        animal1.setIsActive(true);

        animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Rex");
        animal2.setSpecies("Dog");
        animal2.setBreed("Labrador");
        animal2.setGender("Male");
        animal2.setBirthDate(LocalDate.of(2021, 3, 20));
        animal2.setColor("Black");
        animal2.setWeight(new BigDecimal("30.0"));
        animal2.setMicrochipNumber("CHIP002");
        animal2.setOwnerName("Jane Smith");
        animal2.setOwnerPhone("0987654321");
        animal2.setOwnerEmail("jane@example.com");
        animal2.setIsActive(true);

        animal3 = new Animal();
        animal3.setId(3L);
        animal3.setName("Fluffy");
        animal3.setSpecies("Cat");
        animal3.setBreed("Persian");
        animal3.setGender("Female");
        animal3.setBirthDate(LocalDate.of(2019, 7, 10));
        animal3.setColor("White");
        animal3.setWeight(new BigDecimal("4.5"));
        animal3.setMicrochipNumber("CHIP003");
        animal3.setOwnerName("Bob Johnson");
        animal3.setOwnerPhone("5555555555");
        animal3.setOwnerEmail("bob@example.com");
        animal3.setIsActive(true);
    }

    @Test
    @DisplayName("Should return paginated animals with default values (page=0, pageSize=20)")
    void testSearchPaginatedWithDefaults() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2, animal3));

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(null, null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(20, result.getPageSize());
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.getIsFirst());
        assertTrue(result.getIsLast());
        assertFalse(result.getHasNext());
        assertFalse(result.getHasPrevious());
        assertEquals(3, result.getContent().size());

        verify(animalRepository, times(1)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should return first page with custom page size")
    void testSearchPaginatedWithCustomPageSize() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2, animal3));

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(0, 2, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.getIsFirst());
        assertFalse(result.getIsLast());
        assertTrue(result.getHasNext());
        assertFalse(result.getHasPrevious());
        assertEquals(2, result.getContent().size());

        verify(animalRepository, times(1)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should return second page with correct data")
    void testSearchPaginatedSecondPage() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2, animal3));

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(1, 2, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertFalse(result.getIsFirst());
        assertTrue(result.getIsLast());
        assertFalse(result.getHasNext());
        assertTrue(result.getHasPrevious());
        assertEquals(1, result.getContent().size());
        // Items are sorted alphabetically: Bella, Fluffy, Rex
        assertEquals("Rex", result.getContent().get(0).getName());

        verify(animalRepository, times(1)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should return empty list when no animals exist")
    void testSearchPaginatedEmpty() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Collections.emptyList());

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(0, 20, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(20, result.getPageSize());
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.getIsFirst());
        assertTrue(result.getIsLast());
        assertFalse(result.getHasNext());
        assertFalse(result.getHasPrevious());
        assertEquals(0, result.getContent().size());

        verify(animalRepository, times(1)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should sort animals by name alphabetically")
    void testSearchPaginatedSortedByName() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2, animal3));

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(0, 20, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals("Bella", result.getContent().get(0).getName());
        assertEquals("Fluffy", result.getContent().get(1).getName());
        assertEquals("Rex", result.getContent().get(2).getName());

        verify(animalRepository, times(1)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should filter by species and return paginated results")
    void testSearchPaginatedWithSpeciesFilter() {
        // Given
        List<Animal> dogs = Arrays.asList(animal1, animal2);
        when(animalRepository.findBySpeciesAndIsActiveTrue("Dog")).thenReturn(dogs);

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(0, 20, null, "Dog", null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(a -> a.getSpecies().equals("Dog")));

        verify(animalRepository, times(1)).findBySpeciesAndIsActiveTrue("Dog");
    }

    @Test
    @DisplayName("Should filter by name and return paginated results")
    void testSearchPaginatedWithNameFilter() {
        // Given
        List<Animal> filtered = Collections.singletonList(animal2);
        when(animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrue("Rex")).thenReturn(filtered);

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(0, 20, "Rex", null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());
        assertEquals("Rex", result.getContent().get(0).getName());

        verify(animalRepository, times(1)).findByNameContainingIgnoreCaseAndIsActiveTrue("Rex");
    }

    @Test
    @DisplayName("Should filter by owner name and return paginated results")
    void testSearchPaginatedWithOwnerNameFilter() {
        // Given
        List<Animal> filtered = Collections.singletonList(animal1);
        when(animalRepository.findByOwnerNameContainingIgnoreCaseAndIsActiveTrue("John")).thenReturn(filtered);

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(0, 20, null, null, "John");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getOwnerName());

        verify(animalRepository, times(1)).findByOwnerNameContainingIgnoreCaseAndIsActiveTrue("John");
    }

    @Test
    @DisplayName("Should handle negative page number by resetting to 0")
    void testSearchPaginatedWithNegativePage() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2, animal3));

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(-1, 20, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(3, result.getContent().size());

        verify(animalRepository, times(1)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should handle page number beyond total pages by returning last page")
    void testSearchPaginatedWithPageBeyondTotal() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2, animal3));

        // When
        PageResponse<AnimalResponse> result = animalService.searchPaginated(100, 2, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPageNumber());
        assertEquals(1, result.getContent().size());
        assertTrue(result.getIsLast());

        verify(animalRepository, times(1)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should correctly calculate total pages")
    void testSearchPaginatedCalculateTotalPages() {
        // Given
        List<Animal> animals = Arrays.asList(animal1, animal2, animal3);
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(animals);

        // Test with page size 1 (3 elements = 3 pages)
        PageResponse<AnimalResponse> result1 = animalService.searchPaginated(0, 1, null, null, null);
        assertEquals(3, result1.getTotalPages());

        // Test with page size 2 (3 elements = 2 pages)
        PageResponse<AnimalResponse> result2 = animalService.searchPaginated(0, 2, null, null, null);
        assertEquals(2, result2.getTotalPages());

        // Test with page size 3 (3 elements = 1 page)
        PageResponse<AnimalResponse> result3 = animalService.searchPaginated(0, 3, null, null, null);
        assertEquals(1, result3.getTotalPages());

        verify(animalRepository, times(3)).findAllByIsActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should correctly populate pagination metadata")
    void testSearchPaginatedMetadata() {
        // Given
        when(animalRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2, animal3));

        // When - Get second page
        PageResponse<AnimalResponse> result = animalService.searchPaginated(1, 2, null, null, null);

        // Then - Verify metadata
        assertTrue(result.getHasPrevious());
        assertFalse(result.getHasNext());
        assertFalse(result.getIsFirst());
        assertTrue(result.getIsLast());
        assertEquals(1, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
    }
}

