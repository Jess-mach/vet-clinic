
---

## Configuração do Banco de Dados

### Configuração Padrão

- **Host**: localhost (ou `postgres` no Docker)
- **Porta**: 5432
- **Database**: syscecilia
- **Schema**: postgresql
- **Usuário padrão (Docker)**: postgresql
- **Senha padrão (Docker)**: postgresql

### Criando o Banco de Dados Manualmente
```shell
sudo docker run -d   --name postgres   -e POSTGRES_PASSWORD=postgresql   -e POSTGRES_USER=postgresql   -e POSTGRES_DB=postgresql   -p 5432:5432  
```

OU

Baixar o Instalador
O método mais seguro e padrão é usar o instalador mantido pela EnterpriseDB.

Acesse a página oficial de downloads: [(https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)](PostgreSQL Downloads for Windows.)

Clique no botão Download correspondente à versão mais recente (atualmente a versão 16 ou 17) na coluna "Windows x86-64".

Se estiver instalando manualmente, execute os seguintes comandos:

# Criar o banco de dados
CREATE DATABASE syscecilia;

# Criar o schema (opcional, o Flyway pode criar)
\c syscecilia
CREATE SCHEMA IF NOT EXISTS syscecilia;


## Execução via Docker

### Pré-requisitos

1. Certifique-se de que o Docker e Docker Compose estão instalados e rodando
2. Verifique se as portas 5432, 8080 e 5173 estão disponíveis

### Passo a Passo

#### 1. Clone o repositório (se ainda não tiver)

cd /caminho/para/SysCecilia#### 2. Configure variáveis de ambiente (opcional)

Crie um arquivo `.env` na raiz do projeto para personalizar as configurações:

# .env
DB_USERNAME=postgresql
DB_PASSWORD=postgresql
DB_NAME=syscecilia
DB_SCHEMA=syscecilia#### 3. Execute o Docker Compose

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

## Instalação e Execução Manual

### Backend (Spring Boot)

#### 1. Pré-requisitos

# Verificar Java
java -version  # Deve ser Java 21

# Verificar Maven
mvn -version   # Deve ser Maven 3.9+#### 2. Configurar Banco de Dados

