# Support Ticket API

A simple REST API for creating, viewing, and updating support tickets. The application uses Spring Boot and PostgreSQL, and includes database migrations, validation, API documentation, and automated tests.

## Skills demonstrated

- Java 17 and Spring Boot
- REST API design with Spring MVC
- Layered architecture: controller, service, repository, and DTO layers
- PostgreSQL and Spring Data JPA
- Database versioning with Flyway
- Request validation and global exception handling
- DTO mapping with MapStruct
- Aspect-oriented programming (AOP) for execution-time logging
- OpenAPI documentation with Swagger UI
- Unit testing with JUnit, Mockito, and AssertJ
- Integration testing with MockMvc and Testcontainers
- Docker and Docker Compose

## Main features

- Create a support ticket
- View all tickets
- View one ticket by ID
- Update a ticket's status
- Validate incoming requests
- Return consistent success and error responses

Ticket statuses are `OPEN`, `IN_PROGRESS`, and `CLOSED`.

## Tech stack

| Tool | Purpose |
| --- | --- |
| Java 17 | Programming language |
| Spring Boot | Application framework |
| PostgreSQL | Database |
| Flyway | Database migrations |
| MapStruct | DTO and entity mapping |
| Swagger UI | Interactive API documentation |
| JUnit, Mockito, Testcontainers | Automated testing |
| Docker Compose | Local PostgreSQL setup |

## Run the project

### Prerequisites

Install the following tools:

- Java 17 or newer
- Docker Desktop or Docker Engine with Docker Compose

You do not need to install Maven because the project includes the Maven Wrapper.

### 1. Clone and open the project

```bash
git clone https://github.com/M-AminGharibi/Support_Ticket_API.git
cd "Support Ticket"
```


### 2. Start the application

Make sure Docker is running. Spring Boot will use `compose.yaml` to start PostgreSQL automatically.

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

Wait until the console shows that the application has started.

### 3. Open the API documentation

Open Swagger UI in your browser:

```text
http://localhost:8080/swagger-ui.html
```

You can view and test every endpoint from this page.

### 4. Stop the application

Press `Ctrl+C` in the terminal. To remove the PostgreSQL container, run:

```bash
docker compose down
```

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/tickets` | Create a ticket |
| `GET` | `/tickets` | Get all tickets |
| `GET` | `/tickets/{id}` | Get a ticket by ID |
| `PUT` | `/tickets/{id}/status` | Update a ticket's status |


## Run the tests

Keep Docker running so the integration tests can create a temporary PostgreSQL container.

On Windows:

```powershell
.\mvnw.cmd test
```

On macOS or Linux:

```bash
./mvnw test
```

Test reports are generated in `target/surefire-reports`.
