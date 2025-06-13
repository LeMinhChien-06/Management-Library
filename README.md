# Management Library System

## Description

Developed a secure web-based library management system with comprehensive logging

## System Requirements

- Java 17 or higher
- Maven 3.9.9
- MySQL

## Technologies Used

- **Spring Boot** - Main framework
- **Spring Data JPA** - ORM and database access
- **Spring Security use OAuth 2.0 with JWT** - Authentication and authorization
- **Spring Web** - REST API
- **MySQL** - Database
- **JWT** - Token-based authentication
- **Swagger/OpenAPI** - API documentation

## Libraries Used

- **ZXing (QRCodeWriter, MatrixToImageWriter)** - for generating QQRCode

## Installation and Running

### 1. Clone the project

```bash
git clone https://github.com/LeMinhChien-06/Management-Library.git
cd project-name
```

### 2. Database configuration

Create database and update connection information in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/database_name
    username: your_username
    password: your_password
```

### 3. Install dependencies

```bash
# With Maven
mvn clean install
```

### 4. Run the application

```bash
# With Maven
mvn spring-boot:run

# Or run JAR file
java -jar target/project-name-1.0.0.jar
```

The application will run at: `http://localhost:8080`

## API Documentation

After running the application, you can access API documentation at:

- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v3/api-docs`
