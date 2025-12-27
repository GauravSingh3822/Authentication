# 🔐 Spring Boot Authentication & Authorization Service

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?style=for-the-badge&logo=spring)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.2-green?style=for-the-badge&logo=springsecurity)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![JWT](https://img.shields.io/badge/JWT-Auth-red?style=for-the-badge&logo=jsonwebtokens)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A production-ready, enterprise-grade authentication and authorization microservice built with Spring Boot**

[Features](#-features) • [Quick Start](#-quick-start) • [API Documentation](#-api-documentation) • [Architecture](#-architecture) • [Contributing](#-contributing)

</div>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Configuration](#%EF%B8%8F-configuration)
- [Database Schema](#-database-schema)
- [Docker Deployment](#-docker-deployment)
- [Testing](#-testing)
- [Security](#-security)
- [Contributing](#-contributing)
- [License](#-license)
- [Support](#-support)

---

## 🎯 Overview

This is a **production-ready Authentication and Authorization Service** built using **Spring Boot 3** and **Spring Security 6**. It provides secure, stateless JWT-based authentication with role-based access control (RBAC), designed for microservices architectures and scalable applications.

### Why This Project?

- 🔒 **Enterprise-Grade Security** - Implements industry best practices
- 🚀 **Production Ready** - Complete with error handling, validation, and logging
- 📦 **Microservice Ready** - Stateless design perfect for distributed systems
- 🎨 **Clean Architecture** - Well-structured, maintainable codebase
- 📚 **Well Documented** - Comprehensive API documentation and examples

---

## ✨ Features

### Core Features
- ✅ **User Registration & Authentication**
- ✅ **JWT Access & Refresh Tokens**
- ✅ **Role-Based Access Control (RBAC)**
- ✅ **BCrypt Password Encryption**
- ✅ **Stateless Session Management**
- ✅ **Token Refresh Mechanism**
- ✅ **Account Activation & Email Verification** *(optional)*

### Technical Features
- 🛡️ **Spring Security 6 Integration**
- 🗄️ **PostgreSQL Database with JPA/Hibernate**
- 📊 **RESTful API Design**
- 🔍 **Global Exception Handling**
- ✅ **Request Validation**
- 📝 **Comprehensive Logging**
- 🧪 **Unit & Integration Tests**
- 🐳 **Docker Support**

---

## 🛠 Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2.x |
| **Security** | Spring Security 6.2.x |
| **Database** | PostgreSQL 15+ |
| **ORM** | Spring Data JPA (Hibernate) |
| **Authentication** | JWT (JSON Web Tokens) |
| **Build Tool** | Maven 3.9+ |
| **Testing** | JUnit 5, Mockito, Spring Test |
| **Containerization** | Docker & Docker Compose |

---

## 🏗 Architecture

### Project Structure

```
authentication-service/
│
├── src/main/java/com/auth/
│   ├── controller/                    # REST Controllers
│   │   ├── AuthController.java        # Authentication endpoints
│   │   └── UserController.java        # User management endpoints
│   │
│   ├── service/                       # Business Logic Layer
│   │   ├── AuthService.java           # Authentication logic
│   │   ├── UserService.java           # User operations
│   │   └── JwtService.java            # JWT token operations
│   │
│   ├── repository/                    # Data Access Layer
│   │   ├── UserRepository.java        # User database operations
│   │   └── RefreshTokenRepository.java
│   │
│   ├── entity/                        # JPA Entities
│   │   ├── User.java                  # User entity
│   │   ├── Role.java                  # Role entity
│   │   └── RefreshToken.java          # Refresh token entity
│   │
│   ├── dto/                           # Data Transfer Objects
│   │   ├── request/
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── RefreshTokenRequest.java
│   │   └── response/
│   │       ├── AuthResponse.java
│   │       └── UserResponse.java
│   │
│   ├── security/                      # Security Configuration
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── SecurityConfig.java
│   │   └── CustomUserDetailsService.java
│   │
│   ├── exception/                     # Exception Handling
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── UnauthorizedException.java
│   │   └── InvalidTokenException.java
│   │
│   ├── config/                        # Application Configuration
│   │   ├── AppConfig.java
│   │   └── CorsConfig.java
│   │
│   └── util/                          # Utility Classes
│       └── Constants.java
│
├── src/main/resources/
│   ├── application.yml                # Main configuration
│   ├── application-dev.yml            # Development config
│   └── application-prod.yml           # Production config
│
├── src/test/                          # Test Cases
│   └── java/com/auth/
│       ├── service/
│       ├── controller/
│       └── integration/
│
├── docker/
│   ├── Dockerfile
│   └── docker-compose.yml
│
├── pom.xml                            # Maven dependencies
└── README.md
```

### Authentication Flow

```
┌──────────┐          ┌──────────┐          ┌──────────┐          ┌──────────┐
│          │          │          │          │          │          │          │
│  Client  │          │   API    │          │ Security │          │ Database │
│          │          │          │          │          │          │          │
└────┬─────┘          └────┬─────┘          └────┬─────┘          └────┬─────┘
     │                     │                     │                     │
     │ 1. POST /register   │                     │                     │
     │────────────────────>│                     │                     │
     │                     │ 2. Hash Password    │                     │
     │                     │────────────────────>│                     │
     │                     │                     │ 3. Save User        │
     │                     │                     │────────────────────>│
     │                     │                     │                     │
     │                     │ 4. User Created     │                     │
     │<────────────────────│<────────────────────│<────────────────────│
     │                     │                     │                     │
     │ 5. POST /login      │                     │                     │
     │────────────────────>│                     │                     │
     │                     │ 6. Authenticate     │                     │
     │                     │────────────────────>│                     │
     │                     │                     │ 7. Verify User      │
     │                     │                     │────────────────────>│
     │                     │                     │                     │
     │                     │ 8. Generate JWT     │                     │
     │                     │<────────────────────│                     │
     │                     │                     │                     │
     │ 9. Return Tokens    │                     │                     │
     │<────────────────────│                     │                     │
     │                     │                     │                     │
     │ 10. GET /api/users  │                     │                     │
     │ (with JWT Token)    │                     │                     │
     │────────────────────>│                     │                     │
     │                     │ 11. Validate Token  │                     │
     │                     │────────────────────>│                     │
     │                     │                     │ 12. Check Roles     │
     │                     │                     │────────────────────>│
     │                     │                     │                     │
     │                     │ 13. Access Granted  │                     │
     │ 14. Return Data     │<────────────────────│                     │
     │<────────────────────│                     │                     │
     │                     │                     │                     │
```

---

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- ☕ **Java 17** or higher - [Download](https://adoptium.net/)
- 📦 **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- 🐘 **PostgreSQL 15+** - [Download](https://www.postgresql.org/download/)
- 🐙 **Git** - [Download](https://git-scm.com/downloads)
- 🐳 **Docker** (Optional) - [Download](https://www.docker.com/products/docker-desktop)

### Installation

#### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/spring-boot-auth-service.git
cd spring-boot-auth-service
```

#### 2️⃣ Setup PostgreSQL Database

```bash
# Using psql
psql -U postgres

# Create database
CREATE DATABASE auth_db;

# Exit psql
\q
```

Or using command line:

```bash
createdb -U postgres auth_db
```

#### 3️⃣ Configure Application Properties

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_db
    username: postgres
    password: your_password
    
jwt:
  secret: YourSecretKeyHereMustBe256BitsLong!ChangeThis!
  access-token-expiration: 3600000
  refresh-token-expiration: 86400000
```

> ⚠️ **Important**: Generate a secure JWT secret key for production!

#### 4️⃣ Build the Project

```bash
mvn clean install
```

#### 5️⃣ Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/authentication-service-1.0.0.jar
```

#### 6️⃣ Verify Installation

The application will start on `http://localhost:8080`

Test with:
```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

---

## 📡 API Documentation

### Base URL
```
http://localhost:8082/api
```

### 🔓 Public Endpoints

#### Register New User

```http
POST /api/auth/register
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "role": "USER"
}
```

**Response (201 Created):**
```json
{
  "message": "User registered successfully",
  "userId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

#### Login

```http
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

#### Refresh Token

```http
POST /api/auth/refresh
```

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

### 🔐 Protected Endpoints

> **Note**: All protected endpoints require `Authorization: Bearer <token>` header

#### Get Current User

```http
GET /api/users/me
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "USER",
  "enabled": true,
  "createdAt": "2025-12-27T10:30:00Z"
}
```

---

#### Get All Users (ADMIN Only)

```http
GET /api/users
Authorization: Bearer <admin_token>
```

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "role": "USER",
    "enabled": true,
    "createdAt": "2025-12-27T10:30:00Z"
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "role": "ADMIN",
    "enabled": true,
    "createdAt": "2025-12-26T09:15:00Z"
  }
]
```

---

#### Update User

```http
PUT /api/users/{id}
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com"
}
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Updated",
  "email": "john.updated@example.com",
  "role": "USER",
  "enabled": true,
  "createdAt": "2025-12-27T10:30:00Z"
}
```

---

#### Delete User (ADMIN Only)

```http
DELETE /api/users/{id}
Authorization: Bearer <admin_token>
```

**Response (200 OK):**
```json
{
  "message": "User deleted successfully"
}
```

---

### cURL Examples

**Register:**
```bash
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "role": "USER"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

**Access Protected Resource:**
```bash
curl -X GET http://localhost:8082/api/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---
## 🌐 API Endpoints

### 🔐 Authentication APIs

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Authenticate & get JWT | Public |
| POST | `/api/auth/refresh` | Refresh access token | Public |
| POST | `/api/auth/logout` | Invalidate refresh token | Authenticated |

### 🔒 User Management APIs

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/users` | Get all users | ADMIN |
| GET | `/api/users/{id}` | Get user by ID | ADMIN / USER (own) |
| PUT | `/api/users/{id}` | Update user | ADMIN / USER (own) |
| DELETE | `/api/users/{id}` | Delete user | ADMIN |
| GET | `/api/users/me` | Get current user | Authenticated |

---

## 📝 Request/Response Examples

### 1️⃣ Register User

**Request:**
```bash
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "role": "USER"
  }'
```

**Response:**
```json
{
  "message": "User registered successfully",
  "userId": "123e4567-e89b-12d3-a456-426614174000"
}
```

### 2️⃣ Login

**Request:**
```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

### 3️⃣ Access Protected Resource

**Request:**
```bash
curl -X GET http://localhost:8082/api/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER",
  "createdAt": "2025-01-15T10:30:00Z"
}
```

## ⚙️ Configuration

### Application Properties

**Main Configuration (`application.yml`):**

```yaml
spring:
  application:
    name: authentication-service
  
  # Database Configuration
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
  
  # JPA/Hibernate Configuration
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        jdbc:
          batch_size: 20
    
# JWT Configuration
jwt:
  secret: ${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
  access-token-expiration: 3600000      # 1 hour
  refresh-token-expiration: 86400000    # 24 hours

# Server Configuration
server:
  port: 8080
  servlet:
    context-path: /
  error:
    include-message: always
    include-binding-errors: always
    include-stacktrace: on_param

# Logging Configuration
logging:
  level:
    root: INFO
    com.auth: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/application.log
```

### Environment Variables

Create a `.env` file for sensitive data:

```env
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=auth_db
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your-256-bit-secret-key-here-change-in-production
JWT_ACCESS_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=86400000

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev

# Logging
LOG_LEVEL=INFO
```

Load environment variables:

```bash
# Linux/Mac
export $(cat .env | xargs)

# Windows (PowerShell)
Get-Content .env | ForEach-Object {
  $name, $value = $_.split('=')
  Set-Content env:\$name $value
}
```

---

## 💾 Database Schema

### Entity Relationship Diagram

```
┌─────────────────────────────┐
│         USERS               │
├─────────────────────────────┤
│ id (UUID) PK                │
│ name (VARCHAR)              │
│ email (VARCHAR) UNIQUE      │
│ password (VARCHAR)          │
│ role (VARCHAR)              │
│ enabled (BOOLEAN)           │
│ created_at (TIMESTAMP)      │
│ updated_at (TIMESTAMP)      │
└──────────┬──────────────────┘
           │
           │ 1:N
           │
┌──────────▼──────────────────┐
│    REFRESH_TOKENS           │
├─────────────────────────────┤
│ id (UUID) PK                │
│ token (VARCHAR)             │
│ user_id (UUID) FK           │
│ expiry_date (TIMESTAMP)     │
│ created_at (TIMESTAMP)      │
└─────────────────────────────┘
```

### SQL Schema

```sql
-- Users Table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster email lookups
CREATE INDEX idx_users_email ON users(email);

-- Refresh Tokens Table
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token VARCHAR(500) UNIQUE NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster token lookups
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

-- Roles Table (Optional - for multiple roles per user)
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- User-Roles Junction Table (Optional)
CREATE TABLE user_roles (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Insert default roles
INSERT INTO roles (name) VALUES ('USER'), ('ADMIN'), ('MODERATOR');
```

---

## 🐳 Docker Deployment

### Dockerfile

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  # PostgreSQL Database
  postgres:
    image: postgres:15-alpine
    container_name: auth-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: auth_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - auth-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Spring Boot Application
  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: auth-service
    restart: unless-stopped
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/auth_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      JWT_SECRET: your-production-secret-key-256-bits
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - auth-network

volumes:
  postgres_data:

networks:
  auth-network:
    driver: bridge
```

### Running with Docker

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Stop and remove volumes (clears database)
docker-compose down -v
```

---

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=AuthServiceTest
```

### Run Integration Tests

```bash
mvn verify
```

### Generate Test Coverage Report

```bash
mvn jacoco:report
```

View report at: `target/site/jacoco/index.html`

### Test Structure

```
src/test/java/com/auth/
├── controller/
│   ├── AuthControllerTest.java
│   └── UserControllerTest.java
├── service/
│   ├── AuthServiceTest.java
│   ├── UserServiceTest.java
│   └── JwtServiceTest.java
├── repository/
│   └── UserRepositoryTest.java
└── integration/
    └── AuthenticationIntegrationTest.java
```

### Example Test

```java
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldRegisterUser() throws Exception {
        RegisterRequest request = new RegisterRequest(
            "John Doe",
            "john@example.com",
            "password123",
            "USER"
        );
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }
}
```

---

## 🔒 Security

### Security Measures Implemented

- ✅ **BCrypt Password Hashing** - Passwords stored with strong encryption
- ✅ **JWT Token Authentication** - Stateless, secure authentication
- ✅ **Token Expiration** - Automatic token invalidation
- ✅ **Role-Based Access Control** - Fine-grained authorization
- ✅ **HTTPS Support** - Encrypted data transmission
- ✅ **CORS Configuration** - Controlled cross-origin requests
- ✅ **SQL Injection Prevention** - Parameterized queries with JPA
- ✅ **XSS Protection** - Input validation and sanitization
- ✅ **CSRF Protection** - Disabled for stateless API (JWT-based)

### JWT Token Structure

```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "john@example.com",
  "role": ["ROLE_USER"],
  "iat": 1703668800,
  "exp": 1703672400
}

Signature:
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

### Security Best Practices

1. **Never commit secrets** - Use environment variables
2. **Use strong JWT secrets** - Minimum 256 bits
3. **Enable HTTPS** - Always use SSL/TLS in production
4. **Implement rate limiting** - Prevent brute force attacks
5. **Regular security audits** - Keep dependencies updated
6. **Monitor logs** - Track suspicious activities

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

### How to Contribute

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open a Pull Request**

### Code Standards

- Follow Java naming conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation as needed
- Follow existing code style

### Reporting Issues

Found a bug? Have a feature request?

1. Check existing issues first
2. Use issue templates
3. Provide detailed descriptions
4. Include reproduction steps
5. Add relevant labels

---

## 📄 License

This project is licensed under the **MIT License**.

```
MIT License

Copyright (c) 2025 Your Name

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

See [LICENSE](LICENSE) file for details.

---

## 📞 Support

Need help? Have questions?

- 📧 **Email**: gauravsingh233446@gmail.com
- 💬 **Discord**: [Join our community](https://discord.gg/example)
- 🐛 **Issues**: [GitHub Issues](https://github.com/yourusername/spring-boot-auth-service/issues)
- 📚 **Documentation**: [Wiki](https://github.com/yourusername/spring-boot-auth-service/wiki)

---

## 🙏 Acknowledgments

Special thanks to:

- [Spring Framework Team](https://spring.io/)
- [Spring Security Team](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [PostgreSQL Community](https://www.postgresql.org/)
- All contributors who helped improve this project

---

## 📊 Project Stats

![GitHub stars](https://img.shields.io/github/stars/yourusername/spring-boot-auth-service?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/spring-boot-auth-service?style=social)
![GitHub issues](https://img.shields.io/github/issues/yourusername/spring-boot-auth-service)
![GitHub pull requests](https://img.shields.io/github/issues-pr/yourusername/spring-boot-auth-service)
![GitHub last commit](https://img.shields.io/github/last-commit/yourusername/spring-boot-auth-service)

---

## 🗺 Roadmap

- [ ] Add OAuth2 integration (Google, GitHub, Facebook)
- [ ] Implement email verification
- [ ] Add password reset functionality
- [ ] Two-factor authentication (2FA)
- [ ] Account lockout after failed attempts
- [ ] Audit logging system
- [ ] API rate limiting
- [ ] Swagger/OpenAPI documentation
- [ ] GraphQL support
- [ ] Kubernetes deployment configs















