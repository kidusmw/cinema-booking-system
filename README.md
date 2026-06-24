# Cinema Booking System

A desktop cinema booking application built with JavaFX and a Hexagonal/Clean Architecture on PostgreSQL.

## Architecture

The project follows **Hexagonal (Ports & Adapters)** architecture with **Pragmatic Services** (not fragmented Use-Cases):

```
domain/        — Rich domain models with behavior (Booking.confirm(), Seat.book(), etc.)
application/   — AppContext (DI), BookingFacade (transaction safety)
infrastructure/ — Jdbc repositories, Flyway migrations, HikariCP, BCrypt hashing
ui/            — JavaFX controllers and views, grouped by user role (common/, customer/, admin/)
```

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI | JavaFX 21 (Scene Builder, FXML-free) |
| Build | Maven 3.9+ |
| Database | PostgreSQL 16 |
| Migrations | Flyway 10 |
| Connection Pool | HikariCP 5 |
| Password Hashing | BCrypt |
| Testing | JUnit 5, Mockito, Testcontainers |
| Linting | Spotless (Google AOSP), SpotBugs, PMD |

## Prerequisites

- **Java 21** (the only manual install — everything else is handled by the wrapper)
- Docker & Docker Compose (for PostgreSQL + tests)

## Quick Start

```bash
# 1. Start PostgreSQL
docker-compose up -d

# 2. Run the application
make run
```

> The Maven Wrapper (`mvnw`) is included in the repository — `make` and `make ci` use it automatically.
> No local Maven installation is needed beyond what the wrapper downloads.

The application will:
1. Read `db.properties` (defaults to localhost:5432)
2. Run Flyway migrations automatically
3. Launch the JavaFX window

## Testing

```bash
# Run all tests (requires Docker for Testcontainers)
mvn clean test

# Current coverage: 38+ tests
# - Domain model unit tests
# - Service layer tests (AuthService, BookingService, PaymentService)
# - Flyway migration tests with Testcontainers
# - Repository integration tests
```

## Project Structure

```
src/main/java/
├── domain/
│   ├── model/         — Movie, User, Hall, Showtime, Seat, Booking, Payment
│   ├── port/          — Repository interfaces (ports)
│   └── service/       — AuthService, BookingService, PaymentService
├── application/
│   ├── AppContext.java    — Manual DI container
│   └── service/
│       └── BookingFacade.java — Atomic booking+payment transaction
├── infrastructure/
│   ├── config/       — AppConfig (db.properties loader)
│   ├── persistence/  — Jdbc repositories, HikariCP, Flyway
│   └── security/     — BCrypt password hashing
└── ui/
    ├── Main.java                      — Composition Root
    ├── controller/
    │   ├── common/     — Login, SignUp, Welcome, Navigation
    │   ├── customer/   — Browse movies, select seats, payment
    │   └── admin/      — Manage movies, halls, shows, users
    ├── view/
    │   ├── common/     — Login, SignUp, Welcome, AuthChoice
    │   ├── customer/   — Dashboard, MovieBrowser, SeatSelection
    │   └── admin/      — Dashboard, Management pages
    └── common/         — Theme constants
```

## Database Migrations

- `V1__initial_schema.sql` — Legacy schema copy
- `V2__modernize_schema.sql` — BIGINT PKs, CHECK constraints, audit columns, indexes

## Refactoring Plan

See [Refactoring Plan.md](Refactoring%20Plan.md) for the full evolution from legacy to clean architecture.
