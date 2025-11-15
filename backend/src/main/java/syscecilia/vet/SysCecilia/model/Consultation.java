package syscecilia.vet.SysCecilia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@NamedQueries({
        @NamedQuery(
                name = "Consultation.findByFilters",
                query = "SELECT c FROM Consultation c " +
                        "LEFT JOIN c.animal a " +
                        "WHERE (:animalName IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :animalName, '%'))) " +
                        "AND (:ownerName IS NULL OR LOWER(a.ownerName) LIKE LOWER(CONCAT('%', :ownerName, '%'))) " +
                        "AND (:veterinarianName IS NULL OR LOWER(c.veterinarianName) LIKE LOWER(CONCAT('%', :veterinarianName, '%'))) " +
                        "AND (:status IS NULL OR c.status = :status) " +
                        "AND (:reason IS NULL OR LOWER(c.reason) LIKE LOWER(CONCAT('%', :reason, '%'))) " +
                        "AND (:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
                        "AND (:createdAtStart IS NULL OR c.createdAt >= :createdAtStart) " +
                        "AND (:createdAtEnd IS NULL OR c.createdAt <= :createdAtEnd) " +
                        "ORDER BY c.consultationDate DESC"
        )
})
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(name = "consultation_date", nullable = false)
    private LocalDateTime consultationDate;

    @Column(name = "veterinarian_name", nullable = false, length = 100)
    private String veterinarianName;

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "diagnosis", length = 255)
    private String diagnosis;

    @Column(name = "treatment_prescribed", columnDefinition = "TEXT")
    private String treatmentPrescribed;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "next_appointment_date")
    private LocalDateTime nextAppointmentDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "COMPLETED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "COMPLETED";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Consultation() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
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

