package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "Request DTO for creating or updating a consultation")
public class ConsultationRequest {

    @NotNull(message = "Animal ID is required")
    @Min(value = 1, message = "Animal ID must be greater than 0")
    @Schema(description = "ID of the animal", example = "1", required = true)
    private Long animalId;

    @NotNull(message = "Consultation date is required")
    @Schema(description = "Date and time of the consultation (ISO-8601 format)", example = "2025-11-15T14:30:00", required = true)
    private LocalDateTime consultationDate;

    @NotBlank(message = "Veterinarian name is required")
    @Size(max = 100, message = "Veterinarian name must not exceed 100 characters")
    @Schema(description = "Name of the veterinarian", example = "Dr. Silva", required = true, maxLength = 100)
    private String veterinarianName;

    @NotBlank(message = "Reason is required")
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    @Schema(description = "Reason for the consultation", example = "Routine checkup", required = true, maxLength = 255)
    private String reason;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    @Schema(description = "Detailed description of the consultation", example = "General health examination performed", maxLength = 5000)
    private String description;

    @Size(max = 255, message = "Diagnosis must not exceed 255 characters")
    @Schema(description = "Diagnosis from the consultation", example = "Healthy", maxLength = 255)
    private String diagnosis;

    @Size(max = 5000, message = "Treatment prescribed must not exceed 5000 characters")
    @Schema(description = "Treatment prescribed", example = "Continue with regular diet", maxLength = 5000)
    private String treatmentPrescribed;

    @Size(max = 5000, message = "Observations must not exceed 5000 characters")
    @Schema(description = "Additional observations", example = "Schedule next checkup in 6 months", maxLength = 5000)
    private String observations;

    @Schema(description = "Date of the next scheduled appointment (ISO-8601 format)", example = "2026-05-15T14:30:00")
    private LocalDateTime nextAppointmentDate;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    @Schema(description = "Status of the consultation (COMPLETED, SCHEDULED, CANCELLED)", example = "COMPLETED", maxLength = 20)
    private String status;

    public ConsultationRequest() {
    }

    // Getters and Setters
    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public LocalDateTime getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDateTime consultationDate) {
        this.consultationDate = consultationDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentPrescribed() {
        return treatmentPrescribed;
    }

    public void setTreatmentPrescribed(String treatmentPrescribed) {
        this.treatmentPrescribed = treatmentPrescribed;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDateTime getNextAppointmentDate() {
        return nextAppointmentDate;
    }

    public void setNextAppointmentDate(LocalDateTime nextAppointmentDate) {
        this.nextAppointmentDate = nextAppointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

