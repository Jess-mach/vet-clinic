-- Migration: Insert additional scheduled and cancelled consultations
-- Date: 2025-11-25
-- Description: Adds 100 SCHEDULED consultations (future dates) and
--              70 CANCELLED consultations (historical range) to enrich test data.

-- ==========================================================
-- 100 consultations with status = 'SCHEDULED'
--   - consultation_date > 2025-12-02
--   - distributed across 120 animals and 15 veterinarians
-- ==========================================================

WITH scheduled_base AS (
    SELECT
        gs AS seq,
        ((gs - 1) % 120) + 1 AS animal_id,
        ((gs - 1) % 15) + 1 AS veterinarian_id,
        -- Start after 2025-12-02 and spread forward
        TIMESTAMP '2025-12-03 09:00:00' + (gs - 1) * INTERVAL '12 hours' AS consultation_date,
        CASE
            WHEN (gs % 5) IN (1, 2, 3) THEN 1   -- GENERAL_CHECKUP (id 1)
            WHEN (gs % 5) = 4 THEN 8            -- VACCINATION (id 8)
            ELSE 6                              -- EXAMS (id 6)
        END AS reason_code
    FROM generate_series(1, 100) AS gs
),

-- ==========================================================
-- 70 consultations with status = 'CANCELLED'
--   - consultation_date between 2025-01-01 and 2026-06-30
--   - spread starting from 2025-01-01
-- ==========================================================

cancelled_base AS (
    SELECT
        gs AS seq,
        ((gs - 1) % 120) + 1 AS animal_id,
        ((gs - 1) % 15) + 1 AS veterinarian_id,
        TIMESTAMP '2025-01-01 09:00:00' + (gs - 1) * INTERVAL '3 days' AS consultation_date,
        CASE
            WHEN (gs % 4) IN (1, 2) THEN 1   -- GENERAL_CHECKUP
            WHEN (gs % 4) = 3 THEN 8         -- VACCINATION
            ELSE 6                           -- EXAMS
        END AS reason_code
    FROM generate_series(1, 70) AS gs
),

all_new AS (
    SELECT 'SCHEDULED'::VARCHAR(20) AS status, sb.*
    FROM scheduled_base sb
    UNION ALL
    SELECT 'CANCELLED'::VARCHAR(20) AS status, cb.*
    FROM cancelled_base cb
)

INSERT INTO consultations (
    animal_id,
    veterinarian_id,
    consultation_date,
    reason_code,
    description,
    diagnosis,
    treatment_prescribed,
    observations,
    next_appointment_date,
    status,
    created_at,
    updated_at
)
SELECT
    a.animal_id,
    a.veterinarian_id,
    a.consultation_date,
    a.reason_code,
    CASE a.reason_code
        WHEN 1 THEN 'Consulta de rotina / check-up geral (agendada pela recepção).'
        WHEN 6 THEN 'Consulta para realização de exames laboratoriais (agendada).'
        WHEN 8 THEN 'Consulta para vacinação e revisão clínica rápida (agendada).'
        ELSE 'Consulta clínica geral (agendada).'
    END AS description,
    CASE a.status
        WHEN 'CANCELLED' THEN 'Consulta cancelada pelo tutor ou pela clínica antes da realização.'
        ELSE 'Consulta agendada ainda não realizada; diagnóstico pendente.'
    END AS diagnosis,
    CASE a.reason_code
        WHEN 1 THEN 'Planejado uso de suplemento VetMulti Pet 500 UI se necessário após avaliação.'
        WHEN 6 THEN 'Orientado jejum e preparo com SoluVet Hidra para exames, se indicados.'
        WHEN 8 THEN 'Programada administração de ImunoPet V10 1 dose SC na data da consulta.'
        ELSE 'Serão definidos medicamentos após avaliação clínica presencial.'
    END AS treatment_prescribed,
    CASE a.status
        WHEN 'CANCELLED' THEN 'Agendamento cancelado; manter registro apenas para histórico administrativo.'
        ELSE 'Agendamento criado para composição de agenda futura na base de testes.'
    END AS observations,
    CASE
        WHEN a.status = 'SCHEDULED' THEN a.consultation_date + INTERVAL '30 minutes'
        ELSE NULL
    END AS next_appointment_date,
    a.status,
    a.consultation_date AS created_at,
    a.consultation_date AS updated_at
FROM all_new a;

-- Realign consultations sequence with current max ID after inserts
SELECT setval(
    'consultations_id_seq',
    COALESCE((SELECT MAX(id) FROM consultations), 0),
    TRUE
);


