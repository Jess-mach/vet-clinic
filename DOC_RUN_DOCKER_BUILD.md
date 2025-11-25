## Execução via Docker

### Pré-requisitos

1. Certifique-se de que o Docker e Docker Compose estão instalados e rodando
2. Verifique se as portas 5432, 8080 e 5173 estão disponíveis

### Passo a Passo

#### 1. Clone o repositório ou extraia o arquivo zip (se ainda não tiver)



# Construir e iniciar todos os serviços
docker-compose up -d

# Ou para ver os logs em tempo real
docker-compose up#### 4. Verificar o status dos containers

docker-compose psVocê deve ver três containers rodando:
- `postgres_syscecilia` (PostgreSQL)
- `backend_syscecilia` (Spring Boot)
- `frontend_syscecilia` (React)

#### 5. Verificar logs

# Todos os serviços
docker-compose logs -f

# Apenas backend
docker-compose logs -f backend

# Apenas frontend
docker-compose logs -f frontend

# Apenas banco de dados
docker-compose logs -f postgres#### 6. Parar os serviços

# Parar e manter volumes
docker-compose stop

# Parar e remover containers (mantém volumes)
docker-compose down

# Parar e remover containers e volumes
docker-compose down -v### Rebuild das Imagens

Se houver alterações no código, reconstrua as imagens:

# Rebuild forçado
docker-compose build --no-cache

# Rebuild e iniciar
docker-compose up -d --build### Acessos no Docker

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **PostgreSQL**: localhost:5432

---