Certifique-se de que o PostgreSQL está rodando e o banco `syscecilia` foi criado (veja seção [Configuração do Banco de Dados](#configuração-do-banco-de-dados)).

#### 3. Configurar Variáveis de Ambiente

Crie um arquivo `.env` na pasta `backend/` (opcional, mas recomendado):
h
# backend/.env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=syscecilia
DB_USERNAME=jess
DB_PASSWORD=morangos
DB_SCHEMA=sysceciliaOu configure as variáveis de ambiente do sistema:

export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=syscecilia
export DB_USERNAME=jess
export DB_PASSWORD=morangos
export DB_SCHEMA=syscecilia#### 4. Instalar Dependências e Compilar

cd backend

# Instalar dependências e compilar
mvn clean install

# Ou apenas compilar sem rodar testes
mvn clean package -DskipTests#### 5. Executar a Aplicação

# Opção 1: Usando Maven
mvn spring-boot:run

# Opção 2: Executar o JAR gerado
java -jar target/SysCecilia-0.0.1-SNAPSHOT.jar

# Opção 3: Usando o wrapper Maven (se disponível)
./mvnw spring-boot:run#### 6. Verificar se está rodando

Acesse: http://localhost:8080/swagger-ui.html

### Frontend (React + Vite)

#### 1. Pré-requisitos

# Verificar Node.js
node -version  # Deve ser Node.js 22+

# Verificar npm
npm -version    # Deve ser npm 9+#### 2. Instalar Dependências
h
cd vet-clinic

# Instalar dependências (usar --no-bin-links conforme regra do projeto)
npm install --no-bin-links#### 3. Configurar URL da API

O frontend está configurado para usar `http://localhost:8080/api` por padrão. Se necessário, altere em:

- `vet-clinic/src/services/api.ts` (linha 5)

Ou configure via variável de ambiente:

export VITE_API_URL=http://localhost:8080/api#### 4. Executar em Modo Desenvolvimento
sh
npm run devA aplicação estará disponível em: http://localhost:5173

#### 5. Build para Produção

npm run buildOs arquivos compilados estarão em `vet-clinic/dist/`

#### 6. Preview da Build de Produção

npm run preview### Executar Backend e Frontend Simultaneamente

Use o script fornecido:

# Na raiz do projeto
chmod +x start.sh
./start.shEste script:
- Inicia o frontend em background
- Inicia o backend em background
- Exibe logs em tempo real
- Para ambos ao pressionar Ctrl+C

Os logs são salvos em `~/.syscecilia-logs/`

---

## Configurações e Variáveis de Ambiente

### Backend

#### application.properties (Desenvolvimento)

Localização: `backend/src/main/resources/application.properties`

Principais configurações:
- `spring.datasource.url`: URL de conexão JDBC
- `spring.datasource.username`: Usuário do banco
- `spring.datasource.password`: Senha do banco
- `server.port`: Porta do servidor (padrão: 8080)

#### application-docker.properties (Docker)

Localização: `backend/src/main/resources/application-docker.properties`

Usado automaticamente quando `SPRING_PROFILES_ACTIVE=docker`

### Frontend

#### Variáveis de Ambiente

- `VITE_API_URL`: URL base da API (padrão: `http://localhost:8080/api`)

Configure via arquivo `.env` na pasta `vet-clinic/`:

VITE_API_URL=http://localhost:8080/api### Docker Compose

Variáveis de ambiente no `docker-compose.yml`:

- `DB_USERNAME`: Usuário do PostgreSQL
- `DB_PASSWORD`: Senha do PostgreSQL
- `DB_NAME`: Nome do banco de dados
- `DB_SCHEMA`: Schema do banco de dados
- `SPRING_PROFILES_ACTIVE`: Profile Spring (docker)

---

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

### Banco de Dados

- **Host**: localhost (ou `postgres` no Docker)
- **Porta**: 5432
- **Database**: syscecilia
- **Schema**: syscecilia

---

## Troubleshooting

### Problemas Comuns

#### 1. Backend não conecta ao banco de dados

**Sintoma**: Erro de conexão ao iniciar o backend

**Soluções**:
- Verifique se o PostgreSQL está rodando: `sudo systemctl status postgresql`
- Verifique as credenciais em `application.properties` ou variáveis de ambiente
- No Docker, verifique se o container do postgres está saudável: `docker-compose ps`
- Verifique se o banco `syscecilia` existe: `psql -U postgres -l`

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

#### 7. Erro de permissão no script start.sh

**Sintoma**: "Permission denied" ao executar `./start.sh`

**Soluções**:
chmod +x start.sh
./start.sh### Verificações Úteis

# Verificar processos Java rodando
jps -l

# Verificar processos Node rodando
ps aux | grep node

# Verificar portas em uso
netstat -tulpn | grep -E ':(8080|5173|5432)'

# Verificar containers Docker
docker ps -a

# Verificar logs do Docker
docker-compose logs --tail=100 [serviço]---

## Comandos Úteis

### Docker
h
# Iniciar serviços
docker-compose up -d

# Parar serviços
docker-compose stop

# Remover containers
docker-compose down

# Rebuild forçado
docker-compose build --no-cache

# Ver logs
docker-compose logs -f

# Executar comando em container
docker-compose exec backend bash
docker-compose exec postgres psql -U postgresql -d syscecilia### Backend

# Compilar
mvn clean package

# Executar testes
mvn test

# Executar com cobertura
mvn clean test jacoco:report

# Ver relatório de cobertura
open backend/target/site/jacoco/index.html### Frontend

# Instalar dependências
npm install --no-bin-links

# Desenvolvimento
npm run dev

# Build produção
npm run build

# Preview build
npm run preview

# Lint
npm run lint---

## Suporte

Para mais informações, consulte:
- Documentação do Spring Boot: https://spring.io/projects/spring-boot
- Documentação do React: https://react.dev
- Documentação do Vite: https://vitejs.dev
- Documentação do PostgreSQL: https://www.postgresql.org/docs/

---

**Última atualização**: Dezembro 2024