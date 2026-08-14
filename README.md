# vehicle-dealership-backend

Backend em Spring Boot para gerenciar concessionarias e seus veiculos, com API REST para cadastro, consulta, atualizacao e remocao de dealers e vehicles.

## Finalidade do projeto

O projeto centraliza a gestao de concessionarias e veiculos, incluindo:

- CRUD de concessionarias (`/dealer`)
- CRUD de veiculos (`/vehicles`)
- Consulta de veiculos por concessionaria (`/vehicles/dealer/{dealerId}`)
- Integracao com ViaCEP para dados de endereco

Tambem oferece documentacao OpenAPI em:

- Swagger UI: `http://localhost:8080/docs`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Banco de dados e migracoes

- Banco: PostgreSQL 18
- Migracoes: Flyway
- Primeira migracao: `src/main/resources/db/migration/V1__create_initial_schema.sql`

O Hibernate esta configurado com `ddl-auto: validate`, entao o schema e validado com base nas migracoes aplicadas.

## Inicializacao do projeto

### Opcao 1: Docker Compose (recomendado)

```powershell
docker compose up --build
```

Se as portas ja estiverem em uso, voce pode sobrescrever:

```powershell
$env:APP_PORT="8081"
$env:POSTGRES_PORT="5433"
docker compose up --build
```

Servicos expostos:

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

Credenciais padrao:

- Database: `vehicle_dealership`
- Usuario: `dealership`
- Senha: `dealership`

### Opcao 2: Execucao local com Maven Wrapper

Pre-requisitos:

- Java 21
- PostgreSQL em execucao

Configure variaveis de ambiente (opcional, ja possuem valor padrao):

```powershell
$env:URL_POSTGRES="jdbc:postgresql://localhost:5432/vehicle_dealership"
$env:USERNAME_POSTGRES="dealership"
$env:PASSWORD_POSTGRES="dealership"
```

Inicie a aplicacao:

```powershell
.\mvnw.cmd spring-boot:run
```
