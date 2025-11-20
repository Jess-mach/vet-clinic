-- Migration: Create veterinarians table
-- Date: 2025-11-19
-- Description: Creates the veterinarians table to store veterinarian information with their specialties

CREATE TABLE veterinarians (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty_code INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX idx_veterinarians_name ON veterinarians(name);
CREATE INDEX idx_veterinarians_specialty_code ON veterinarians(specialty_code);

-- Add comments for documentation
COMMENT ON TABLE veterinarians IS 'Table to store veterinarian information including their specialties';
COMMENT ON COLUMN veterinarians.id IS 'Primary key identifier';
COMMENT ON COLUMN veterinarians.name IS 'Name of the veterinarian (e.g., Dr. Amelia Rivers)';
COMMENT ON COLUMN veterinarians.specialty_code IS 'Specialty code referencing ConsultationReasonType enum (1=General, 2=Ophthalmology, etc.)';
COMMENT ON COLUMN veterinarians.created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN veterinarians.updated_at IS 'Timestamp when the record was last updated';

