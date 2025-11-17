CREATE TABLE consultations (
    id BIGSERIAL PRIMARY KEY,
    animal_id BIGINT NOT NULL,
    consultation_date TIMESTAMP NOT NULL,
    veterinarian_name VARCHAR(100) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    description TEXT,
    diagnosis VARCHAR(255),
    treatment_prescribed TEXT,
    observations TEXT,
    next_appointment_date TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_consultations_animals FOREIGN KEY (animal_id) REFERENCES animals(id) ON DELETE CASCADE
);

CREATE INDEX idx_consultations_animal_id ON consultations(animal_id);
CREATE INDEX idx_consultations_consultation_date ON consultations(consultation_date);
CREATE INDEX idx_consultations_status ON consultations(status);

COMMENT ON TABLE consultations IS 'Table to store consultation records for animals';
COMMENT ON COLUMN consultations.id IS 'Primary key identifier';
COMMENT ON COLUMN consultations.animal_id IS 'Foreign key reference to animals table';
COMMENT ON COLUMN consultations.consultation_date IS 'Date and time of the consultation';
COMMENT ON COLUMN consultations.veterinarian_name IS 'Name of the veterinarian';
COMMENT ON COLUMN consultations.reason IS 'Reason for the consultation';
COMMENT ON COLUMN consultations.description IS 'Detailed description of the consultation';
COMMENT ON COLUMN consultations.diagnosis IS 'Diagnosis from the consultation';
COMMENT ON COLUMN consultations.treatment_prescribed IS 'Treatment prescribed';
COMMENT ON COLUMN consultations.observations IS 'Additional observations';
COMMENT ON COLUMN consultations.next_appointment_date IS 'Date of the next scheduled appointment';
COMMENT ON COLUMN consultations.status IS 'Status of the consultation (COMPLETED, SCHEDULED, CANCELLED)';
COMMENT ON COLUMN consultations.created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN consultations.updated_at IS 'Timestamp when the record was last updated';

