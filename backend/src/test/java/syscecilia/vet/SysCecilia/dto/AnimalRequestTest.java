package syscecilia.vet.SysCecilia.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnimalRequest DTO Tests")
class AnimalRequestTest {

    private Validator validator;

    private AnimalRequest animalRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        animalRequest = new AnimalRequest();
    }

    @Test
    @DisplayName("Should create AnimalRequest with default constructor")
    void testAnimalRequestDefaultConstructor() {
        assertNotNull(animalRequest);
        assertNull(animalRequest.getName());
        assertNull(animalRequest.getSpecies());
    }

    @Test
    @DisplayName("Should set and get name")
    void testSetGetName() {
        String testName = "Rex";
        animalRequest.setName(testName);
        assertEquals(testName, animalRequest.getName());
    }

    @Test
    @DisplayName("Should set and get species")
    void testSetGetSpecies() {
        String testSpecies = "Dog";
        animalRequest.setSpecies(testSpecies);
        assertEquals(testSpecies, animalRequest.getSpecies());
    }

    @Test
    @DisplayName("Should set and get breed")
    void testSetGetBreed() {
        String testBreed = "Golden Retriever";
        animalRequest.setBreed(testBreed);
        assertEquals(testBreed, animalRequest.getBreed());
    }

    @Test
    @DisplayName("Should set and get gender")
    void testSetGetGender() {
        String testGender = "Male";
        animalRequest.setGender(testGender);
        assertEquals(testGender, animalRequest.getGender());
    }

    @Test
    @DisplayName("Should set and get birth date")
    void testSetGetBirthDate() {
        LocalDate testBirthDate = LocalDate.of(2020, 5, 15);
        animalRequest.setBirthDate(testBirthDate);
        assertEquals(testBirthDate, animalRequest.getBirthDate());
    }

    @Test
    @DisplayName("Should set and get color")
    void testSetGetColor() {
        String testColor = "Golden";
        animalRequest.setColor(testColor);
        assertEquals(testColor, animalRequest.getColor());
    }

    @Test
    @DisplayName("Should set and get weight")
    void testSetGetWeight() {
        BigDecimal testWeight = new BigDecimal("25.50");
        animalRequest.setWeight(testWeight);
        assertEquals(testWeight, animalRequest.getWeight());
    }

    @Test
    @DisplayName("Should set and get microchip number")
    void testSetGetMicrochipNumber() {
        String testMicrochip = "CHIP001";
        animalRequest.setMicrochipNumber(testMicrochip);
        assertEquals(testMicrochip, animalRequest.getMicrochipNumber());
    }

    @Test
    @DisplayName("Should set and get owner name")
    void testSetGetOwnerName() {
        String testOwnerName = "John Doe";
        animalRequest.setOwnerName(testOwnerName);
        assertEquals(testOwnerName, animalRequest.getOwnerName());
    }

    @Test
    @DisplayName("Should set and get owner phone")
    void testSetGetOwnerPhone() {
        String testOwnerPhone = "1234567890";
        animalRequest.setOwnerPhone(testOwnerPhone);
        assertEquals(testOwnerPhone, animalRequest.getOwnerPhone());
    }

    @Test
    @DisplayName("Should set and get owner email")
    void testSetGetOwnerEmail() {
        String testOwnerEmail = "john@example.com";
        animalRequest.setOwnerEmail(testOwnerEmail);
        assertEquals(testOwnerEmail, animalRequest.getOwnerEmail());
    }

    // Validation Tests

    @Test
    @DisplayName("Should fail validation when name is blank")
    void testValidationFailWhenNameIsBlank() {
        animalRequest.setName("");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    @DisplayName("Should fail validation when species is blank")
    void testValidationFailWhenSpeciesIsBlank() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("species")));
    }

    @Test
    @DisplayName("Should fail validation when gender is blank")
    void testValidationFailWhenGenderIsBlank() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("");
        animalRequest.setOwnerName("John Doe");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("gender")));
    }

    @Test
    @DisplayName("Should fail validation when owner name is blank")
    void testValidationFailWhenOwnerNameIsBlank() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ownerName")));
    }

    @Test
    @DisplayName("Should fail validation when name exceeds maximum length")
    void testValidationFailWhenNameExceedsMaxLength() {
        animalRequest.setName("A".repeat(101));
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    @DisplayName("Should fail validation when species exceeds maximum length")
    void testValidationFailWhenSpeciesExceedsMaxLength() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("B".repeat(51));
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("species")));
    }

    @Test
    @DisplayName("Should fail validation when birth date is in the future")
    void testValidationFailWhenBirthDateIsInFuture() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");
        animalRequest.setBirthDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthDate")));
    }

    @Test
    @DisplayName("Should fail validation when weight is less than 0.01")
    void testValidationFailWhenWeightIsTooSmall() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");
        animalRequest.setWeight(new BigDecimal("0.00"));

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("weight")));
    }

    @Test
    @DisplayName("Should fail validation when weight exceeds 999.99")
    void testValidationFailWhenWeightIsTooBig() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");
        animalRequest.setWeight(new BigDecimal("1000.00"));

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("weight")));
    }

    @Test
    @DisplayName("Should fail validation when owner email is invalid")
    void testValidationFailWhenOwnerEmailIsInvalid() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");
        animalRequest.setOwnerEmail("invalid-email");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ownerEmail")));
    }

    @Test
    @DisplayName("Should fail validation when owner phone has invalid characters")
    void testValidationFailWhenOwnerPhoneHasInvalidCharacters() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");
        animalRequest.setOwnerPhone("123abc456");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ownerPhone")));
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void testValidationPassWithValidData() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setBreed("Golden Retriever");
        animalRequest.setGender("Male");
        animalRequest.setBirthDate(LocalDate.of(2020, 5, 15));
        animalRequest.setColor("Golden");
        animalRequest.setWeight(new BigDecimal("25.50"));
        animalRequest.setMicrochipNumber("CHIP001");
        animalRequest.setOwnerName("John Doe");
        animalRequest.setOwnerPhone("+1 (123) 456-7890");
        animalRequest.setOwnerEmail("john@example.com");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should pass validation with required fields only")
    void testValidationPassWithRequiredFieldsOnly() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");

        Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should accept valid phone formats")
    void testValidPhoneFormats() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");

        String[] validPhones = {"+1234567890", "(123) 456-7890", "+55 11 9999-8888", "1234567890"};

        for (String phone : validPhones) {
            animalRequest.setOwnerPhone(phone);
            Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
            assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("ownerPhone")),
                    "Phone format should be valid: " + phone);
        }
    }

    @Test
    @DisplayName("Should accept valid email formats")
    void testValidEmailFormats() {
        animalRequest.setName("Rex");
        animalRequest.setSpecies("Dog");
        animalRequest.setGender("Male");
        animalRequest.setOwnerName("John Doe");

        String[] validEmails = {"john@example.com", "test.email@domain.co.uk", "user+tag@example.com"};

        for (String email : validEmails) {
            animalRequest.setOwnerEmail(email);
            Set<ConstraintViolation<AnimalRequest>> violations = validator.validate(animalRequest);
            assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("ownerEmail")),
                    "Email format should be valid: " + email);
        }
    }
}

