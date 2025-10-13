# Workout Tracker API

A comprehensive RESTful API for tracking workouts and fitness progress, built with Spring Boot and PostgreSQL.

## Features

- **JWT Authentication**: Secure user authentication and authorization
- **User Management**: User registration, login, and profile management
- **Exercise Database**: Pre-populated database with 40+ exercises categorized by muscle groups
- **Workout Planning**: Create, schedule, and manage workout plans
- **Progress Tracking**: Track workout completion and exercise progress
- **Reporting**: Generate weekly and monthly workout reports
- **API Documentation**: Interactive Swagger UI for API exploration
- **Postman Collection**: Postman collection is included to testing if you prefer it over Swagger

## Technology Stack

- **Backend**: Spring Boot 3.5.x
- **Database**: PostgreSQL 16
- **Security**: Spring Security 6.x with JWT
- **ORM**: Spring Data JPA with Hibernate
- **Migrations**: Flyway
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Testing**: Postman Collection
- **Build Tool**: Gradle
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 21 or higher
- Docker and Docker Compose
- Gradle 8.x (optional, wrapper included)

## Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository:
```bash
git clone https://github.com/yourusername/workout-tracker.git
cd workout-tracker
```

2. Setup environment variables:
The minimum is to set up JWT_SECRET

3. Start the application:
```bash
docker-compose up -d
```

The application will be available at `http://localhost:8080`

## Database Schema

The application uses the following main entities:
- **Users**: User accounts and authentication
- **Exercises**: Exercise library with categories and muscle groups
- **Workout Plans**: Scheduled workout sessions
- **Workout Exercises**: Exercises within workout plans
- **Workout Logs**: Completed exercise tracking
- **User Progress**: Exercise progress over time

### Database Migrations

Flyway migrations are automatically applied on application startup. Migration files are located in:
```
src/main/resources/db/migration/
```

## Configuration

Application configuration can be customized through:
- Environment variables (`.env` file)
- `application.yml` for general settings

## Security Considerations

- Passwords are encrypted using BCrypt
- JWT tokens expire after 1 hour (configurable)
- Refresh tokens expire after 24 hours (configurable)
- CORS is configured for local development
- All endpoints except auth are protected

## License

This project is licensed under the Apache 2.0 License.

## Support

For issues and questions, please create an issue in the GitHub repository.