package syscecilia.vet.SysCecilia.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnimalBasicInfo DTO Tests")
class AnimalBasicInfoTest {

    @Test
    @DisplayName("Should create AnimalBasicInfo with no-args constructor")
    void shouldCreateAnimalBasicInfoWithNoArgsConstructor() {
        // When
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo();
        
        // Then
        assertNotNull(animalBasicInfo);
        assertNull(animalBasicInfo.getId());
        assertNull(animalBasicInfo.getName());
        assertNull(animalBasicInfo.getSpecies());
        assertNull(animalBasicInfo.getBreed());
        assertNull(animalBasicInfo.getOwnerName());
    }

    @Test
    @DisplayName("Should create AnimalBasicInfo with all args constructor")
    void shouldCreateAnimalBasicInfoWithAllArgsConstructor() {
        // Given
        Long id = 1L;
        String name = "Rex";
        String species = "Dog";
        String breed = "Golden Retriever";
        String ownerName = "John Doe";
        
        // When
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo(id, name, species, breed, ownerName);
        
        // Then
        assertNotNull(animalBasicInfo);
        assertEquals(id, animalBasicInfo.getId());
        assertEquals(name, animalBasicInfo.getName());
        assertEquals(species, animalBasicInfo.getSpecies());
        assertEquals(breed, animalBasicInfo.getBreed());
        assertEquals(ownerName, animalBasicInfo.getOwnerName());
    }

    @Test
    @DisplayName("Should set and get id")
    void shouldSetAndGetId() {
        // Given
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo();
        Long id = 1L;
        
        // When
        animalBasicInfo.setId(id);
        
        // Then
        assertEquals(id, animalBasicInfo.getId());
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        // Given
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo();
        String name = "Fluffy";
        
        // When
        animalBasicInfo.setName(name);
        
        // Then
        assertEquals(name, animalBasicInfo.getName());
    }

    @Test
    @DisplayName("Should set and get species")
    void shouldSetAndGetSpecies() {
        // Given
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo();
        String species = "Cat";
        
        // When
        animalBasicInfo.setSpecies(species);
        
        // Then
        assertEquals(species, animalBasicInfo.getSpecies());
    }

    @Test
    @DisplayName("Should set and get breed")
    void shouldSetAndGetBreed() {
        // Given
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo();
        String breed = "Persian";
        
        // When
        animalBasicInfo.setBreed(breed);
        
        // Then
        assertEquals(breed, animalBasicInfo.getBreed());
    }

    @Test
    @DisplayName("Should set and get owner name")
    void shouldSetAndGetOwnerName() {
        // Given
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo();
        String ownerName = "Jane Smith";
        
        // When
        animalBasicInfo.setOwnerName(ownerName);
        
        // Then
        assertEquals(ownerName, animalBasicInfo.getOwnerName());
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // Given & When
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo(null, null, null, null, null);
        
        // Then
        assertNull(animalBasicInfo.getId());
        assertNull(animalBasicInfo.getName());
        assertNull(animalBasicInfo.getSpecies());
        assertNull(animalBasicInfo.getBreed());
        assertNull(animalBasicInfo.getOwnerName());
    }

    @Test
    @DisplayName("Should create with partial information")
    void shouldCreateWithPartialInformation() {
        // Given
        Long id = 5L;
        String name = "Max";
        
        // When
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo();
        animalBasicInfo.setId(id);
        animalBasicInfo.setName(name);
        
        // Then
        assertEquals(id, animalBasicInfo.getId());
        assertEquals(name, animalBasicInfo.getName());
        assertNull(animalBasicInfo.getSpecies());
        assertNull(animalBasicInfo.getBreed());
        assertNull(animalBasicInfo.getOwnerName());
    }
}

