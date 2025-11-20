package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import syscecilia.vet.SysCecilia.model.ConsultationReasonType;

import java.time.LocalDateTime;

@Schema(description = "Response DTO for veterinarian information")
public class VeterinarianResponse {

    @Schema(description = "Unique identifier of the veterinarian", example = "1")
    private Long id;

    @Schema(description = "Name of the veterinarian", example = "Dr. Amelia Rivers")
    private String name;

    @Schema(description = "Specialty code (reference to ConsultationReasonType)", example = "2")
    private Integer specialtyCode;

    @Schema(description = "Specialty description", example = "Consulta com oftalmologista")
    private String specialty;

    @Schema(description = "Timestamp when the veterinarian was created", example = "2025-11-19T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the veterinarian was last updated", example = "2025-11-19T10:30:00")
    private LocalDateTime updatedAt;

    public VeterinarianResponse() {
    }

    public VeterinarianResponse(Long id, String name, Integer specialtyCode, String specialty, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.specialtyCode = specialtyCode;
        this.specialty = specialty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSpecialtyCode() {
        return specialtyCode;
    }

    public void setSpecialtyCode(Integer specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

