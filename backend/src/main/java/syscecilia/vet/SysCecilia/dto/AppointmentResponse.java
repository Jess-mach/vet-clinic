package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response DTO for appointment data")
public class AppointmentResponse {

    @Schema(description = "Appointment ID")
    private Long id;

    @Schema(description = "Animal information")
    private AnimalBasicInfo animal;

    @Schema(description = "Date and time of the appointment")
    private LocalDateTime appointmentDate;

    @Schema(description = "Name of the veterinarian")
    private String veterinarianName;

    @Schema(description = "Reason for the appointment")
    private String reason;

    @Schema(description = "Additional notes about the appointment")
    private String notes;

    @Schema(description = "Status of the appointment")
    private String status;

    @Schema(description = "Timestamp when the appointment record was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the appointment record was last updated")
    private LocalDateTime updatedAt;

    public AppointmentResponse() {
    }

    public AppointmentResponse(Long id, AnimalBasicInfo animal, LocalDateTime appointmentDate,
                              String veterinarianName, String reason, String notes, String status,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.animal = animal;
        this.appointmentDate = appointmentDate;
        this.veterinarianName = veterinarianName;
        this.reason = reason;
        this.notes = notes;
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

