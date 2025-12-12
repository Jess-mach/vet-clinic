# 🐳 Guia Docker - SysCecilia

## Início Rápido

### 1. Subir toda a aplicação

#### Install docker-compose (Ubuntu)
# Remover versão antiga
sudo apt-get remove docker-compose

# Baixar versão mais recente
sudo curl -SL https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose

# Dar permissão de execução
sudo chmod +x /usr/local/bin/docker-compose

# Verificar
docker-compose --version

```bash
docker-compose up -d --build
```

Isso vai:
- ✅ Criar e iniciar o PostgreSQL na porta 5432
- ✅ Fazer build e iniciar o Backend Spring Boot na porta 8080
- ✅ Fazer build e iniciar o Frontend React na porta 5173

### 2. Verificar status

```bash
docker-compose ps
```

**Status esperado:**
```
NAME                   STATUS              PORTS
syscecilia-postgres    Up (healthy)        5432->5432
syscecilia-backend     Up (healthy)        8080->8080
syscecilia-frontend    Up (healthy)        5173->5173
```

### 3. Acessar a aplicação

- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:8080/api
- **Swagger UI:** http://localhost:8080/swagger-ui.html

## Comandos Úteis

### Logs

```bash
# Todos os serviços
docker-compose logs -f

# Apenas backend
docker-compose logs -f backend

# Apenas frontend
docker-compose logs -f frontend

# Apenas banco de dados
docker-compose logs -f postgres
```

### Parar e iniciar

```bash
# Parar todos os serviços
docker-compose stop

# Iniciar serviços parados
docker-compose start

# Parar e remover containers (dados do banco são mantidos)
docker-compose down

# Parar e remover containers E volumes (APAGA DADOS DO BANCO!)
docker-compose down -v
```

### Rebuild

```bash
# Rebuild de todos os serviços
docker-compose up --build -d

# Rebuild apenas do backend
docker-compose up --build -d backend

# Rebuild apenas do frontend
docker-compose up --build -d frontend

# Rebuild sem cache (mais lento, mas garante limpeza completa)
docker-compose build --no-cache
docker-compose up -d
```

### Executar comandos nos containers

```bash
# Acessar shell do backend
docker-compose exec backend sh

# Acessar PostgreSQL
docker-compose exec postgres psql -U postgres

# Ver tabelas no banco
docker-compose exec postgres psql -U postgres -c "\dt"

# Ver estrutura do schema
docker-compose exec postgres psql -U postgres -c "\d animals"
```

## Tempos de Inicialização

### Primeira execução (build)
- **PostgreSQL:** ~10-20 segundos
- **Backend:** ~2-4 minutos (download de dependências Maven + build + migrações)
- **Frontend:** ~1-3 minutos (download de dependências npm + build)

### Execuções subsequentes
- **PostgreSQL:** ~5 segundos
- **Backend:** ~30-60 segundos (apenas inicialização Spring Boot)
- **Frontend:** ~5-10 segundos (serve já buildado)

## Troubleshooting

### Backend não inicia

**Problema:** Backend fica em estado "unhealthy" ou reiniciando

**Solução:**
```bash
# Verificar logs
docker-compose logs backend

# Causas comuns:
# 1. Banco ainda não está pronto -> aguardar 30s
# 2. Erro nas migrações -> verificar logs do Flyway
# 3. Porta 8080 já em uso -> parar processo ou mudar porta
```

### Frontend não carrega

**Problema:** Página não abre ou mostra erro de conexão

**Solução:**
```bash
# Verificar se backend está saudável primeiro
docker-compose ps backend

# Verificar logs do frontend
docker-compose logs frontend

# Rebuild do frontend
docker-compose up --build -d frontend
```

### Erro de conexão com banco

**Problema:** Backend não conecta ao PostgreSQL

