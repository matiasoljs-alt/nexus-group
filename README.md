# Grupo Nexus - Sistema de Gestão Interna de Funcionários

Sistema web corporativo completo para gestão interna de colaboradores, benefícios, escalas de trabalho e comunicados institucionais da empresa **Grupo Nexus**, inspirado em sistemas empresariais reais de médio porte.

---

## 🏛️ Visão Geral do Sistema

O sistema disponibiliza um portal interno unificado com **3 níveis de controle de acesso (RBAC)**:

1. **Funcionário (`ROLE_FUNCIONARIO`)**:
   - Consulta seus dados profissionais e cadastrais no **Meu Perfil**.
   - Visualiza sua escala de trabalho atribuída, horários de entrada/saída, intervalo e folgas.
   - Consulta seus benefícios ativos e subsídios concedidos.
   - Acessa o mural de comunicados e avisos urgentes da empresa.
   - Dashboard com resumo personalizado e métricas da empresa.

2. **Recursos Humanos (`ROLE_RH`)**:
   - **Gestão completa de Funcionários**: listagem com pesquisa em tempo real, filtros dinâmicos por setor e status, cadastro de novos colaboradores com criação automática de usuário de acesso, edição e inativação/ativação.
   - **Gestão de Escalas**: criação e parametrização de turnos de trabalho.
   - **Gestão de Benefícios**: catálogo corporativo com valores, descrições e regras.
   - **Gestão de Comunicados**: publicação e destaque de comunicados para todos os funcionários.
   - **Estrutura Organizacional**: gerenciamento de Cargos e Departamentos (Setores).

3. **Administrador (`ROLE_ADMIN`)**:
   - Acesso irrestrito a todos os módulos do sistema.
   - **Gestão de Usuários e Permissões**: alteração de roles (`ADMIN`, `RH`, `FUNCIONARIO`), bloqueio/desbloqueio de contas e redefinição de senhas.

> **Regra do Projeto**: Seguindo as diretrizes corporativas, **não são utilizadas fotos ou avatares de funcionários**; em vez disso, o sistema adota insígnias de monograma com iniciais dinâmicas, chips de status de alta fidelidade e dossiês profissionais limpos.

---

## 💻 Tecnologias Empregadas

### Backend (Java)
- **Java 17 / 21 / 25**
- **Spring Boot 3.3.4**
- **Spring Web** (Construção de APIs REST)
- **Spring Data JPA & Hibernate** (Mapeamento Objeto-Relacional e persistência)
- **Spring Security 6** (Autenticação Stateless e Autorização por Roles)
- **JWT (JSON Web Token - JJWT 0.12.6)** para troca segura de tokens nas requisições
- **BCrypt** (Criptografia forte de senhas)
- **Bean Validation** (`@Valid`, `@NotNull`, `@NotBlank`, `@Email`)
- **Maven** (Gerenciamento de dependências e build automatizado)
- **MySQL 8.0** (Banco de dados relacional oficial) + perfil alternativo **H2 Database**

### Frontend (Moderno, Responsivo & Mobile-First)
- **Single Page Application (SPA)** integrada nativamente na pasta `src/main/resources/static`.
- **HTML5 Semântico**, **CSS3 Moderno** com variáveis corporativas inspiradas em arquitetura e alta tecnologia (paleta azul profundo, azul cobalto, ardósia e branco limpo).
- **JavaScript Moderno (ES6+)** estruturado em módulos (`api.js`, `auth.js`, `app.js` e views dedicadas).
- **Mobile-First**:
  - Desktop: Sidebar lateral corporativa, cards de KPI em 4 colunas, tabelas completas.
  - Celular e Tablet: Menu hambúrguer deslizante com backdrop fosco, cards reorganizados em coluna única, tabelas responsivas e botões táteis (alvos de toque >= 44px) sem scroll horizontal indesejado.

---

## 🏢 Arquitetura do Backend (Boas Práticas de OO)

O código-fonte segue estritamente a arquitetura em camadas exigida:

```text
src/main/java/com/gruponexus/gestao/
├── config/              # Configurações de CORS, inicialização e carga de dados de teste
│   ├── CorsConfig.java
│   └── DataInitializer.java
├── controller/          # Controladores REST expondo as rotas protegidas
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── FuncionarioController.java
│   ├── CargoController.java
│   ├── SetorController.java
│   ├── EscalaController.java
│   ├── BeneficioController.java
│   ├── ComunicadoController.java
│   └── UsuarioController.java
├── dto/                 # Objetos de Transferência de Dados desacoplados das entidades
│   ├── request/         # LoginRequest, FuncionarioRequest, CargoRequest, etc.
│   └── response/        # JwtResponse, FuncionarioResponse, DashboardResponse, etc.
├── entity/              # Entidades JPA com relacionamentos e validações
│   ├── Role.java
│   ├── Usuario.java
│   ├── Funcionario.java
│   ├── Cargo.java
│   ├── Setor.java
│   ├── Escala.java
│   ├── Beneficio.java
│   └── Comunicado.java
├── exception/           # Tratamento global de erros e exceções customizadas
│   ├── BusinessException.java
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/         # Interfaces Spring Data JPA com queries customizadas
│   ├── UsuarioRepository.java
│   ├── FuncionarioRepository.java
│   ├── CargoRepository.java
│   ├── SetorRepository.java
│   ├── EscalaRepository.java
│   ├── BeneficioRepository.java
│   └── ComunicadoRepository.java
├── security/           # Infraestrutura de segurança JWT e filtros
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── CustomUserDetails.java
│   ├── CustomUserDetailsService.java
│   └── SecurityConfig.java
├── service/            # Regras de negócio, validações e orquestração
│   ├── AuthService.java
│   ├── FuncionarioService.java
│   ├── CargoService.java
│   ├── SetorService.java
│   ├── EscalaService.java
│   ├── BeneficioService.java
│   ├── ComunicadoService.java
│   ├── DashboardService.java
│   └── UsuarioService.java
└── GrupoNexusApplication.java # Classe principal Spring Boot
```

