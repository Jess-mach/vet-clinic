# Multi-stage build para otimizar a imagem final
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .

# Baixar dependências (camada cacheável)
RUN mvn dependency:go-offline -B || true

# Copiar código-fonte
COPY src ./src

# Build da aplicação (pula testes para build mais rápido)
RUN mvn clean package -DskipTests -B

# Imagem final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expor porta
EXPOSE 8080

# Variáveis de ambiente padrão
ENV DB_HOST=postgres \
    DB_PORT=5432 \
    DB_NAME=postgres \
    DB_USERNAME=postgres \
    DB_PASSWORD=postgres \
    DB_SCHEMA=postgres

# Health check (verifica se a aplicação está respondendo)
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/swagger-ui.html || exit 1

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

