package syscecilia.vet.SysCecilia.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnimalResponse DTO Tests")
class AnimalResponseTest {

    private AnimalResponse animalResponse;

    @BeforeEach
    void setUp() {
        animalResponse = new AnimalResponse();
    }

    @Test
    @DisplayName("Should create AnimalResponse with default constructor")
    void testAnimalResponseDefaultConstructor() {
        assertNotNull(animalResponse);
        assertNull(animalResponse.getId());
        assertNull(animalResponse.getName());
        assertNull(animalResponse.getSpecies());
    }

    @Test
    @DisplayName("Should create AnimalResponse with full constructor")
    void testAnimalResponseFullConstructor() {
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

        AnimalResponse response = new AnimalResponse(
                id, name, species, breed, gender, birthDate, color, weight,
                microchipNumber, ownerName, ownerPhone, ownerEmail, createdAt, updatedAt
        );

        assertEquals(id, response.getId());
        assertEquals(name, response.getName());
        assertEquals(species, response.getSpecies());
        assertEquals(breed, response.getBreed());
        assertEquals(gender, response.getGender());
        assertEquals(birthDate, response.getBirthDate());
        assertEquals(color, response.getColor());
        assertEquals(weight, response.getWeight());
        assertEquals(microchipNumber, response.getMicrochipNumber());
        assertEquals(ownerName, response.getOwnerName());
        assertEquals(ownerPhone, response.getOwnerPhone());
        assertEquals(ownerEmail, response.getOwnerEmail());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get id")
    void testSetGetId() {
        Long testId = 1L;
        animalResponse.setId(testId);
        assertEquals(testId, animalResponse.getId());
    }

    @Test
    @DisplayName("Should set and get name")
    void testSetGetName() {
        String testName = "Rex";
        animalResponse.setName(testName);
        assertEquals(testName, animalResponse.getName());
    }

    @Test
    @DisplayName("Should set and get species")
    void testSetGetSpecies() {
        String testSpecies = "Dog";
        animalResponse.setSpecies(testSpecies);
        assertEquals(testSpecies, animalResponse.getSpecies());
    }

    @Test
    @DisplayName("Should set and get breed")
    void testSetGetBreed() {
        String testBreed = "Golden Retriever";
        animalResponse.setBreed(testBreed);
        assertEquals(testBreed, animalResponse.getBreed());
    }

    @Test
    @DisplayName("Should set and get gender")
    void testSetGetGender() {
        String testGender = "Male";
        animalResponse.setGender(testGender);
        assertEquals(testGender, animalResponse.getGender());
    }

    @Test
    @DisplayName("Should set and get birth date")
    void testSetGetBirthDate() {
        LocalDate testBirthDate = LocalDate.of(2020, 5, 15);
        animalResponse.setBirthDate(testBirthDate);
        assertEquals(testBirthDate, animalResponse.getBirthDate());
    }

    @Test
    @DisplayName("Should set and get color")
    void testSetGetColor() {
        String testColor = "Golden";
        animalResponse.setColor(testColor);
        assertEquals(testColor, animalResponse.getColor());
    }

    @Test
    @DisplayName("Should set and get weight")
    void testSetGetWeight() {
        BigDecimal testWeight = new BigDecimal("25.50");
        animalResponse.setWeight(testWeight);
        assertEquals(testWeight, animalResponse.getWeight());
    }

    @Test
    @DisplayName("Should set and get microchip number")
    void testSetGetMicrochipNumber() {
        String testMicrochip = "CHIP001";
        animalResponse.setMicrochipNumber(testMicrochip);
        assertEquals(testMicrochip, animalResponse.getMicrochipNumber());
    }

    @Test
    @DisplayName("Should set and get owner name")
    void testSetGetOwnerName() {
        String testOwnerName = "John Doe";
        animalResponse.setOwnerName(testOwnerName);
        assertEquals(testOwnerName, animalResponse.getOwnerName());
    }

    @Test
    @DisplayName("Should set and get owner phone")
    void testSetGetOwnerPhone() {
        String testOwnerPhone = "1234567890";
        animalResponse.setOwnerPhone(testOwnerPhone);
        assertEquals(testOwnerPhone, animalResponse.getOwnerPhone());
    }

    @Test
    @DisplayName("Should set and get owner email")
    void testSetGetOwnerEmail() {
        String testOwnerEmail = "john@example.com";
        animalResponse.setOwnerEmail(testOwnerEmail);
        assertEquals(testOwnerEmail, animalResponse.getOwnerEmail());
    }

    @Test
    @DisplayName("Should set and get created at")
    void testSetGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        animalResponse.setCreatedAt(now);
        assertEquals(now, animalResponse.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updated at")
    void testSetGetUpdatedAt() {
        LocalDateTime now = LocalDateTime.now();
        animalResponse.setUpdatedAt(now);
        assertEquals(now, animalResponse.getUpdatedAt());
    }

    @Test
    @DisplayName("Should populate response with all fields")
    void testAnimalResponseWithAllFields() {
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

        animalResponse.setId(id);
        animalResponse.setName(name);
        animalResponse.setSpecies(species);
        animalResponse.setBreed(breed);
        animalResponse.setGender(gender);
        animalResponse.setBirthDate(birthDate);
        animalResponse.setColor(color);
        animalResponse.setWeight(weight);
        animalResponse.setMicrochipNumber(microchipNumber);
        animalResponse.setOwnerName(ownerName);
        animalResponse.setOwnerPhone(ownerPhone);
        animalResponse.setOwnerEmail(ownerEmail);
        animalResponse.setCreatedAt(createdAt);
        animalResponse.setUpdatedAt(updatedAt);

        assertEquals(id, animalResponse.getId());
        assertEquals(name, animalResponse.getName());
        assertEquals(species, animalResponse.getSpecies());
        assertEquals(breed, animalResponse.getBreed());
        assertEquals(gender, animalResponse.getGender());
        assertEquals(birthDate, animalResponse.getBirthDate());
        assertEquals(color, animalResponse.getColor());
        assertEquals(weight, animalResponse.getWeight());
        assertEquals(microchipNumber, animalResponse.getMicrochipNumber());
        assertEquals(ownerName, animalResponse.getOwnerName());
        assertEquals(ownerPhone, animalResponse.getOwnerPhone());
        assertEquals(ownerEmail, animalResponse.getOwnerEmail());
        assertEquals(createdAt, animalResponse.getCreatedAt());
        assertEquals(updatedAt, animalResponse.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle null values for optional fields")
    void testAnimalResponseWithNullOptionalFields() {
        animalResponse.setId(1L);
        animalResponse.setName("Rex");
        animalResponse.setSpecies("Dog");
        animalResponse.setGender("Male");
        animalResponse.setOwnerName("John Doe");

        animalResponse.setBreed(null);
        animalResponse.setColor(null);
        animalResponse.setWeight(null);
        animalResponse.setMicrochipNumber(null);
        animalResponse.setOwnerPhone(null);
        animalResponse.setOwnerEmail(null);
        animalResponse.setBirthDate(null);

        assertNull(animalResponse.getBreed());
        assertNull(animalResponse.getColor());
        assertNull(animalResponse.getWeight());
        assertNull(animalResponse.getMicrochipNumber());
        assertNull(animalResponse.getOwnerPhone());
        assertNull(animalResponse.getOwnerEmail());
        assertNull(animalResponse.getBirthDate());
    }

    @Test
    @DisplayName("Should handle edge case values")
    void testAnimalResponseEdgeCaseValues() {
        // Test with maximum length values
        String maxLengthName = "A".repeat(100);
        String maxLengthSpecies = "B".repeat(50);
        String maxLengthOwnerName = "C".repeat(100);

        animalResponse.setName(maxLengthName);
        animalResponse.setSpecies(maxLengthSpecies);
        animalResponse.setOwnerName(maxLengthOwnerName);

        assertEquals(maxLengthName, animalResponse.getName());
        assertEquals(maxLengthSpecies, animalResponse.getSpecies());
        assertEquals(maxLengthOwnerName, animalResponse.getOwnerName());
    }

    @Test
    @DisplayName("Should handle zero and very large weight values")
    void testAnimalResponseWeightEdgeCases() {
        BigDecimal smallWeight = new BigDecimal("0.01");
        BigDecimal largeWeight = new BigDecimal("999.99");

        animalResponse.setWeight(smallWeight);
        assertEquals(smallWeight, animalResponse.getWeight());

        animalResponse.setWeight(largeWeight);
        assertEquals(largeWeight, animalResponse.getWeight());
    }

    @Test
    @DisplayName("Should handle timestamp fields correctly")
    void testAnimalResponseTimestampFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(1);

        animalResponse.setCreatedAt(now);
        animalResponse.setUpdatedAt(later);

        assertEquals(now, animalResponse.getCreatedAt());
        assertEquals(later, animalResponse.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle different date values")
    void testAnimalResponseDifferentDateValues() {
        LocalDate pastDate = LocalDate.of(2010, 1, 1);
        LocalDate recentDate = LocalDate.of(2023, 12, 25);
        LocalDate today = LocalDate.now();

        animalResponse.setBirthDate(pastDate);
        assertEquals(pastDate, animalResponse.getBirthDate());

        animalResponse.setBirthDate(recentDate);
        assertEquals(recentDate, animalResponse.getBirthDate());

        animalResponse.setBirthDate(today);
        assertEquals(today, animalResponse.getBirthDate());
    }
}

