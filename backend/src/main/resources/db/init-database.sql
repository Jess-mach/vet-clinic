-- Script para criar o banco de dados e usuário
-- Execute este script como superusuário do PostgreSQL (postgres)

-- Criar o banco de dados
CREATE DATABASE sysCecilia;


-- Conceder privilégios ao usuário
GRANT ALL PRIVILEGES ON DATABASE syscecilia TO jess;

-- Conectar ao banco syscecilia e conceder privilégios no schema public
\c syscecilia
GRANT ALL ON SCHEMA public TO jess;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO jess;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO jess;

