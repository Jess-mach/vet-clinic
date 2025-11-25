# SysCecilia - Sistema de Clínica Veterinária

Sistema web para gerenciamento de clínica veterinária, desenvolvido com Spring Boot e React.

## 🚀 Tecnologias

### Backend
- Java 21
- Spring Boot 3.5.7
- PostgreSQL 16
- Flyway (Migrações)
- SpringDoc OpenAPI (Documentação API)
- JaCoCo (Cobertura de Testes)

### Frontend
- React 19.2.0
- TypeScript
- Vite
- React Router DOM

## 📋 Pré-requisitos

- Java 21
- Node.js (versão 18+)
- Docker e Docker Compose
- Maven

## 🏃 Como Rodar

### Opção 1: Com Docker (Recomendado)

```bash
# Subir todos os serviços
docker-compose up -d

# Acessar a aplicação
# Frontend: http://localhost:5173
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Opção 2: Desenvolvimento Local

**1. Subir o banco de dados:**
```bash
docker run -d \
  --name syscecilia \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=postgres \
  -p 5432:5432 \
  postgres:15
```

**2. Rodar o backend:**
```bash
cd backend
mvn spring-boot:run
```

**3. Rodar o frontend:**
```bash
cd frontend
npm install --no-bin-links
npm run dev
```

## 🔗 URLs

- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:8080
- **Documentação API (Swagger):** http://localhost:8080/swagger-ui.html

## 🧪 Testes

```bash
cd backend
mvn test

# Relatório de cobertura em: target/site/jacoco/index.html
```

## 📁 Estrutura do Projeto

```
SysCecilia/
├── backend/          # API Spring Boot
├── frontend/       # Frontend React
├── docker-compose.yml
└── README.md
```

## 🛠️ Comandos Úteis

### Docker
```bash
# Parar containers
docker-compose down

# Ver logs
docker-compose logs -f

# Reconstruir imagens
docker-compose up --build
```

### Backend
```bash
# Limpar e compilar
mvn clean install

# Matar processo na porta 8080
lsof -ti:8080 | xargs kill -9
```

## 👥 Desenvolvido por

Jéssica Machado 

Projeto acadêmico - ADS 3
 

