-- Insert sample animals for testing
INSERT INTO animals (name, species, breed, gender, birth_date, color, weight, microchip_number, owner_name, owner_phone, owner_email, created_at, updated_at)
VALUES 
    (
        'Rex',
        'Dog',
        'Golden Retriever',
        'Male',
        '2020-05-15',
        'Golden',
        32.50,
        'CHIP001987654321',
        'João Silva',
        '(11) 98765-4321',
        'joao.silva@email.com',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Luna',
        'Cat',
        'Persian',
        'Female',
        '2021-08-22',
        'White',
        4.75,
        'CHIP002123456789',
        'Maria Santos',
        '(21) 99876-5432',
        'maria.santos@email.com',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );

