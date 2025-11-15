# Multi-stage build para otimizar a imagem final
FROM node:22-alpine AS builder

WORKDIR /app

# Instalar dependências do sistema necessárias para o build
RUN apk add --no-cache python3 make g++

# Copiar arquivos de dependências
COPY package*.json ./

# Instalar dependências incluindo devDependencies (necessário para build)
# Não usar --no-bin-links em Docker pois não há restrições de symlinks
RUN npm install

# Copiar código-fonte
COPY . .

# Preparar diretório temporário para esbuild
RUN mkdir -p /tmp/esbuild

# Build da aplicação
RUN ESBUILD_BINARY_PATH=/tmp/esbuild npm run build

# Imagem final
FROM node:22-alpine

WORKDIR /app

# Copiar arquivos necessários da etapa anterior
COPY --from=builder /app/dist dist
COPY --from=builder /app/package.json .
COPY --from=builder /app/package-lock.json .

# Instalar apenas dependências de produção
RUN npm install --production --no-bin-links

# Instalar servidor HTTP simples para servir arquivos estáticos
RUN npm install -g serve

# Expor porta
EXPOSE 5173

# Variável de ambiente para API
ENV VITE_API_URL=http://localhost:8080/api

# Comando para iniciar a aplicação
CMD ["serve", "-s", "dist", "-l", "5173"]

