# SysCecilia - Backend

Sistema de gerenciamento de clínica veterinária desenvolvido com Spring Boot.

## 📋 Pré-requisitos

- Java 21
- Maven 3.6+
- PostgreSQL 12+
- Docker e Docker Compose (opcional)

## 🚀 Tecnologias

- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Flyway** - Controle de versão do banco de dados
- **SpringDoc OpenAPI** - Documentação da API
- **JaCoCo** - Cobertura de código
- **Spring Validation** - Validação de dados

## 🏗️ Arquitetura

O projeto segue o padrão **MVC** com as seguintes camadas:

```
src/main/java/syscecilia/vet/SysCecilia/
├── controller/      # Endpoints da API REST
├── service/         # Regras de negócio
├── repository/      # Acesso a dados
├── model/          # Entidades JPA
├── dto/            # Objetos de transferência de dados
├── specification/  # Filtros dinâmicos
├── exception/      # Tratamento de erros
└── config/         # Configurações da aplicação
```

## 📦 Recursos da API

- **Animais** - Gerenciamento de pets e tutores
- **Consultas** - Registro e histórico de consultas veterinárias
- **Veterinários** - Cadastro e disponibilidade de profissionais

## ⚙️ Configuração

### Variáveis de Ambiente

```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=postgres
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_SCHEMA=postgres
```

### application.properties

As configurações principais estão em:
- `src/main/resources/application.properties` - Desenvolvimento
- `src/main/resources/application-prod.properties` - Produção

## 🔧 Instalação e Execução

### Modo Desenvolvimento

1. Clone o repositório
```bash
git clone <repository-url>
cd SysCecilia/backend
```

2. Configure o banco de dados PostgreSQL

3. Execute a aplicação
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Modo Docker

```bash
# Na raiz do projeto
docker-compose up -d
```

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🧪 Testes

### Executar todos os testes
```bash
./mvnw test
```

### Gerar relatório de cobertura
```bash
./mvnw test jacoco:report
```

O relatório estará disponível em: `target/site/jacoco/index.html`

**Meta de cobertura**: Mínimo de 50% de cobertura de linhas

## 📝 Padrões de Código

- **DTOs**: Sempre utilizar Request/Response objects, nunca expor entidades diretamente
- **Validação**: Usar Spring Validator com mensagens customizadas
- **Documentação**: Todas as APIs documentadas com SpringDoc annotations
- **Testes**: Testes unitários e de integração com MockMVC obrigatórios

## 🗄️ Migrations

As migrations do banco de dados estão em `src/main/resources/db/migration/`

O Flyway executa automaticamente as migrations ao iniciar a aplicação.

## 📦 Build

Para gerar o JAR da aplicação:

```bash
./mvnw clean package
```

O arquivo JAR será gerado em `target/SysCecilia-0.0.1-SNAPSHOT.jar`

## 🐳 Docker

Build da imagem Docker:

```bash
docker build -t syscecilia-backend .
```

## 📄 Licença

Projeto desenvolvido por Jessica Machado para fins acadêmicos - ADS 3

## 👥 Contribuição

1. Sempre criar testes para novos recursos
2. Manter cobertura de código acima de 50%
3. Documentar alterações de API em `.context/`
4. Seguir o padrão MVC estabelecido
