
ALTER TABLE consultations
    ADD COLUMN reason_code INTEGER NOT NULL DEFAULT 1;


ALTER TABLE consultations
    DROP COLUMN reason;


