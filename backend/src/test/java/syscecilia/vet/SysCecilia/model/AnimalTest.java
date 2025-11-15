package syscecilia.vet.SysCecilia.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Animal Model Tests")
class AnimalTest {

    private Animal animal;

    @BeforeEach
    void setUp() {
        animal = new Animal();
    }

    @Test
    @DisplayName("Should create an Animal with default constructor")
    void testAnimalDefaultConstructor() {
        assertNotNull(animal);
        assertNull(animal.getId());
        assertNull(animal.getName());
        assertNull(animal.getSpecies());
    }

    @Test
    @DisplayName("Should set and get id")
    void testSetGetId() {
        Long testId = 1L;
        animal.setId(testId);
        assertEquals(testId, animal.getId());
    }

    @Test
    @DisplayName("Should set and get name")
    void testSetGetName() {
        String testName = "Rex";
        animal.setName(testName);
        assertEquals(testName, animal.getName());
    }

    @Test
    @DisplayName("Should set and get species")
    void testSetGetSpecies() {
        String testSpecies = "Dog";
        animal.setSpecies(testSpecies);
        assertEquals(testSpecies, animal.getSpecies());
    }

    @Test
    @DisplayName("Should set and get breed")
    void testSetGetBreed() {
        String testBreed = "Golden Retriever";
        animal.setBreed(testBreed);
        assertEquals(testBreed, animal.getBreed());
    }

    @Test
    @DisplayName("Should set and get gender")
    void testSetGetGender() {
        String testGender = "Male";
        animal.setGender(testGender);
        assertEquals(testGender, animal.getGender());
    }

    @Test
    @DisplayName("Should set and get birth date")
    void testSetGetBirthDate() {
        LocalDate testBirthDate = LocalDate.of(2020, 5, 15);
        animal.setBirthDate(testBirthDate);
        assertEquals(testBirthDate, animal.getBirthDate());
    }

    @Test
    @DisplayName("Should set and get color")
    void testSetGetColor() {
        String testColor = "Golden";
        animal.setColor(testColor);
        assertEquals(testColor, animal.getColor());
    }

    @Test
    @DisplayName("Should set and get weight")
    void testSetGetWeight() {
        BigDecimal testWeight = new BigDecimal("25.50");
        animal.setWeight(testWeight);
        assertEquals(testWeight, animal.getWeight());
    }

    @Test
    @DisplayName("Should set and get microchip number")
    void testSetGetMicrochipNumber() {
        String testMicrochip = "CHIP001";
        animal.setMicrochipNumber(testMicrochip);
        assertEquals(testMicrochip, animal.getMicrochipNumber());
    }

    @Test
    @DisplayName("Should set and get owner name")
    void testSetGetOwnerName() {
        String testOwnerName = "John Doe";
        animal.setOwnerName(testOwnerName);
        assertEquals(testOwnerName, animal.getOwnerName());
    }

    @Test
    @DisplayName("Should set and get owner phone")
    void testSetGetOwnerPhone() {
        String testOwnerPhone = "1234567890";
        animal.setOwnerPhone(testOwnerPhone);
        assertEquals(testOwnerPhone, animal.getOwnerPhone());
    }

    @Test
    @DisplayName("Should set and get owner email")
    void testSetGetOwnerEmail() {
        String testOwnerEmail = "john@example.com";
        animal.setOwnerEmail(testOwnerEmail);
        assertEquals(testOwnerEmail, animal.getOwnerEmail());
    }

    @Test
    @DisplayName("Should set and get created at")
    void testSetGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        animal.setCreatedAt(now);
        assertEquals(now, animal.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updated at")
    void testSetGetUpdatedAt() {
        LocalDateTime now = LocalDateTime.now();
        animal.setUpdatedAt(now);
        assertEquals(now, animal.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create animal with all fields populated")
    void testAnimalWithAllFields() {
        Long id = 1L;
        String name = "Rex";
        String species = "Dog";
        String breed = "Golden Retriever";
        String gender = "Male";
        LocalDate birthDate = LocalDate.of(2020, 5, 15);
        String color = "Golden";
        BigDecimal weight = new BigDecimal("25.50");
        String microchipNumber = "CHIP001";
        String ownerName = "John Doe";
        String ownerPhone = "1234567890";
        String ownerEmail = "john@example.com";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        animal.setId(id);
        animal.setName(name);
        animal.setSpecies(species);
        animal.setBreed(breed);
        animal.setGender(gender);
        animal.setBirthDate(birthDate);
        animal.setColor(color);
        animal.setWeight(weight);
        animal.setMicrochipNumber(microchipNumber);
        animal.setOwnerName(ownerName);
        animal.setOwnerPhone(ownerPhone);
        animal.setOwnerEmail(ownerEmail);
        animal.setCreatedAt(createdAt);
        animal.setUpdatedAt(updatedAt);

        assertEquals(id, animal.getId());
        assertEquals(name, animal.getName());
        assertEquals(species, animal.getSpecies());
        assertEquals(breed, animal.getBreed());
        assertEquals(gender, animal.getGender());
        assertEquals(birthDate, animal.getBirthDate());
        assertEquals(color, animal.getColor());
        assertEquals(weight, animal.getWeight());
        assertEquals(microchipNumber, animal.getMicrochipNumber());
        assertEquals(ownerName, animal.getOwnerName());
        assertEquals(ownerPhone, animal.getOwnerPhone());
        assertEquals(ownerEmail, animal.getOwnerEmail());
        assertEquals(createdAt, animal.getCreatedAt());
        assertEquals(updatedAt, animal.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle null values for optional fields")
    void testAnimalWithNullOptionalFields() {
        animal.setName("Rex");
        animal.setSpecies("Dog");
        animal.setGender("Male");
        animal.setOwnerName("John Doe");

        animal.setBreed(null);
        animal.setColor(null);
        animal.setWeight(null);
        animal.setMicrochipNumber(null);
        animal.setOwnerPhone(null);
        animal.setOwnerEmail(null);
        animal.setBirthDate(null);

        assertNull(animal.getBreed());
        assertNull(animal.getColor());
        assertNull(animal.getWeight());
        assertNull(animal.getMicrochipNumber());
        assertNull(animal.getOwnerPhone());
        assertNull(animal.getOwnerEmail());
        assertNull(animal.getBirthDate());
    }

    @Test
    @DisplayName("Should handle edge case values")
    void testAnimalEdgeCaseValues() {
        // Test with maximum length values
        String maxLengthName = "A".repeat(100);
        String maxLengthSpecies = "B".repeat(50);
        String maxLengthOwnerName = "C".repeat(100);

        animal.setName(maxLengthName);
        animal.setSpecies(maxLengthSpecies);
        animal.setOwnerName(maxLengthOwnerName);

        assertEquals(maxLengthName, animal.getName());
        assertEquals(maxLengthSpecies, animal.getSpecies());
        assertEquals(maxLengthOwnerName, animal.getOwnerName());
    }

    @Test
    @DisplayName("Should handle zero and very large weight values")
    void testAnimalWeightEdgeCases() {
        BigDecimal smallWeight = new BigDecimal("0.01");
        BigDecimal largeWeight = new BigDecimal("999.99");

        animal.setWeight(smallWeight);
        assertEquals(smallWeight, animal.getWeight());

        animal.setWeight(largeWeight);
        assertEquals(largeWeight, animal.getWeight());
    }
}

