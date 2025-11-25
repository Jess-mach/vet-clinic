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

# Imagem final - usando Distroless (muito mais leve e seguro)
FROM gcr.io/distroless/java21-debian12:nonroot

WORKDIR /app

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

# Nota: Distroless não tem shell, então healthcheck precisa ser feito externamente
# O healthcheck será gerenciado pelo docker-compose

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]