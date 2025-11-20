-- Migration: Insert sample veterinarians
-- Date: 2025-11-19
-- Description: Inserts 15 veterinarians with different specialties for initial data

-- Specialty codes:
-- 1 = Consulta com clinico geral (General Checkup)
-- 2 = Consulta com oftalmologista (Ophthalmology)
-- 3 = Consulta com cardiologista (Cardiology)
-- 4 = Consulta com ortopedista (Orthopedics)
-- 5 = Consulta com neurologista (Neurology)
-- 6 = Exames (Exams)
-- 7 = Exame de imagem (Imaging Exams)
-- 8 = Vacinação (Vaccination)
-- 9 = Cirurgia (Surgery)
-- 10 = Retorno (Follow-up)
-- 11 = Emergência (Emergency)

INSERT INTO veterinarians (name, specialty_code, created_at, updated_at) VALUES
('Dr. Amelia Rivers', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),      -- Ophthalmology
('Dr. Noah Bennett', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),       -- General Checkup
('Dr. Olivia Carter', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),      -- Cardiology
('Dr. Ethan Walker', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),       -- General Checkup
('Dr. Sophia Hayes', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),       -- Orthopedics
('Dr. Lucas Griffin', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),      -- Neurology
('Dr. Harper Collins', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),     -- General Checkup
('Dr. Mason Clarke', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),       -- Surgery
('Dr. Isla Morgan', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),        -- Ophthalmology
('Dr. Leo Harrison', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),       -- General Checkup
('Dr. Aria Mitchell', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),      -- Cardiology
('Dr. Daniel Brooks', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),      -- Neurology
('Dr. Chloe Parker', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),       -- Orthopedics
('Dr. Henry Coleman', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),      -- General Checkup
('Dr. Avery Scott', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);        -- Exams

-- Comments explaining the distribution:
-- 5 General practitioners (most common)
-- 2 Ophthalmologists
-- 2 Cardiologists  
-- 2 Orthopedists
-- 2 Neurologists
-- 1 Exam specialist
-- 1 Surgeon

