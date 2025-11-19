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
    private String veterinarianId;

    @Schema(description = "Name of the veterinarian")
    private String veterinarianName;

    @Schema(
            description = "Reason code for the consultation (numeric id from ConsultationReasonType enum)",
            example = "1"
    )
    private Integer reasonCode;

    @Schema(description = "Reason description for the consultation")
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

    public ConsultationResponse(Long id,
                                AnimalBasicInfo animal,
                                LocalDateTime consultationDate,
                                String veterinarianName,
                                Integer reasonCode,
                                String reason,
                                String description,
                                String diagnosis,
                                String treatmentPrescribed,
                                String observations,
                                LocalDateTime nextAppointmentDate,
                                String status,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {
        this.id = id;
        this.animal = animal;
        this.consultationDate = consultationDate;
        this.veterinarianName = getVeterinarianSelect(veterinarianName);
        this.veterinarianId = veterinarianName;
        this.reasonCode = reasonCode;
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

    private String getVeterinarianSelect(String veterinarianId) {
        switch (veterinarianId) {
            case "1":  return "Dr. Amelia Rivers";
            case "2":  return "Dr. Noah Bennett";
            case "3":  return "Dr. Olivia Carter";
            case "4":  return "Dr. Ethan Walker";
            case "5":  return "Dr. Sophia Hayes";
            case "6":  return "Dr. Lucas Griffin";
            case "7":  return "Dr. Harper Collins";
            case "8":  return "Dr. Mason Clarke";
            case "9":  return "Dr. Isla Morgan";
            case "10": return "Dr. Leo Harrison";
            case "11": return "Dr. Aria Mitchell";
            case "12": return "Dr. Daniel Brooks";
            case "13": return "Dr. Chloe Parker";
            case "14": return "Dr. Henry Coleman";
            case "15": return "Dr. Avery Scott";
            default:
                throw new IllegalArgumentException("Unknown veterinarian id: " + id);
        }
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

    public Integer getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(Integer reasonCode) {
        this.reasonCode = reasonCode;
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

    public String getVeterinarianId() {
        return veterinarianId;
    }

    public void setVeterinarianId(String veterinarianId) {
        this.veterinarianId = veterinarianId;
    }
}

