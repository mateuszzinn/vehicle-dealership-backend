Backend em Spring Boot para gerenciar concessionárias e seus veículos, com API REST para cadastro, consulta, atualização e remoção de dealers e vehicles.

## Finalidade do projeto

O projeto centraliza a gestão de concessionárias e veículos, incluindo:

- CRUD de concessionárias (`/dealers`)
- CRUD de veículos (`/vehicles`)
- Consulta de veículos por concessionária (`/vehicles/dealer/{dealerId}`)
- Integração com ViaCEP para dados de endereço

Também oferece documentação OpenAPI em:

- Swagger UI: `http://localhost:8080/docs`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Stack tecnológica

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.1.0 | Framework principal |
| Spring Data JPA | - | Persistência de dados |
| Spring Web MVC | - | API REST |
| Flyway | - | Migrações de banco |
| PostgreSQL | 18 | Banco de dados |
| MapStruct | 1.6.3 | Mapeamento DTO/Entity |
| Lombok | - | Redução de boilerplate |
| OpenFeign | 5.0.2 | Cliente HTTP (ViaCEP) |
| SpringDoc OpenAPI | 2.8.13 | Documentação da API |
| JUnit 5 + Mockito | - | Testes |

## Arquitetura de pastas

```
src/main/java/com/example/vehicledealershipbackend/
├── config/                 # Configurações da aplicação
│   ├── CorsConfig.java     # Configuração CORS
│   └── OpenApiConfig.java  # Configuração Swagger/OpenAPI
├── controller/             # Camada de apresentação (REST Controllers)
│   ├── doc/                # Interfaces para documentação OpenAPI
│   │   ├── DealerControllerDoc.java
│   │   └── VehicleControllerDoc.java
│   ├── DealerController.java
│   └── VehicleController.java
├── dto/                    # Data Transfer Objects
│   ├── dealer/
│   │   ├── DealerRequest.java
│   │   ├── DealerResponse.java
│   │   └── DealerUpdateRequest.java
│   ├── vehicle/
│   │   ├── VehicleRequest.java
│   │   └── VehicleResponse.java
│   └── viacep/
│       └── ViaCepResponse.java
├── entity/                 # Entidades JPA
│   ├── enums/
│   │   └── FuelType.java
│   ├── Dealer.java
│   └── Vehicle.java
├── exception/              # Tratamento global de exceções
│   ├── CustomGlobalExceptionHandler.java
│   ├── ErrorMessage.java
│   ├── ExternalServiceException.java
│   ├── ResourceConflictException.java
│   └── ResourceNotFoundException.java
├── mapper/                 # Mapeadores MapStruct
│   ├── DealerMapper.java
│   └── VehicleMapper.java
├── repository/             # Repositórios Spring Data JPA
│   ├── DealerRepository.java
│   └── VehicleRepository.java
├── service/                # Regras de negócio
│   ├── AddressService.java
│   ├── DealerService.java
│   └── VehicleService.java
├── client/                 # Clientes externos (Feign)
│   └── ViaCepClient.java
└── VehicleDealershipBackendApplication.java
```

### Princípios de organização

- **Controller**: Apenas recebe requisições, valida entrada (Bean Validation) e delega para Service
- **Service**: Contém toda a lógica de negócio, transações e orquestração
- **Repository**: Interface para acesso a dados (Spring Data JPA)
- **Entity**: Mapeamento direto das tabelas do banco
- **DTO**: Contratos de entrada/saída da API (desacoplados da entidade)
- **Mapper**: Conversão Entity ↔ DTO via MapStruct
- **Exception**: Exceções de domínio + handler global (`@RestControllerAdvice`)
- **Client**: Integração com APIs externas (ViaCEP via Feign)

## Banco de dados e migrações

- **Banco**: PostgreSQL 18
- **Migrações**: Flyway (localização: `src/main/resources/db/migration/`)
- **Estratégia**: `ddl-auto: validate` — o Hibernate valida o schema contra as migrações aplicadas

### Migrações existentes

| Arquivo | Descrição |
|---------|-----------|
| `V1__create_initial_schema.sql` | Criação das tabelas `dealer`, `vehicle` e `address` |
| `V2__alter_vehicle_price_to_double.sql` | Altera tipo do preço do veículo para `DOUBLE PRECISION` |

## Inicialização do projeto

### Opção 1: Docker Compose (recomendado)

```powershell
docker compose up --build
```

Se as portas já estiverem em uso, sobrescreva via variáveis de ambiente:

```powershell
$env:APP_PORT="8081"
$env:POSTGRES_PORT="5433"
docker compose up --build
```

**Serviços expostos:**

| Serviço | URL |
|---------|-----|
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/docs` |
| PostgreSQL | `localhost:5432` |

**Credenciais padrão (definidas no `docker-compose.yml`):**

- Database: `vehicle_dealership`
- Usuário: `dealership`
- Senha: `dealership`

---

### Opção 2: Execução local com Maven Wrapper

**Pré-requisitos:**

- Java 21 instalado
- PostgreSQL rodando (local ou remoto)

**Configure variáveis de ambiente (opcional — já possuem valores padrão no `application.yaml`):**

```powershell
$env:URL_POSTGRES="jdbc:postgresql://localhost:5432/vehicle_dealership"
$env:USERNAME_POSTGRES="dealership"
$env:PASSWORD_POSTGRES="dealership"
```

**Inicie a aplicação:**

```powershell
.\mvnw.cmd spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## Testes

```powershell
# Executar todos os testes
.\mvnw.cmd test

# Executar testes com cobertura (se configurado)
.\mvnw.cmd verify
```

Estrutura de testes espelha a de `src/main`:

```
src/test/java/com/example/vehicledealershipbackend/
├── service/
│   ├── AddressServiceTest.java
│   ├── DealerServiceTest.java
│   └── VehicleServiceTest.java
└── VehicleDealershipBackendApplicationTests.java
```

## Endpoints principais

### Dealers (`/dealers`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/dealers` | Lista todas concessionárias |
| GET | `/dealers/{id}` | Busca por ID |
| POST | `/dealers` | Cria nova concessionária |
| PUT | `/dealers/{id}` | Atualiza concessionária |
| DELETE | `/dealers/{id}` | Remove concessionária |

### Vehicles (`/vehicles`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/vehicles` | Lista todos veículos |
| GET | `/vehicles/{id}` | Busca por ID |
| GET | `/vehicles/dealer/{dealerId}` | Lista veículos de uma concessionária |
| POST | `/vehicles` | Cria novo veículo |
| PUT | `/vehicles/{id}` | Atualiza veículo |
| DELETE | `/vehicles/{id}` | Remove veículo |

## Variáveis de ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `URL_POSTGRES` | `jdbc:postgresql://localhost:5432/vehicle-dealership` | JDBC URL do PostgreSQL |
| `USERNAME_POSTGRES` | `postgres` | Usuário do banco |
| `PASSWORD_POSTGRES` | `postgres` | Senha do banco |
| `APP_PORT` | `8080` | Porta da aplicação (Docker) |
| `POSTGRES_PORT` | `5432` | Porta do PostgreSQL (Docker) |

## Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/docs
- **OpenAPI Spec (JSON)**: http://localhost:8080/api-docs

## Licença

Este projeto está sob a licença definida no arquivo `LICENSE`.