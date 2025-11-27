# SysCecilia - Frontend

Sistema de gestão para clínicas veterinárias - Interface Web

## 📋 Sobre o Projeto

O frontend do SysCecilia é uma aplicação web moderna desenvolvida em React + TypeScript que fornece uma interface intuitiva e responsiva para gerenciamento completo de clínicas veterinárias. O sistema permite o cadastro e gestão de animais, consultas veterinárias, impressão de receitas e históricos médicos.

## 🚀 Tecnologias Utilizadas

- **React 19.2.0** - Biblioteca JavaScript para construção de interfaces
- **TypeScript 5.9.3** - Superset JavaScript com tipagem estática
- **Vite (Rolldown)** - Build tool ultra-rápida
- **React Router DOM 6.26.0** - Roteamento da aplicação
- **CSS Modules** - Estilização componentizada
- **ESLint** - Linter para qualidade de código

## 📁 Estrutura do Projeto

```
frontend/
├── public/                      # Arquivos públicos e imagens
│   ├── cardiologista.png
│   ├── Clinico-geral.png
│   ├── Neurologista.png
│   ├── oftamologista.png
│   ├── Ortopedista.png
│   └── pet-hero.jpg
├── src/
│   ├── components/              # Componentes React
│   │   ├── animals/            # Componentes de gestão de animais
│   │   │   ├── AnimalList.tsx
│   │   │   ├── AnimalDetails.tsx
│   │   │   ├── AnimalFilters.tsx
│   │   │   ├── AnimalSearchModal.tsx
│   │   │   ├── AnimalConsultationHistoryModal.tsx
│   │   │   ├── AnimalConsultationHistoryPrint.tsx
│   │   │   ├── CreateAnimal.tsx
│   │   │   └── PetsPage.tsx
│   │   ├── consultations/      # Componentes de consultas
│   │   │   ├── ConsultationList.tsx
│   │   │   ├── ConsultationDetails.tsx
│   │   │   ├── ConsultationDetailsPrint.tsx
│   │   │   ├── ConsultationFilters.tsx
│   │   │   ├── ConsultationRecipePrint.tsx
│   │   │   ├── CreateConsultation.tsx
│   │   │   └── ConsultationsPage.tsx
│   │   ├── landing/            # Componentes da landing page
│   │   │   ├── Hero.tsx
│   │   │   ├── Services.tsx
│   │   │   ├── Specialties.tsx
│   │   │   ├── Differentials.tsx
│   │   │   └── Testimonials.tsx
│   │   └── shared/             # Componentes compartilhados
│   │       ├── Header.tsx
│   │       ├── Footer.tsx
│   │       ├── Pagination.tsx
│   │       ├── Modal.tsx
│   │       ├── ConfirmModal.tsx
│   │       └── LoadingSpinner.tsx
│   ├── services/               # Serviços de API
│   │   ├── apiClient.ts       # Cliente HTTP base
│   │   ├── animalApi.ts       # API de animais
│   │   ├── consultationApi.ts # API de consultas
│   │   └── veterinarianApi.ts # API de veterinários
│   ├── types/                  # Definições TypeScript
│   │   ├── animal.ts
│   │   ├── consultation.ts
│   │   └── veterinarian.ts
│   ├── styles/                 # Estilos globais
│   │   ├── variables.css      # Variáveis CSS
│   │   └── buttons.css        # Estilos de botões
│   ├── App.tsx                # Componente principal
│   ├── App.css                # Estilos do App
│   ├── main.tsx               # Entry point
│   └── index.css              # Estilos globais
├── .eslintrc.js               # Configuração ESLint
├── tsconfig.json              # Configuração TypeScript
├── vite.config.ts             # Configuração Vite
├── package.json               # Dependências e scripts
```

## 🎯 Funcionalidades

### 🏠 Landing Page
- Hero com informações da clínica
- Apresentação de serviços
- Especialidades veterinárias
- Diferenciais da clínica
- Depoimentos de clientes

### 🐾 Gestão de Animais
- ✅ Cadastro completo de animais (nome, espécie, raça, gênero, peso, etc.)
- ✅ Listagem paginada com filtros (nome, espécie, tutor)
- ✅ Visualização detalhada dos dados do animal
- ✅ Edição de informações do animal
- ✅ Histórico completo de consultas
- ✅ Impressão de histórico médico
- ✅ Busca rápida por nome ou tutor

### 🏥 Gestão de Consultas
- ✅ Cadastro de consultas veterinárias
- ✅ Vinculação com animal e veterinário
- ✅ Registro de sintomas, diagnóstico e tratamento
- ✅ Prescrição de medicamentos
- ✅ Listagem com filtros avançados
- ✅ Edição de consultas
- ✅ Visualização detalhada
- ✅ Impressão de receita médica
- ✅ Impressão de detalhes da consulta

