package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response DTO for consultation data")
public class ConsultationResponse {

    @Schema(description = "Consultation ID")
    private Long id;

    @Schema(description = "Animal information")
    private AnimalBasicInfo animal;

    @Schema(description = "Date and time of the consultation")
    private LocalDateTime consultationDate;

    @Schema(description = "Name of the veterinarian")
    private String veterinarianName;

    @Schema(description = "Reason for the consultation")
    private String reason;

    @Schema(description = "Detailed description of the consultation")
    private String description;

    @Schema(description = "Diagnosis from the consultation")
    private String diagnosis;

    @Schema(description = "Treatment prescribed")
    private String treatmentPrescribed;

    @Schema(description = "Additional observations")
    private String observations;

    @Schema(description = "Date of the next scheduled appointment")
    private LocalDateTime nextAppointmentDate;

    @Schema(description = "Status of the consultation")
    private String status;

    @Schema(description = "Timestamp when the consultation record was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the consultation record was last updated")
    private LocalDateTime updatedAt;

    public ConsultationResponse() {
    }

    public ConsultationResponse(Long id, AnimalBasicInfo animal, LocalDateTime consultationDate,
                               String veterinarianName, String reason, String description, String diagnosis,
                               String treatmentPrescribed, String observations, LocalDateTime nextAppointmentDate,
                               String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.animal = animal;
        this.consultationDate = consultationDate;
        this.veterinarianName = veterinarianName;
        this.reason = reason;
        this.description = description;
        this.diagnosis = diagnosis;
        this.treatmentPrescribed = treatmentPrescribed;
        this.observations = observations;
        this.nextAppointmentDate = nextAppointmentDate;
        this.status = status;
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

    public AnimalBasicInfo getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalBasicInfo animal) {
        this.animal = animal;
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

