# AGENTS.md — Guia de Desenvolvimento e Regras de Arquitetura

Este documento estabelece a estrutura, convenções de código, diretrizes de segurança e padrões operacionais para agentes de IA e desenvolvedores que atuam no repositório **sistema-reservas-api**.

---

## 1. Visão Geral do Projeto

O **sistema-reservas-api** é o back-end (REST API) de um ecossistema de gestão e agendamento de salas físicas equipadas com fechaduras eletrônicas inteligentes (IoT).
- **Domínio Principal**: Cadastro e reserva de salas, geração e provisionamento automatizado de senhas temporárias (PINs de 6 dígitos) diretamente em fechaduras inteligentes através da API da **Tuya IoT**.
- **Contexto**: Trabalho de Fim de Curso (TFC).

---

## 2. Stack Tecnológica

| Componente | Tecnologia / Versão |
| :--- | :--- |
| **Linguagem** | Java 21 (LTS) |
| **Framework Base** | Spring Boot 3.5.x |
| **Segurança** | Spring Security 6 (Stateless JWT via `com.auth0:java-jwt`) |
| **Persistência** | Spring Data JPA / Hibernate |
| **Banco de Dados (Produção)** | PostgreSQL (variáveis `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) |
| **Banco de Dados (Testes)** | H2 Database em memória (`MODE=PostgreSQL`) |
| **Utilitários & Produtividade** | Project Lombok (com annotation processor configurado no Maven) |
| **Validação** | Jakarta Validation (`spring-boot-starter-validation`) |
| **Testes** | JUnit 5, Mockito, Spring Boot Test, Spring Security Test, MockMvc |
| **Build & Dependências** | Apache Maven com Maven Wrapper (`./mvnw`) |

---

## 3. Estrutura de Pacotes e Diretórios

O código-fonte segue a estrutura padrão Maven sob o pacote base `com.radieske.reservasapi`:

```text
src/main/java/com/radieske/reservasapi/
├── config/              # Configurações Spring (SecurityConfig, AsyncConfig)
├── controller/          # Endpoints REST (apenas roteamento, validação e DTOs)
├── dto/                 # Data Transfer Objects (Records e DTOs de request/response)
├── enums/               # Enums de domínio (TipoUsuario, Status)
├── exception/           # Tratamento global de exceções (GlobalExceptionHandler)
├── integration/         # Adaptadores e clientes de provedores IoT
│   └── tuya/            # Implementação do protocolo Tuya Cloud API (AuthTuya, CriptoTuya, Tuya)
├── model/               # Entidades JPA mapeadas para o banco de dados
├── repository/          # Interfaces Spring Data JPA
├── security/            # Provedor JWT, filtros de autenticação e entry points
├── service/             # Interfaces de serviços de negócio
│   └── impl/            # Implementações dos serviços de negócio
└── util/                # Utilitários (criptografia, gerador seguro de senhas)

src/main/resources/
└── application.properties # Configurações da aplicação (PostgreSQL, JWT)

src/test/
├── java/com/radieske/   # Suítes de testes automatizados (unitários, integração e segurança)
└── resources/
    └── application.properties # Configuração isolada para testes (H2 em memória)
```

---

## 4. Regras e Padrões de Arquitetura

Ao estender ou modificar este projeto, siga rigorosamente as seguintes convenções:

### 4.1. Camada de Serviço Obrigatória (Separation of Concerns)
- **Controllers NUNCA devem conter lógica de negócio**: Controllers devem apenas receber DTOs anotados com `@Valid`, chamar a camada de serviço correspondente e retornar `ResponseEntity<?>`.
- **Padrão Interface + Implementação**: Todo serviço deve possuir sua interface declarada em `service.<Nome>Service` e sua implementação correspondente em `service.impl.<Nome>ServiceImpl`.
- **Injeção de Dependências**: Injete dependências via `@Autowired` ou construtor.

### 4.2. DTOs e Proteção de Dados Sensíveis
- Sempre prefira o uso de Java Records ou DTOs imutáveis para transferência de dados.
- **Nunca exponha hashes ou senhas**: A entidade `Usuario` possui o campo `senhaHash` configurado como `@JsonProperty(access = Access.WRITE_ONLY)`. Nunca remova essa anotação e nunca serialize credenciais para respostas HTTP.

### 4.3. Tratamento Centralizado de Exceções
- Exceções conhecidas e validações de payload devem ser tratadas em [`GlobalExceptionHandler`](file:///src/main/java/com/radieske/reservasapi/exception/GlobalExceptionHandler.java).
- Respostas de erro devem sempre seguir o formato JSON estruturado:
  ```json
  {
    "error": "Descrição clara da falha",
    "status": 400
  }
  ```

---

## 5. Diretrizes de Segurança (Spring Security & JWT)

1. **Anti-Enumeração de Usuários (CWE-204)**:
   - Em operações de login, **nunca diferencie** usuário inexistente de senha incorreta.
   - Ambas as falhas devem lançar `BadCredentialsException` e retornar **HTTP 401** com a mensagem padronizada: `"Usuário ou senha inválidos"`.
   - Toda a verificação de credenciais deve ser orquestrada pelo `AuthenticationManager.authenticate(...)`.

2. **Arquitetura Stateless**:
   - A API não armazena sessões de usuário no servidor. O `SecurityConfig` deve manter sempre `SessionCreationPolicy.STATELESS`.
   - Acesso não autenticado a rotas protegidas deve retornar HTTP 401 através de [`CustomAuthenticationEntryPoint`](file:///src/main/java/com/radieske/reservasapi/security/CustomAuthenticationEntryPoint.java).

3. **Filtro de Autenticação (`UserAuthenticationFilter`)**:
   - O filtro extrai e valida tokens JWT via cabeçalho `Authorization: Bearer <token>`.
   - **Nunca lance exceções em filtros de servlet**: Se o token estiver ausente ou corrompido, limpe o `SecurityContextHolder` e apenas execute `filterChain.doFilter(request, response)` para que o `SecurityFilterChain` e o CORS tratem a requisição sem derrubar o container.
   - Requisições CORS Preflight (`OPTIONS`) devem ser permitidas sem autenticação.

4. **Controle de Acesso Baseado em Perfis (RBAC)**:
   - Perfis disponíveis em `TipoUsuario`: `comum`, `admin`.
   - Authorities registradas: `ROLE_comum`, `ROLE_admin`.
   - Endpoints administrativos devem ser protegidos com `@PreAuthorize("hasRole('admin')")`.

5. **Geração de Credenciais e Criptografia**:
   - Senhas temporárias para fechaduras físicas geradas em [`PasswordGenerator`](file:///src/main/java/com/radieske/reservasapi/util/PasswordGenerator.java) **devem** utilizar `java.security.SecureRandom`.
   - Senhas de usuários devem ser sempre armazenadas como hash BCrypt via `PasswordEncoder`.

---

## 6. Integração IoT (Tuya Cloud)

- O fluxo de criação de reserva (`ReservaServiceImpl`) dispara automaticamente a criação de uma `SenhaTemporaria`.
- A classe [`ProcessIntegration`](file:///src/main/java/com/radieske/reservasapi/integration/ProcessIntegration.java) orquestra chamadas assíncronas para provisionar o PIN numérico na fechadura física via API Tuya.
- Utiliza criptografia simétrica `AES/ECB/PKCS5Padding` para decodificar o ticket da fechadura e criptografar o PIN temporário antes da transmissão externa.

---

## 7. Instruções para Execução e Testes

### 7.1. Comandos Maven Principais
Execute sempre através do Maven Wrapper (`./mvnw` no PowerShell/Bash ou `mvnw.cmd` no CMD):

- **Compilar o projeto**:
  ```bash
  ./mvnw test-compile
  ```
- **Executar todos os testes**:
  ```bash
  ./mvnw test
  ```
- **Executar um teste específico**:
  ```bash
  ./mvnw test -Dtest=AuthControllerTest
  ```
- **Executar a aplicação em desenvolvimento**:
  ```bash
  ./mvnw spring-boot:run
  ```

### 7.2. Padrão para Novos Testes
- **Ambiente de Testes Autossuficiente**: O arquivo `src/test/resources/application.properties` configura o banco de dados H2 em memória. Testes automatizados **nunca** devem depender de instâncias externas de banco de dados ou da Tuya Cloud ativas.
- **Camada Web / Controller**: Utilize `@SpringBootTest` + `@AutoConfigureMockMvc` e `@MockitoBean` para testar controllers HTTP, contratos JSON, validações `@Valid` e códigos de status.
- **Camada de Serviço**: Utilize testes de unidade puros com `@ExtendWith(MockitoExtension.class)`, `@Mock` e `@InjectMocks` para validar regras de negócio isoladas.
- **Validação de Sucesso**: Antes de considerar qualquer modificação concluída, certifique-se de que `./mvnw test` execute com sucesso total (`BUILD SUCCESS`, 0 failures, 0 errors).
