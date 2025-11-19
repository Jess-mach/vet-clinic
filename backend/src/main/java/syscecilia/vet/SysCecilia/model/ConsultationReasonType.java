package syscecilia.vet.SysCecilia.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enum for consultation reason types")
public enum ConsultationReasonType {

    GENERAL_CHECKUP(1, "Consulta com clinico geral"),
    OPHTHALMOLOGY(2, "Consulta com oftalmologista"),
    CARDIOLOGY(3, "Consulta com cardiologista"),
    ORTHOPEDICS(4, "Consulta com ortopedista"),
    NEUROLOGY(5, "Consulta com neurologista"),
    EXAMS(6, "Exames"),
    IMAGING_EXAMS(7, "Exame de imagem (raio-x, ultrassom, etc.)"),
    VACCINATION(8, "Vacinação"),
    SURGERY(9, "Cirurgia"),
    FOLLOW_UP(10, "Retorno"),
    EMERGENCY(11, "Emergência"),
    EMERGENCY_URGENT(12, "Emergência Urgente");

    private final int id;
    private final String description;

    ConsultationReasonType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    @Schema(description = "Numeric ID stored in database", example = "1")
    public int getId() {
        return id;
    }

    @Schema(description = "Human readable description", example = "Consulta com clinico geral")
    public String getDescription() {
        return description;
    }

    public static ConsultationReasonType fromId(int id) {
        for (ConsultationReasonType value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid ConsultationReasonType id: " + id);
    }

    public static ConsultationReasonType fromDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Consultation reason description cannot be null");
        }
        for (ConsultationReasonType value : values()) {
            if (value.description.equalsIgnoreCase(description.trim())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid consultation reason description: " + description);
    }
}


