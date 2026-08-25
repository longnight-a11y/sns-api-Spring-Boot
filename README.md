![CI](https://github.com/longnight-a11y/task-management-api-Spring-Boot/actions/workflows/ci.yml/badge.svg)
# SNS API

## Overview

This is a RESTful API developed with **Spring Boot 4.x**, **Java25**, and **PostgreSQL**. 
It simulates SNS API where users can create and manage their own posts safely.

While working on this project, I focused on:
- Layered architecture
- Spring Security
- JWT authentication
- Docker compose
- Exception Handling
- JPA relationships
- Bean Validation
- Pagination

---
## Goal of This Project

The goal of this project is to demonstrate and
improve my backend development skills such as
REST API, authentication, authorization, database design,
and security considerations.

---
## Features

### Authentication
- JWT authentication ensures only authenticated users can manage their own posts.

### Post Management
- Create new posts
- View all/only my posts
- View a single post found by post ID
- Update my post (by using PATCH, users can modify only the fields they want)
- Delete my own post

### User Interface
- OpenAPI/SwaggerUI Documentation

### Exception Handling

Application exceptions are handled centrally using
`@RestControllerAdvice` and returns as
`ProblemDetail` responses.

For example:
- `400 Bad Request` — Invalid request data
- `401 Unauthorized` — Authentication failed
- `403 Forbidden` — User is not authorized
- `404 Not Found` — Resource does not exist

### Technical Highlights
- Using pagination to display posts for scalability
- Layered architecture separates JWT authentication, HTTP handling, business logic, and data access for maintainability and testability
- Bean validation prevents invalid user inputs

---
## Architecture
This project follows a layered architecture.

### System Architecture Diagram
```
         Client
           │
           │ HTTP Request, JWT
           ▼
┌──────────────────────┐
│ Spring Security      │
│ JWT Authentication   │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Controller           │
│ HTTP request/response│
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Service              │
│ Business logic       │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Repository           │
│ Data access          │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ PostgreSQL           │
└──────────────────────┘
```

### Responsibilities
- Security: Validates JWT tokens and authenticates users
- Controller: Handles HTTP requests/responses
- Service: Contains business logic such as an authorization or post management
- Repository: Handles database access through Spring Data JPA
- Global Exception Handler: Converts application exceptions into consistent HTTP error responses
---
## Design Decisions

### Why JWT?
JWT was chosen to provide stateless authentication for the REST API.
The server does not need to maintain session state
for authenticated users. It can enhance scalability and simplicity.

### Why layered architecture?
Security Filter, Controller, Service, and Repository responsibilities
are separated for maintainability and testability.

### Why PATCH for update?
PATCH allows clients to update only the fields they want to change,
instead of sending entire resource. It makes the API user-friendly.

---
## Security

- Passwords are hashed using argon2 which is strong against GPU attacks
- JWT is used for stateless authentication
- JWT tokens are validated for protected endpoints
- Users can update and delete only their own posts (authorization)
- Authentication failure returns `401 Unauthorized`
- Authorization failure returns `403 Forbidden`
- Passwords are never included in API responses
- Bean Validation is used to validate incoming requests

---
## CI

### This project uses GitHub Actions for continuous integration.

Every push ad pull requests automatically:
- Builds the project
- Verifies the application can be compiled successfully

---
## Tech Stack

| Category | Technology | Version |
|---|---|---|
| Language | Java | 25 |
| Framework | Spring Boot | 4.1.0 |
| Security | Spring Security | 7.1.0 |
| ORM | Spring Data JPA | 4.1.0 |
| ORM | Hibernate | 7.4.1.Final |
| Database | PostgreSQL | 17 |
| Validation | Jakarta Bean Validation | 3.1.1 |
| Authentication | JJWT | 0.12.6 |
| API Documentation | Springdoc OpenAPI | 3.0.3 |
| API Documentation | Swagger UI | 5.32.2 |
| Testing | JUnit | 6.0.3 |
| Testing | Mockito | 5.23.0 |
| Build Tool | Maven | — |
| Containerization | Docker | — |
| CI | GitHub Actions | — |

---
## API Endpoints

### Authentication
- Login | POST /auth/login

### Users
- Create User | POST /users
- Get My Information | GET /users/me

### Posts
- Create Post | POST /posts
- Get Posts | GET /posts
- Get My Posts | GET /posts/me
- Get Single Post | GET /{postId}
- Update Post | PATCH /{postId}
- Delete Post | DELETE /{postId}

---
## API Examples

### Login
POST /auth/login  
Content-Type: application/json  

Request body:
```
{
  "username": "Mikasa",
  "password": "testpass1"
}
```
Response body:
```
{
  "token": "eyJhbGc......",
  "tokenType": "bearer"
}
```

### Create Post
POST /posts  
Authorization: Bearer eyJhbGc....  
Content-Type: application/json

Request body:
```
{
  "title": "Helloooooo",
  "content": "this is test post"
}
```
Response body:
```
{
  "id": "fa8c8f8a-3fe8-4512-a73f-be6936018959",
  "title": "Helloooooo",
  "content": "this is test post",
  "user": {
    "id": "5e12959b-374e-4ef5-9e29-7cfc10a93562",
    "username": "Mikasa"
  }
}
```
### Practical Update
The API supports practical update using `PATCH`.
Only the field provided in the request are updated.

PATCH /posts/{postId}  
Authorization: Bearer eyJhbGc....  
Content-Type: application/json

Request body:
```
{
  "title": "UPDATED"
}
```
Response body:
```
{
  "id": "fa8c8f8a-3fe8-4512-a73f-be6936018959",
  "title": "UPDATED",
  "content": "this is test post",
  "user": {
    "id": "5e12959b-374e-4ef5-9e29-7cfc10a93562",
    "username": "Mikasa"
  }
}
```
The existing `content` remains unchanged.

---
## Database

### User
- id | UUID
- username | String
- hashedPassword | String
- posts | List\<Post>

### Post
- id | UUID
- title | String
- content | String
- userId | UUID

### Relationship
```
User (1)
   │
   └──────< Post (Many)
```

### ER Diagram
```
┌─────────┐       ┌─────────┐
│  User   │ 1   N │  Post   │
├─────────┤───────├─────────┤
│ id      │       │ id      │
│ username│       │ title   │
│ password│       │ content │
└─────────┘       │ userId  │
                  └─────────┘
```

---
## How to Run Locally

Clone this repository:
```
git clone https://github.com/longnight-a11y/sns-api-Spring-Boot.git
```

Configure your database in application.yml which is located in src/main/resources.
Example:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/snsdb
    username: your_username
    password: your_password

app:
  jwt:
    secret: your_secret_key
```
Run the application with the command below:
```
mvn spring-boot:run
```

---
## SwaggerUI

```
http://localhost:8080/swagger-ui/index.html
```

---
## Testing


---
## Future Improvements

- Unit Test
- Deployment
- Database Migration

---
## Author

- Mirai (longnight-a11y)