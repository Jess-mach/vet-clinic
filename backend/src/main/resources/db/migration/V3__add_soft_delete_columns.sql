-- Migration: Add soft delete columns to animals table
-- Date: 2025-11-15
-- Description: Adds is_active and inactivated_at columns for soft delete functionality

ALTER TABLE animals 
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN inactivated_at TIMESTAMP NULL;

-- Create indexes for performance
CREATE INDEX idx_animals_is_active ON animals(is_active);
CREATE INDEX idx_animals_inactivated_at ON animals(inactivated_at);

-- Add comments for documentation
COMMENT ON COLUMN animals.is_active IS 'Flag indicating if the animal record is active (true) or inactive/deleted (false)';
COMMENT ON COLUMN animals.inactivated_at IS 'Timestamp when the animal was marked as inactive (soft delete). NULL if still active.';

