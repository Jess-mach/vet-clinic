-- Migration: Alter consultations table to use veterinarian_id instead of veterinarian_name
-- Date: 2025-11-19
-- Description: Adds veterinarian_id column, migrates data from veterinarian_name to veterinarian_id, then removes veterinarian_name column

-- Step 1: Add veterinarian_id column (nullable initially)
ALTER TABLE consultations 
ADD COLUMN veterinarian_id BIGINT;

-- Step 2: Create index for veterinarian_id
CREATE INDEX idx_consultations_veterinarian_id ON consultations(veterinarian_id);

-- Step 3: Migrate data from veterinarian_name to veterinarian_id
-- Mapping veterinarian names to their IDs based on the initial data
UPDATE consultations 
SET veterinarian_id = CASE 
    WHEN veterinarian_name = 'Dr. Amelia Rivers' THEN 1
    WHEN veterinarian_name = 'Dr. Noah Bennett' THEN 2
    WHEN veterinarian_name = 'Dr. Olivia Carter' THEN 3
    WHEN veterinarian_name = 'Dr. Ethan Walker' THEN 4
    WHEN veterinarian_name = 'Dr. Sophia Hayes' THEN 5
    WHEN veterinarian_name = 'Dr. Lucas Griffin' THEN 6
    WHEN veterinarian_name = 'Dr. Harper Collins' THEN 7
    WHEN veterinarian_name = 'Dr. Mason Clarke' THEN 8
    WHEN veterinarian_name = 'Dr. Isla Morgan' THEN 9
    WHEN veterinarian_name = 'Dr. Leo Harrison' THEN 10
    WHEN veterinarian_name = 'Dr. Aria Mitchell' THEN 11
    WHEN veterinarian_name = 'Dr. Daniel Brooks' THEN 12
    WHEN veterinarian_name = 'Dr. Chloe Parker' THEN 13
    WHEN veterinarian_name = 'Dr. Henry Coleman' THEN 14
    WHEN veterinarian_name = 'Dr. Avery Scott' THEN 15
    ELSE NULL
END;

-- Step 4: For any records that couldn't be matched, set a default (first veterinarian)
-- This handles edge cases where the name doesn't match exactly
UPDATE consultations 
SET veterinarian_id = 1 
WHERE veterinarian_id IS NULL;

-- Step 5: Make veterinarian_id NOT NULL now that all records have values
ALTER TABLE consultations 
ALTER COLUMN veterinarian_id SET NOT NULL;

-- Step 6: Add foreign key constraint
ALTER TABLE consultations 
ADD CONSTRAINT fk_consultations_veterinarians 
FOREIGN KEY (veterinarian_id) REFERENCES veterinarians(id) ON DELETE RESTRICT;

-- Step 7: Remove the old veterinarian_name column
ALTER TABLE consultations 
DROP COLUMN veterinarian_name;

-- Update comments
COMMENT ON COLUMN consultations.veterinarian_id IS 'Foreign key reference to veterinarians table';

