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

### Security design
- Passwords are hashed using argon2 which is strong against GPU attacks
- JWT tokens are validated for each request

### User Interface
- OpenAPI/SwaggerUI Documentation

### Technical Highlights
- Using pagination to display posts for scalability
- Clean architecture separating service and controller for maintainability
- Bean validation prevents invalid user inputs

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
## Database

### User
- id | UUID
- username | String
- hashedPassword | String
- posts | List<Post>

### Post
- id | UUID
- title | String
- content | String
- userId | UUID

### Relationship
```
User (1)
   │
   └──────< Task (Many)
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
## Future Improvements

- Unit Test
- Deployment
- Database Migration

---
## Author

- Mirai (longnight-a11y)