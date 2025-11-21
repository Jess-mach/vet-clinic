CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    animal_id BIGINT NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    veterinarian_name VARCHAR(100) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_appointments_animals FOREIGN KEY (animal_id) REFERENCES animals(id) ON DELETE CASCADE
);

CREATE INDEX idx_appointments_animal_id ON appointments(animal_id);
CREATE INDEX idx_appointments_appointment_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_status ON appointments(status);

COMMENT ON TABLE appointments IS 'Table to store appointment scheduling records for animals';
COMMENT ON COLUMN appointments.id IS 'Primary key identifier';
COMMENT ON COLUMN appointments.animal_id IS 'Foreign key reference to animals table';
COMMENT ON COLUMN appointments.appointment_date IS 'Date and time of the scheduled appointment';
COMMENT ON COLUMN appointments.veterinarian_name IS 'Name of the veterinarian assigned to the appointment';
COMMENT ON COLUMN appointments.reason IS 'Reason for the appointment';
COMMENT ON COLUMN appointments.notes IS 'Additional notes about the appointment';
COMMENT ON COLUMN appointments.status IS 'Status of the appointment (SCHEDULED, CONFIRMED, CANCELLED, COMPLETED)';
COMMENT ON COLUMN appointments.created_at IS 'Timestamp when the appointment record was created';
COMMENT ON COLUMN appointments.updated_at IS 'Timestamp when the appointment record was last updated';