---

## 🗄️ Modelagem do Banco de Dados (MySQL)

As entidades relacionam-se através de JPA/Hibernate sem duplicações:

- **`usuarios`**: credenciais criptografadas (`senha` com BCrypt), e-mail, permissão (`ROLE_ADMIN`, `ROLE_RH`, `ROLE_FUNCIONARIO`) e status da conta.
- **`funcionarios`**: dados cadastrais (matrícula, nome, CPF, e-mail, telefone, admissão, status, contrato), com relacionamento `1:1` para `usuarios`, `N:1` para `cargos`, `N:1` para `setores`, `N:1` para `escalas` e `N:N` para `beneficios`.
- **`setores`**: departamentos corporativos (TI, RH, Engenharia & Obras, Operações, Comercial, Financeiro).
- **`cargos`**: posições e níveis (Júnior, Pleno, Sênior, etc.), com salário base e chave estrangeira para o setor correspondente.
- **`escalas`**: jornada de trabalho (tipo, horários de entrada/saída, intervalo, dias de trabalho e folgas).
- **`beneficios`**: catálogo de benefícios e tabela associativa intermediária `funcionario_beneficios`.
- **`comunicados`**: mural corporativo de comunicados com título, conteúdo, autor, data e sinalizador de urgência/destaque.

---

## 🔑 Credenciais de Acesso para Demonstração

O sistema já é inicializado com dados realistas do Grupo Nexus e botões de **preenchimento com 1 clique** na tela de login:

| Perfil | Usuário / Matrícula | Senha | Descrição |
|---|---|---|---|
| **Administrador** | `admin` | `admin123` | Acesso completo a todos os módulos e permissões |
| **Recursos Humanos** | `rh` | `rh123` | Matrícula NEX-1001 (Beatriz Silveira - RH) |
| **Funcionário** | `NEX-1002` | `user123` | Matrícula NEX-1002 (Lucas Ferreira - Dev TI) |
| **Funcionário** | `NEX-1003` | `user123` | Matrícula NEX-1003 (Mariana Albuquerque - Eng. Civil) |
| **Funcionário** | `NEX-1004` | `user123` | Matrícula NEX-1004 (Rafael Vasconcelos - Operações) |

---

## 🚀 Como Executar o Projeto

### Opção 1: Execução com 1 Clique (Windows)
Basta dar um duplo clique no arquivo:
```cmd
run.bat
```
*(Ele detecta automaticamente o JDK e o Maven e inicializa o servidor).*

### Opção 2: Pelo Terminal / Prompt de Comando
Na pasta do projeto, execute:
```cmd
.\mvnw.cmd spring-boot:run
```

### Opção 3: Abrir na IDE (IntelliJ IDEA ou VS Code)
1. Abra a pasta `GRUPO NEXUS` como projeto Maven.
2. Aguarde a sincronização das dependências.
3. Execute o método `main` da classe `com.gruponexus.gestao.GrupoNexusApplication`.

### Acessando a Aplicação
Abra o navegador em:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 📡 Principais Endpoints da API REST

### Autenticação & Perfil
- `POST /api/auth/login`: Autentica usuário/matrícula e gera token JWT.
- `POST /api/auth/recuperar-senha`: Solicitação de recuperação de credenciais.
- `POST /api/auth/alterar-senha`: Troca de senha autenticada com confirmação.
- `GET /api/auth/me`: Dados do usuário logado e perfil associado.

### Dashboard
- `GET /api/dashboard`: Métricas agregadas (totais, distribuição setorial, comunicados e escalas).

### Funcionários (`/api/funcionarios`)
- `GET /api/funcionarios`: Listagem com filtros por `busca`, `setorId`, `cargoId` e `status`.
- `GET /api/funcionarios/{id}`: Dossiê completo do colaborador.
- `GET /api/funcionarios/me`: Dados do colaborador atualmente autenticado.
- `POST /api/funcionarios`: Cadastra funcionário e conta de acesso (RH/Admin).
- `PUT /api/funcionarios/{id}`: Atualiza dados cadastrais (RH/Admin).
- `PATCH /api/funcionarios/{id}/status`: Alterna status Ativo/Inativo (RH/Admin).
- `DELETE /api/funcionarios/{id}`: Exclusão com cascateamento seguro (RH/Admin).

### Estrutura Organizacional & Gestão
- `GET /api/setores` / `POST /api/setores` / `PUT /api/setores/{id}` / `DELETE /api/setores/{id}`
- `GET /api/cargos` / `POST /api/cargos` / `PUT /api/cargos/{id}` / `DELETE /api/cargos/{id}`
- `GET /api/escalas` / `POST /api/escalas` / `PUT /api/escalas/{id}` / `DELETE /api/escalas/{id}`
- `GET /api/beneficios` / `POST /api/beneficios` / `PUT /api/beneficios/{id}` / `DELETE /api/beneficios/{id}`
- `GET /api/comunicados` / `POST /api/comunicados` / `PUT /api/comunicados/{id}` / `DELETE /api/comunicados/{id}`
- `GET /api/usuarios` / `PUT /api/usuarios/{id}` (Apenas Admin)

---

Desenvolvido para atender aos mais altos padrões acadêmicos e empresariais para apresentação técnica.