### 👨‍⚕️ Gestão de Veterinários
- ✅ Cadastro de veterinários
- ✅ Listagem com filtros
- ✅ Vinculação com consultas

## 🔧 Pré-requisitos

- Node.js (versão 18 ou superior)
- npm ou yarn
- Backend do SysCecilia rodando em `http://localhost:8080`

## 📦 Instalação

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd SysCecilia/frontend
```

2. Instale as dependências:
```bash
npm install --no-bin-links
```

> **Nota:** O flag `--no-bin-links` é necessário em ambientes Windows com sistema de arquivos compartilhados.

## ▶️ Como Executar

### Modo Desenvolvimento

```bash
npm run dev
```

A aplicação estará disponível em `http://localhost:5173`

### Build de Produção

```bash
npm run build
```

Os arquivos otimizados serão gerados na pasta `dist/`

### Preview da Build

```bash
npm run preview
```

## 🔗 Integração com Backend

A aplicação se comunica com o backend através da API REST em `http://localhost:8080/api`

### Endpoints Utilizados:

- **Animais:**
  - `GET /animals` - Lista animais com paginação e filtros
  - `GET /animals/{id}` - Busca animal por ID
  - `POST /animals` - Cria novo animal
  - `PUT /animals/{id}` - Atualiza animal
  - `DELETE /animals/{id}` - Remove animal
  - `GET /animals/{id}/consultation-history` - Histórico de consultas

- **Consultas:**
  - `GET /consultations` - Lista consultas com paginação e filtros
  - `GET /consultations/{id}` - Busca consulta por ID
  - `POST /consultations` - Cria nova consulta
  - `PUT /consultations/{id}` - Atualiza consulta
  - `DELETE /consultations/{id}` - Remove consulta

- **Veterinários:**
  - `GET /veterinarians` - Lista veterinários

### Configuração da URL do Backend

Para alterar a URL do backend, edite o arquivo `src/services/apiClient.ts`:

```typescript
const API_BASE_URL = 'http://localhost:8080/api';
```

## 🎨 Padrões de Código

### Componentes
- Cada componente possui seu arquivo `.tsx` e `.css` correspondente
- Utiliza-se TypeScript estrito para tipagem
- Props são sempre tipadas com interfaces

### Nomenclatura
- Componentes: PascalCase (ex: `CreateAnimal.tsx`)
- Arquivos de serviço: camelCase (ex: `animalApi.ts`)
- Estilos: kebab-case para classes CSS

### Estrutura de Componente
```typescript
import React from 'react';
import './ComponentName.css';

interface ComponentProps {
  // props tipadas
}

export const ComponentName: React.FC<ComponentProps> = ({ prop1, prop2 }) => {
  // lógica do componente
  
  return (
    // JSX
  );
};
```

## 📜 Scripts Disponíveis

- `npm run dev` - Inicia servidor de desenvolvimento
- `npm run build` - Gera build de produção
- `npm run preview` - Visualiza build de produção localmente
- `npm run lint` - Executa linter ESLint

## 🎯 Features de Impressão

O sistema possui funcionalidades robustas de impressão:

### Receita Médica
- Formatação profissional
- Dados do animal e veterinário
- Prescrições detalhadas
- Cabeçalho e rodapé personalizados

### Histórico de Consultas
- Timeline de consultas
- Dados completos de cada atendimento
- Formatação para impressão A4

### Detalhes da Consulta
- Informações completas da consulta
- Sintomas, diagnóstico e tratamento
- Prescrições medicamentosas

## 🔒 Tratamento de Erros

O sistema possui tratamento robusto de erros:
- Validações de formulário
- Mensagens de erro amigáveis
- Tratamento de erros de API
- Estados de loading e feedback visual

## 🌐 Responsividade

A interface é totalmente responsiva e otimizada para:
- Desktop (1920px+)
- Laptop (1366px - 1920px)
- Tablet (768px - 1366px)
- Mobile (320px - 768px)

## 🚧 Próximas Funcionalidades

- [ ] Dashboard com estatísticas
- [ ] Sistema de agendamento
- [ ] Upload de imagens (Raio-X, exames)
- [ ] Relatórios financeiros
- [ ] Sistema de notificações
- [ ] Chat em tempo real

## 👥 Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
2. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
3. Push para a branch (`git push origin feature/AmazingFeature`)
4. Abra um Pull Request

## 📄 Licença

Projeto desenvolvido por Jessica Machado para fins acadêmicos - ADS 3
