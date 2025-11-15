# Multi-stage build para otimizar a imagem final
FROM maven:3.9.8-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copiar código-fonte
COPY src src

# Build da aplicação
RUN mvn clean package -DskipTests

# Imagem final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR da etapa anterior
COPY --from=builder /app/target/SysCecilia-0.0.1-SNAPSHOT.jar app.jar

# Expor porta
EXPOSE 8080

# Variáveis de ambiente padrão
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=syscecilia
ENV DB_USERNAME=postgresql
ENV DB_PASSWORD=postgresql

# Entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]

