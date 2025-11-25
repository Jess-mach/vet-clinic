
CREATE TABLE animals (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(100),
    gender VARCHAR(20) NOT NULL,
    birth_date DATE,
    color VARCHAR(50),
    weight DECIMAL(5,2),
    microchip_number VARCHAR(50) UNIQUE,
    owner_name VARCHAR(100) NOT NULL,
    owner_phone VARCHAR(20),
    owner_email VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_animals_name ON animals(name);
CREATE INDEX idx_animals_species ON animals(species);
CREATE INDEX idx_animals_owner_name ON animals(owner_name);
CREATE INDEX idx_animals_microchip_number ON animals(microchip_number);

COMMENT ON TABLE animals IS 'Table to store information about animals registered in the veterinary clinic';
COMMENT ON COLUMN animals.id IS 'Primary key identifier';
COMMENT ON COLUMN animals.name IS 'Name of the animal';
COMMENT ON COLUMN animals.species IS 'Species of the animal (e.g., Dog, Cat, Bird)';
COMMENT ON COLUMN animals.breed IS 'Breed of the animal';
COMMENT ON COLUMN animals.gender IS 'Gender of the animal (Male, Female, Neutered, Spayed)';
COMMENT ON COLUMN animals.birth_date IS 'Birth date of the animal';
COMMENT ON COLUMN animals.color IS 'Color of the animal';
COMMENT ON COLUMN animals.weight IS 'Weight of the animal in kilograms';
COMMENT ON COLUMN animals.microchip_number IS 'Microchip identification number';
COMMENT ON COLUMN animals.owner_name IS 'Name of the animal owner';
COMMENT ON COLUMN animals.owner_phone IS 'Phone number of the owner';
COMMENT ON COLUMN animals.owner_email IS 'Email address of the owner';
COMMENT ON COLUMN animals.created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN animals.updated_at IS 'Timestamp when the record was last updated';


