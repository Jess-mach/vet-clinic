## Configuração do Banco de Dados

### Configuração Padrão

- **Host**: localhost (ou `postgres` no Docker)
- **Porta**: 5432
- **Database**: postgres
- **Schema**: postgres
- **Usuário padrão (Docker)**: postgres
- **Senha padrão (Docker)**: postgres

### Instalação do Banco de Dados via Docker
```shell
sudo docker run -d   --name syscecilia   -e POSTGRES_PASSWORD=postgres   -e POSTGRES_USER=postgres   -e POSTGRES_DB=postgres   -p 5432:5432 postgres:15
```

OU

#### Baixar o Instalador
O método mais seguro e padrão é usar o instalador mantido pela EnterpriseDB.

Acesse a página oficial de downloads: [https://www.enterprisedb.com/downloads/postgres-postgresql-downloads](PostgreSQL Downloads for Windows.)

Clique no botão Download correspondente à versão mais recente (atualmente a versão 16 ou 17) na coluna "Windows x86-64".

Se estiver instalando manualmente, execute os seguintes comandos:

### Frontend (React + Vite)

#### 1. Pré-requisitos

# Verificar Node.js
node -version  # Deve ser Node.js 22+

# Verificar npm
npm -version    # Deve ser npm 9+#### 

2. Instalar Dependências
cd vet-clinic

# Instalar dependências (usar --no-bin-links conforme regra do projeto)
npm install --no-bin-links

#### 3. Configurar URL da API
O frontend está configurado para usar `http://localhost:8080/api` por padrão. Se necessário, altere em:

4. Executar em Modo Desenvolvimento
npm run dev

A aplicação estará disponível em: http://localhost:5173

### Backend (Spring Boot)

#### 1. Pré-requisitos

# Verificar Java
java -version  # Deve ser Java 21

# Verificar Maven
mvn -version   # Deve ser Maven 3.9+#### 2. Configurar Banco de Dados

Certifique-se de que o PostgreSQL está rodando veja seção [Configuração do Banco de Dados](#configuração-do-banco-de-dados)).

# Instalar dependências e compilar
mvn clean install

# Ou apenas compilar sem rodar testes
mvn clean package -DskipTests#### 5. Executar a Aplicação

# **1. Executar o Backend**: Usando Maven
mvn spring-boot:run 

# Opção completa - todas as variáveis (Ou passando os parametros caso use um postgreSQL já existente:)
cd backend &&
DB_HOST=localhost \
DB_PORT=5432 \
DB_NAME=syscecilia \
DB_USERNAME=postgres \
DB_PASSWORD=minhasenha \
DB_SCHEMA=syscecilia \
mvn spring-boot:run


**2. Executar o Frontend**

Em um novo terminal, navegue até a pasta do frontend e execute:

```shell
cd vet-clinic
npm run dev
```

## Acessos e Endpoints

### Frontend

- **URL**: http://localhost:5173 (desenvolvimento) ou http://localhost:5173 (Docker)

### Backend

- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/v3/api-docs

### Endpoints Principais

- `GET /api/animals` - Listar animais
- `POST /api/animals` - Criar animal
- `GET /api/animals/{id}` - Buscar animal por ID
- `PUT /api/animals/{id}` - Atualizar animal
- `DELETE /api/animals/{id}` - Deletar animal
- `GET /api/consultations` - Listar consultas
- `POST /api/consultations` - Criar consulta
- `GET /api/veterinarians` - Listar veterinários

Consulte a documentação Swagger para a lista completa.

## Troubleshooting

### Problemas Comuns

#### 1. Backend não conecta ao banco de dados

**Sintoma**: Erro de conexão ao iniciar o backend

**Soluções**:
- Verifique se o PostgreSQL está rodando: `sudo systemctl status postgresql`
- Verifique as credenciais em `application.properties` ou variáveis de ambiente
- No Docker, verifique se o container do postgres está saudável: `docker ps`
- Verifique se o banco `postgres` existe: `psql -U postgres -l`

#### 2. Porta já em uso

**Sintoma**: Erro "Address already in use"

**Soluções**:
- Backend (8080): Altere `server.port` em `application.properties` ou pare o processo usando a porta
- Frontend (5173): Altere a porta no `vite.config.ts` ou pare o processo
- PostgreSQL (5432): Pare o serviço PostgreSQL local ou altere a porta no docker-compose.yml

#### 3. Erro de migração Flyway

**Sintoma**: Erro ao executar migrações do Flyway

**Soluções**:
- Verifique se o schema existe: `CREATE SCHEMA IF NOT EXISTS syscecilia;`
- Limpe a tabela de histórico do Flyway (cuidado!): `DROP TABLE IF EXISTS flyway_schema_history;`
- Verifique os scripts SQL em `backend/src/main/resources/db/migration/`

#### 4. Frontend não conecta ao backend

**Sintoma**: Erro de CORS ou "Network error"

**Soluções**:
- Verifique se o backend está rodando: http://localhost:8080/swagger-ui.html
- Verifique a URL da API em `vet-clinic/src/services/api.ts`
- No Docker, use `http://backend:8080/api` dentro da rede Docker
- Verifique configurações de CORS no backend (se aplicável)

#### 5. Erro ao instalar dependências do frontend

**Sintoma**: Erro no `npm install`

**Soluções**:
- Use `npm install --no-bin-links` conforme regra do projeto
- Limpe o cache: `npm cache clean --force`
- Remova `node_modules` e `package-lock.json` e reinstale
- Verifique a versão do Node.js (deve ser 22+)

#### 6. Container Docker não inicia

**Sintoma**: Container para imediatamente após iniciar

**Soluções**:
- Verifique os logs: `docker-compose logs [serviço]`
- Verifique se as portas estão disponíveis
- Verifique se há erros de build: `docker-compose build --no-cache`
- Verifique as variáveis de ambiente no docker-compose.yml