package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "Request DTO for creating an appointment")
public class AppointmentRequest {

    @NotNull(message = "Animal ID is required")
    @Min(value = 1, message = "Animal ID must be greater than 0")
    @Schema(description = "ID of the animal", example = "1", required = true)
    private Long animalId;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    @Schema(description = "Date and time of the appointment (ISO-8601 format)", example = "2025-12-20T14:30:00", required = true)
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Veterinarian name is required")
    @Size(max = 100, message = "Veterinarian name must not exceed 100 characters")
    @Schema(description = "Name of the veterinarian", example = "Dr. Silva", required = true, maxLength = 100)
    private String veterinarianName;

    @NotBlank(message = "Reason is required")
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    @Schema(description = "Reason for the appointment", example = "Routine checkup", required = true, maxLength = 255)
    private String reason;

    @Size(max = 5000, message = "Notes must not exceed 5000 characters")
    @Schema(description = "Additional notes about the appointment", example = "First visit for this animal", maxLength = 5000)
    private String notes;

    public AppointmentRequest() {
    }

    // Getters and Setters
    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }

    public void setVeterinarianName(String veterinarianName) {
        this.veterinarianName = veterinarianName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

