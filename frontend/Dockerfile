# Multi-stage build para otimizar a imagem final
FROM node:22-alpine AS builder

WORKDIR /app

# Instalar dependências do sistema necessárias para o build
RUN apk add --no-cache python3 make g++

# Copiar arquivos de dependências
COPY package*.json ./

# Instalar dependências incluindo devDependencies (necessário para build)
RUN npm ci

# Copiar código-fonte
COPY . .

# Preparar diretório temporário para esbuild
RUN mkdir -p /tmp/esbuild

# Build da aplicação
RUN ESBUILD_BINARY_PATH=/tmp/esbuild npm run build

# Imagem final - usando Distroless Node.js (mais leve que Alpine)
FROM gcr.io/distroless/nodejs22-debian12:nonroot

WORKDIR /app

# Copiar arquivos buildados
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/package.json ./

# Instalar serve globalmente no build stage e copiar
FROM node:22-alpine AS server-builder
RUN npm install -g serve
RUN cp $(which serve) /tmp/serve

# Stage final com distroless
FROM gcr.io/distroless/nodejs22-debian12:nonroot
WORKDIR /app
COPY --from=builder /app/dist ./dist
COPY --from=server-builder /tmp/serve ./serve

EXPOSE 5173
ENV VITE_API_URL=http://localhost:8080/api

CMD ["./serve", "-s", "dist", "-l", "5173"]