**Solução:**
```bash
# Verificar se PostgreSQL está saudável
docker-compose ps postgres

# Resetar banco (APAGA DADOS!)
docker-compose down -v
docker-compose up -d postgres

# Aguardar até ficar healthy
docker-compose ps postgres

# Subir backend
docker-compose up -d backend
```

### Porta já em uso

**Problema:** `Error: port is already allocated`

**Solução:**
```bash
# Verificar processos nas portas
lsof -ti:8080  # Backend
lsof -ti:5173  # Frontend
lsof -ti:5432  # PostgreSQL

# Opção 1: Matar o processo
lsof -ti:8080 | xargs kill -9

# Opção 2: Mudar porta no docker-compose.yml
# Exemplo: "8081:8080" usa porta 8081 no host
```

### Build muito lento

**Problema:** Build demora muito tempo

**Solução:**
```bash
# Limpar cache do Docker
docker system prune -a

# Aumentar recursos do Docker
# Docker Desktop -> Settings -> Resources
# - CPUs: 4+
# - Memory: 4GB+
```

### Container reiniciando constantemente

**Problema:** Container fica reiniciando

**Solução:**
```bash
# Ver logs completos
docker-compose logs --tail=100 backend

# Desabilitar restart automático temporariamente
# Editar docker-compose.yml:
# restart: "no"  # ao invés de unless-stopped

# Subir novamente
docker-compose up -d
```

## Estrutura dos Arquivos

```
SysCecilia/
├── docker-compose.yml       # Orquestração de todos os serviços
├── backend/
│   ├── Dockerfile          # Build do backend Spring Boot
│   └── .dockerignore       # Arquivos ignorados no build
└── frontend/
    ├── Dockerfile          # Build do frontend React
    └── .dockerignore       # Arquivos ignorados no build
```

## Variáveis de Ambiente

### Backend

Configuradas no `docker-compose.yml`:

```yaml
DB_HOST: postgres           # Host do banco (nome do serviço)
DB_PORT: 5432              # Porta do PostgreSQL
DB_NAME: postgres          # Nome do banco
DB_USERNAME: postgres      # Usuário do banco
DB_PASSWORD: postgres      # Senha do banco
DB_SCHEMA: postgres        # Schema do banco
```

### Frontend

```yaml
VITE_API_URL: http://localhost:8080/api  # URL do backend
```

**Nota:** Para produção, mude para a URL real do backend.

## Volumes

### Volume persistente do PostgreSQL

```bash
# Listar volumes
docker volume ls

# Inspecionar volume
docker volume inspect syscecilia_postgres_data

# Backup do banco
docker-compose exec postgres pg_dump -U postgres postgres > backup.sql

# Restaurar backup
docker-compose exec -T postgres psql -U postgres postgres < backup.sql
```

## Rede

Os containers se comunicam através da rede `syscecilia-network`:

- **Backend** acessa PostgreSQL via `postgres:5432`
- **Frontend** (do navegador) acessa Backend via `localhost:8080`

## Limpeza Completa

```bash
# Parar e remover tudo (APAGA TODOS OS DADOS!)
docker-compose down -v

# Remover imagens criadas
docker rmi syscecilia-backend syscecilia-frontend

# Limpar cache do Docker
docker system prune -a --volumes
```

## Produção

Para produção, ajuste:

1. **Senhas fortes** no PostgreSQL
2. **HTTPS** com certificado SSL
3. **Variáveis de ambiente** via arquivo `.env`
4. **Profile prod** do Spring: `SPRING_PROFILES_ACTIVE=prod`
5. **Recursos** adequados (CPU/RAM)
6. **Backup** automático do banco
7. **Logs** centralizados
8. **Monitoring** com Prometheus/Grafana

## Suporte

Em caso de problemas:

1. Verificar logs: `docker-compose logs -f`
2. Verificar status: `docker-compose ps`
3. Consultar README.md do projeto
4. Verificar portas disponíveis: `lsof -ti:PORTA`

