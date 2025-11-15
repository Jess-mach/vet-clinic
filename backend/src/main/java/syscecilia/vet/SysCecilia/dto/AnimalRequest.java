package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Request DTO for creating a new animal")
public class AnimalRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Schema(description = "Animal name", example = "Rex", required = true, maxLength = 100)
    private String name;

    @NotBlank(message = "Species is required")
    @Size(max = 50, message = "Species must not exceed 50 characters")
    @Schema(description = "Animal species (e.g., Dog, Cat, Bird)", example = "Dog", required = true, maxLength = 50)
    private String species;

    @Size(max = 100, message = "Breed must not exceed 100 characters")
    @Schema(description = "Animal breed", example = "Golden Retriever", maxLength = 100)
    private String breed;

    @NotBlank(message = "Gender is required")
    @Size(max = 20, message = "Gender must not exceed 20 characters")
    @Schema(description = "Animal gender (Male, Female, Neutered, Spayed)", example = "Male", required = true, maxLength = 20)
    private String gender;

    @PastOrPresent(message = "Birth date cannot be in the future")
    @Schema(description = "Animal birth date (YYYY-MM-DD)", example = "2020-05-15", type = "string", format = "date")
    private LocalDate birthDate;

    @Size(max = 50, message = "Color must not exceed 50 characters")
    @Schema(description = "Animal color", example = "Golden", maxLength = 50)
    private String color;

    @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
    @DecimalMax(value = "999.99", message = "Weight must not exceed 999.99 kg")
    @Digits(integer = 3, fraction = 2, message = "Weight must have at most 3 integer digits and 2 decimal places")
    @Schema(description = "Animal weight in kilograms", example = "25.5", minimum = "0.01", maximum = "999.99")
    private BigDecimal weight;

    @Size(max = 50, message = "Microchip number must not exceed 50 characters")
    @Schema(description = "Microchip identification number (must be unique)", example = "CHIP001", maxLength = 50)
    private String microchipNumber;

    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name must not exceed 100 characters")
    @Schema(description = "Owner name", example = "John Doe", required = true, maxLength = 100)
    private String ownerName;

    @Size(max = 20, message = "Owner phone must not exceed 20 characters")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Owner phone must contain only numbers, spaces, hyphens, parentheses, and optional plus sign")
    @Schema(description = "Owner phone number", example = "1234567890", maxLength = 20)
    private String ownerPhone;

    @Size(max = 100, message = "Owner email must not exceed 100 characters")
    @Email(message = "Owner email must be a valid email address")
    @Schema(description = "Owner email address", example = "john@example.com", maxLength = 100, format = "email")
    private String ownerEmail;

    public AnimalRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getMicrochipNumber() {
        return microchipNumber;
    }

    public void setMicrochipNumber(String microchipNumber) {
        this.microchipNumber = microchipNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}


