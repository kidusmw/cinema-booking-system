# REFACTORING PLAN V2.0 — Cinema Booking System

## 1. CURRENT STATE (As-Is)

### Architecture: Degenerate Layered MVC / Transaction Script

```
src/main/java/
├── Controller/        ← fat: UI + business logic + data access mixed together
├── DAO/               ← inconsistent: some have interfaces, some are concrete
├── Database/          ← static singleton HikariCP pool (no interface)
├── Model/             ← anemic POJOs with mixed int/String IDs
└── View/              ← JavaFX builders with no logic
```

### Tightest Coupling

| Coupling | Severity |
|---|---|
| `Controller` → `new XxxDAOimp()` directly | **Critical** — no DI, no interface programming |
| `DAO` → `DatabaseConnection.getConnection()` (static) | **Critical** — impossible to mock |
| `PaymentController` → `BookingDAO` + `PaymentDAO` (no txn) | **Critical** — orphan bookings on crash |
| `UserSession` (static) + `NavigationManager` (static) | **High** — global mutable state, untestable |
| Controller → Controller via `new NextController(stage,…)` | **High** — tangled navigation graph |

### Single Biggest Testability Bottleneck

**`DatabaseConnection` is a static singleton with no interface.** Every DAO calls `getConnection()` directly. Zero tests exist anywhere in the repo.

---

## 2. CRITICAL SMELLS (Prioritized)

### P0 — Security & Data Integrity

| # | Smell | Location | Impact |
|---|---|---|---|
| 1 | **Plaintext passwords** | `UserDAOimp.login()` line 134: `WHERE Username=? AND user_Password=?` | Full credential exposure. |
| 2 | **No password hashing** | `UserDAOimp.addUser()` stores raw `user.getPassword()` | Same as above. |
| 3 | **No transactional boundary** | `PaymentController.processSuccessfulPayment()` line 170–189: booking in one connection, payment in another | Booking saved, payment fails → orphan. |
| 4 | **DB schema mismatch (code ≠ SQL)** | DAOs reference `User_`, `Movie_Hall_`, `Show_Table`, `Movie_` with columns like `First_N`, `Last_N`, `Poster_Path` — but `script.sql` defines `"user"`, `hall`, `showtime`, `movie` with snake_case | App cannot run against the provided schema. |
| 5 | **Hardcoded DB credentials** | `docker-compose.yml` + `script.sql`: `java_user` / `java_pass` | Leaked to git. |
| 6 | **T-SQL in Postgres code** | `MovieDAO.getTopRatedMovies()`: `SELECT TOP (?) …` | Runtime crash on Postgres. |

### P1 — Architectural

| # | Smell | Location |
|---|---|---|
| 7 | **God Controller** (206 lines) | `PaymentController`: OTP, countdown, booking, payment, seat nav, UI styling |
| 8 | **Inconsistent DAO pattern** | `MovieDAO`, `MovieHallDAO`, `BookingDAO` = concrete; `UserDAO`, `SeatDAO`, `ShowDAO`, `PaymentDAO` = interface + impl |
| 9 | **Mixed ID types** | `Booking.bookingID`=`int`, `Payment.paymentID`=`String`, `Seat.seatID`=`String`, `Show.showID`=`String` → `Integer.parseInt()` everywhere |
| 10 | **Controllers create controllers** | Every screen transition does `new NextController(stage, …)` |

### P2 — Maintainability

| # | Smell |
|---|---|
| 11 | Typo filename: `MovieManagmentContrller.java` |
| 12 | Duplicated UI constants (`ACCENT`, `TEXT_DARK`, …) in 21 files |
| 13 | Dead stubs: `SeatDAOimp.getSeatsByShow()` returns `new ArrayList<>()`, `MovieHallDAO` has 5 stubs |
| 14 | Zero test dependencies in `pom.xml` |
| 15 | `script.sql` is UTF-16LE (should be UTF-8) |

---

## 3. AUDIT: script.sql Defects

Before writing Flyway migrations, here is the precise audit of the current schema:

### Naming
| Issue | Detail |
|---|---|
| ✅ Good | snake_case, singular table names, `"user"` quoted (reserved word) |
| ⚠️ `"user"` quoted | Works but ugly. Alternative: rename to `"account"` or `"customer"`. Leave as-is for now. |

### Data Types
| Column | Current | Issue | Fix |
|---|---|---|---|
| All `SERIAL` PKs | `INT` | `SERIAL` is `INTEGER` — only 2B rows | `BIGSERIAL` → `BIGINT` for all PKs and FKs |
| `movie.rating` | `DECIMAL(3,1)` | Max 99.9 — fine for 0–10 scale | Acceptable, but `DECIMAL(2,1)` is more precise |
| `payment.verification_code` | `VARCHAR(20)` | May be too short for hashes | `VARCHAR(64)` |
| `booking.movie_name` | `VARCHAR(200)` | Denormalized — duplicates `movie.title` | Accept as read-only denormalization, but add a trigger or app-layer sync |
| `seat.status` | `VARCHAR(20)` | Magic strings | Consider `VARCHAR(20)` with `CHECK( IN ('available','booked','reserved'))` |

### Missing Constraints
| Defect | Detail |
|---|---|
| ❌ No `NOT NULL` on `"user".email` | Allows null emails — breaks payment receipts |
| ❌ No `CHECK` on `"user".role` | `VARCHAR(20)` with no constraint — `'admin'`, `'Admin'`, `'ADMIN'` all valid |
| ❌ No `CHECK` on `booking.status` | `VARCHAR(50)` — `'pending'`, `'confirmed'`, `'cancelled'`, `'no-show'` should be enforced |
| ❌ No `CHECK` on `payment.status` | `VARCHAR(20)` — `'pending'`, `'paid'`, `'failed'`, `'refunded'` |
| ❌ No `CHECK` on `seat.seat_type` | `VARCHAR(30)` — `'regular'`, `'vip'`, `'disabled'` |
| ❌ No `CHECK` on `hall.hall_type` | `VARCHAR(50)` — `'regular'`, `'vip'`, `'imax'` |
| ❌ No `updated_at` audit columns | No table has `updated_at TIMESTAMPTZ` |

### Indexes
| Issue | Detail |
|---|---|
| ✅ Good | Indexes on `seat(hall_id)`, `showtime(movie_id/hall_id/date)`, `booking(user_id/show_id/status)` |
| ⚠️ Missing | `payment(booking_id)` — already covered by `UNIQUE`, but explicit index helps |
| ⚠️ Missing | `"user"(username)` — already `UNIQUE`, but explicit index for lookup |
| ⚠️ Missing | `"user"(role)` — for admin filtering |

### Seed Data
| Issue | Detail |
|---|---|
| ⚠️ Duplicate users | `hawi123` appears twice (user_id 2 and 3) |
| ⚠️ Inconsistent IDs | `show_id` values 8–11 but no 1–7 (gap from prior deletes) |
| ⚠️ Missing `ticket_price` | Showtimes 8–11 insert without `ticket_price` (defaults to 0.00) |
| ⚠️ Plaintext passwords | All seed passwords stored as plaintext strings |

### Encoding
| Issue | Detail |
|---|---|
| ❌ UTF-16LE | File is UTF-16LE with BOM — causes tooling issues. Must convert to UTF-8. |

---

## 4. TARGET ARCHITECTURE (To-Be)

### Hexagonal / Clean Architecture with Pragmatic Services

```
src/main/java/
├── domain/                        ← Innermost — zero framework deps
│   ├── model/                     ← Rich domain models (behavior + data)
│   │   ├── User.java              · confirmPassword(), isAdmin()
│   │   ├── Movie.java             · getFormattedDuration()
│   │   ├── Hall.java              · isVip()
│   │   ├── Seat.java              · isAvailable(), book(), release()
│   │   ├── Showtime.java          · isPast(), hasStarted()
│   │   ├── Booking.java           · confirm(), cancel(), total()
│   │   ├── BookingSeat.java       · (value object for junction)
│   │   └── Payment.java           · markPaid(), markFailed(), verifyOtp()
│   ├── service/                   ← Domain services (orchestration)
│   │   ├── BookingService.java    · createBooking, cancelBooking, getHistory
│   │   ├── PaymentService.java    · processPayment, verifyPayment, refund
│   │   └── AuthService.java       · login, register, hashPassword, verifyPassword
│   └── port/                      ← Outbound port interfaces
│       ├── UserRepository.java
│       ├── MovieRepository.java
│       ├── HallRepository.java
│       ├── SeatRepository.java
│       ├── ShowtimeRepository.java
│       ├── BookingRepository.java
│       └── PaymentRepository.java
│
├── application/                   ← Application layer (DTOs, orchestration)
│   ├── dto/
│   │   ├── BookingRequest.java
│   │   ├── PaymentRequest.java
│   │   └── LoginRequest.java
│   └── service/                   ← Application services (txn management, security)
│       ├── BookingFacade.java     · wraps BookingService + PaymentService + txn
│       └── AuthFacade.java        · wraps AuthService + UserSession management
│
├── infrastructure/                ← Adapter implementations
│   ├── persistence/
│   │   ├── ConnectionProvider.java         ← interface (was static DatabaseConnection)
│   │   ├── DatabaseConnectionProvider.java ← impl (wraps HikariCP)
│   │   ├── JdbcUserRepository.java
│   │   ├── JdbcMovieRepository.java
│   │   ├── JdbcHallRepository.java
│   │   ├── JdbcSeatRepository.java
│   │   ├── JdbcShowtimeRepository.java
│   │   ├── JdbcBookingRepository.java
│   │   └── JdbcPaymentRepository.java
│   ├── security/
│   │   └── BCryptPasswordHasher.java
│   ├── config/
│   │   └── AppConfig.java         ← loads db.properties, creates ConnectionProvider
│   └── migration/
│       └── db/migration/          ← Flyway migration files
│           ├── V1__initial_schema.sql
│           └── V2__modernize_schema.sql
│
└── ui/                            ← Inbound adapters (JavaFX)
    ├── controller/                ← Thin — delegate to Application services
    ├── view/                      ← JavaFX builders (same as current View/)
    ├── common/
    │   └── Theme.java             ← Single source for ACCENT, TEXT_DARK, etc.
    └── Main.java                  ← Composition Root (manual DI)
```

### Key Architectural Decisions

| Decision | Rationale |
|---|---|
| **Pragmatic Services, not 50 Use-Cases** | `BookingService`, `PaymentService`, `AuthService` — cohesive, not fragmented. |
| **Rich domain models** | `Booking.confirm()` contains business logic (validates state, fires events). Service calls model methods. |
| **All repositories have interfaces** | Enables mocking without a database. True dependency inversion. |
| **`ConnectionProvider` interface** | Static `DatabaseConnection` replaced with injectable interface. Testable. |
| **Manual DI in Composition Root** (`Main.java`) | Zero framework overhead. Everything wired in one place. |
| **Consistent `Long` IDs** | No more `Integer.parseInt()`. All PKs are `BIGINT` in DB → `Long` in Java. |
| **Transactions via `BookingFacade`** | Application service manages the connection/txn lifecycle, passes to domain services. |

---

## 5. REFACTORING STRATEGY — 4 Phases

### Phase 0: Testing Foundation

**Goal:** Add test infrastructure and write characterization tests before touching production code.

**Files to touch:**
- `pom.xml`
- `src/test/java/…` (new directory tree)

**pom.xml changes — add:**
```xml
<dependency><groupId>org.junit.jupiter</groupId><artifactId>junit-jupiter</artifactId><version>5.11.4</version><scope>test</scope></dependency>
<dependency><groupId>org.mockito</groupId><artifactId>mockito-core</artifactId><version>5.14.2</version><scope>test</scope></dependency>
<dependency><groupId>org.mockito</groupId><artifactId>mockito-junit-jupiter</artifactId><version>5.14.2</version><scope>test</scope></dependency>
<dependency><groupId>org.testcontainers</groupId><artifactId>postgresql</artifactId><version>1.20.4</version><scope>test</scope></dependency>
<dependency><groupId>org.testcontainers</groupId><artifactId>junit-jupiter</artifactId><version>1.20.4</version><scope>test</scope></dependency>
<dependency><groupId>org.assertj</groupId><artifactId>assertj-core</artifactId><version>3.26.3</version><scope>test</scope></dependency>
<dependency><groupId>org.flywaydb</groupId><artifactId>flyway-core</artifactId><version>10.18.2</version></dependency>
<dependency><groupId>org.flywaydb</groupId><artifactId>flyway-database-postgresql</artifactId><version>10.18.2</version></dependency>
```

Also add the Flyway Maven plugin:
```xml
<plugin><groupId>org.flywaydb</groupId><artifactId>flyway-maven-plugin</artifactId><version>10.18.2</version></plugin>
```

**Characterization tests to write (in order):**
1. `UserDAOimpTest` — capture current plaintext login flow (with TestContainers)
2. `BookingDAOTest` — capture `addBooking()` + `getAllBookings()` behavior
3. `PaymentControllerTest` — capture `processSuccessfulPayment()` flow (mock the DAOs)
4. `LoginControllerTest` — capture role-dispatch logic (`Admin` vs `Customer`)

**Testing strategy:** Write tests that pass against the *current* buggy code. These become the regression safety net for all subsequent phases.

**Breaking change?** NO.

**Branch:** `refactor/phase-0-test-foundation`

---

### Phase 1: Flyway Schema Setup + Migration

**Goal:** Establish Flyway, convert `script.sql` to a proper V1 migration, write the V2 modernization migration, and delete the old `script.sql`.

#### Step 1.1 — Convert script.sql to V1

**Files created:**
- `src/main/resources/db/migration/V1__initial_schema.sql`

**Action:** Copy `script.sql` verbatim (after converting from UTF-16LE to UTF-8). Remove the `CREATE ROLE` and `GRANT` statements (those are operational, not schema). Remove seed data (will go in a separate idempotent step or be added by app). Keep only the DDL (CREATE TABLE, CREATE INDEX).

#### Step 1.2 — Write V2 modernization migration

**Files created:**
- `src/main/resources/db/migration/V2__modernize_schema.sql`

**SQL changes in V2:**

```sql
-- 1.1: Change all SERIAL PKs to BIGINT
ALTER TABLE "user"   ALTER COLUMN user_id   TYPE BIGINT;
ALTER TABLE movie    ALTER COLUMN movie_id   TYPE BIGINT;
ALTER TABLE hall     ALTER COLUMN hall_id    TYPE BIGINT;
ALTER TABLE seat     ALTER COLUMN seat_id    TYPE BIGINT;
ALTER TABLE showtime ALTER COLUMN show_id    TYPE BIGINT;
ALTER TABLE booking  ALTER COLUMN booking_id TYPE BIGINT;
ALTER TABLE payment  ALTER COLUMN payment_id TYPE BIGINT;

-- Also widen FK columns to match
ALTER TABLE seat     ALTER COLUMN hall_id    TYPE BIGINT;
ALTER TABLE showtime ALTER COLUMN movie_id   TYPE BIGINT;
ALTER TABLE showtime ALTER COLUMN hall_id    TYPE BIGINT;
ALTER TABLE booking  ALTER COLUMN user_id    TYPE BIGINT;
ALTER TABLE booking  ALTER COLUMN show_id    TYPE BIGINT;
ALTER TABLE booking_seat ALTER COLUMN booking_id TYPE BIGINT;
ALTER TABLE booking_seat ALTER COLUMN seat_id    TYPE BIGINT;
ALTER TABLE payment  ALTER COLUMN booking_id TYPE BIGINT;

-- 1.2: Add CHECK constraints for status/type columns
ALTER TABLE "user"   ADD CONSTRAINT chk_user_role CHECK (role IN ('admin','customer'));
ALTER TABLE booking  ADD CONSTRAINT chk_booking_status CHECK (status IN ('pending','confirmed','cancelled','no-show'));
ALTER TABLE payment  ADD CONSTRAINT chk_payment_status CHECK (status IN ('pending','paid','failed','refunded'));
ALTER TABLE seat     ADD CONSTRAINT chk_seat_type CHECK (seat_type IN ('regular','vip','disabled'));
ALTER TABLE hall     ADD CONSTRAINT chk_hall_type CHECK (hall_type IN ('regular','vip','imax'));
ALTER TABLE seat     ADD CONSTRAINT chk_seat_status CHECK (status IN ('available','booked','reserved'));

-- 1.3: Add NOT NULL where missing
ALTER TABLE "user" ALTER COLUMN email SET NOT NULL;

-- 1.4: Add audit columns
ALTER TABLE "user"   ADD COLUMN updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE movie    ADD COLUMN updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE hall     ADD COLUMN updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE seat     ADD COLUMN updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE showtime ADD COLUMN updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE booking  ADD COLUMN updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE payment  ADD COLUMN updated_at TIMESTAMPTZ DEFAULT NOW();

-- 1.5: Widen verification_code for hashes
ALTER TABLE payment ALTER COLUMN verification_code TYPE VARCHAR(64);

-- 1.6: Add missing indexes
CREATE INDEX IF NOT EXISTS idx_user_username ON "user"(username);
CREATE INDEX IF NOT EXISTS idx_user_role ON "user"(role);
CREATE INDEX IF NOT EXISTS idx_payment_booking ON payment(booking_id);
```

#### Step 1.3 — Add Flyway Config

**Files created:**
- `src/main/resources/flyway.conf` (or use `application.properties` with `flyway.url`, `flyway.user`, `flyway.password`)
- `src/main/resources/application.properties` (new, to centralize config)

**Action:** Configure Flyway to auto-migrate on startup via a `FlywayMigrator` class called from `Main.java`.

#### Step 1.4 — Convert script.sql to UTF-8

**Files modified:**
- `script.sql` — convert to UTF-8 (or remove after V1 is verified)

**Testing strategy:**
- TestContainers test: apply `V1__initial_schema.sql`, then `V2__modernize_schema.sql`. Verify all columns, constraints, and indexes exist.
- Verify the old `UserDAOimp` tests still pass against the migrated schema.

**Breaking change?** YES — SERIAL → BIGSERIAL changes PK type. Existing data is preserved (ALTER COLUMN TYPE BIGINT is safe). New apps must use `Long` in Java.

**Branch:** `refactor/phase-1-flyway-schema`

---

### Phase 2: Domain Extraction + Rich Models + Repository Interfaces

**Goal:** Build the domain layer with rich models, extract repository interfaces, and introduce `ConnectionProvider` DI — all while the existing controllers and DAOs still work.

#### Step 2.1 — Create Rich Domain Models

**New files in `domain/model/`:**
- `User.java` — add `boolean isAdmin()`, `boolean isCustomer()`, `boolean verifyPassword(String plaintext, PasswordHasher hasher)`
- `Movie.java` — same fields as existing, add `getFormattedDuration()` (already exists)
- `Hall.java` — rename from `Moviehall`, add `boolean isVip()`
- `Seat.java` — add `void book()`, `void release()`, `boolean isAvailable()`
- `Showtime.java` — rename from `Show`, add `boolean isPast()`, `boolean hasStarted()`
- `Booking.java` — add `void confirm()`, `void cancel()`, `Money total()`
- `BookingSeat.java` — new value object wrapping `(BookingId, SeatId, Money price)`
- `Payment.java` — add `void markPaid()`, `void markFailed()`, `boolean verifyOtp(String otp)`

**ID type change:** All `*ID` fields become `Long`. Old String getters become `@Deprecated` wrappers.

#### Step 2.2 — Create Repository Interfaces

**New files in `domain/port/`:**
- `UserRepository.java` — `Optional<User> findByUsername(String)`, `Optional<User> findById(Long)`, `List<User> findAll()`, `User save(User)`, `void delete(Long)`
- `MovieRepository.java` — `Optional<Movie> findById(Long)`, `List<Movie> findAll()`, `List<Movie> searchByTitle(String)`, `Movie save(Movie)`, `void delete(Long)`
- `HallRepository.java` — `Optional<Hall> findById(Long)`, `List<Hall> findAll()`, `Hall save(Hall)`
- `SeatRepository.java` — `List<Seat> findByHallId(Long)`, `Optional<Seat> findById(Long)`, `void updateStatus(Long seatId, String status)`
- `ShowtimeRepository.java` — `List<Showtime> findByMovieId(Long)`, `Optional<Showtime> findById(Long)`, `List<Showtime> findByDate(LocalDate)`
- `BookingRepository.java` — `Booking save(Booking)`, `Optional<Booking> findById(Long)`, `List<Booking> findByUserId(Long)`, `List<Booking> findAll()`, `void cancel(Long bookingId)`
- `PaymentRepository.java` — `Payment save(Payment)`, `Optional<Payment> findById(Long)`, `Optional<Payment> findByBookingId(Long)`

#### Step 2.3 — Create ConnectionProvider Interface

**New file:** `infrastructure/persistence/ConnectionProvider.java`
```java
public interface ConnectionProvider {
    Connection getConnection() throws SQLException;
}
```

**Modify:** `DatabaseConnection.java` → implements `ConnectionProvider`. Old static methods remain as deprecated for backward compatibility during transition.

#### Step 2.4 — Create PasswordHasher

**New file:** `infrastructure/security/PasswordHasher.java`
```java
public interface PasswordHasher {
    String hash(String plaintext);
    boolean verify(String plaintext, String hash);
}
```

**New file:** `infrastructure/security/BCryptPasswordHasher.java` (uses `org.mindrot.jbcrypt.BCrypt`)

#### Step 2.5 — Create AuthService

**New file:** `domain/service/AuthService.java`
```java
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordHasher hasher;

    public User login(String username, String plainPassword) { … }
    public User register(String username, String plainPassword, String role, …) { … }
}
```

#### Step 2.6 — Create BookingService + PaymentService

**New file:** `domain/service/BookingService.java` — `Booking createBooking(…)`, `void cancelBooking(Long bookingId)`
**New file:** `domain/service/PaymentService.java` — `Payment processPayment(…)`, `boolean verifyOtp(Long paymentId, String otp)`
**New file:** `application/service/BookingFacade.java` — wraps `BookingService` + `PaymentService` in a single transaction (manages connection lifecycle)

**Testing strategy:**
- Unit-test every domain model method (pure logic, no mocks).
- Unit-test every service with mocked repository interfaces.
- Integration-test `BookingFacade` with TestContainers (full booking → payment flow in one txn).

**Breaking change?** YES for domain models (new package, updated types). NO for the old UI — it still uses old Controller/DAO/Model classes.

**Branch:** `refactor/phase-2-domain-models`

---

### Phase 3: Service/Application Layer + New Repository Implementations

**Goal:** Write the JDBC implementations of all repository interfaces (targeting the V2 schema), build the application service facades, and wire everything together with manual DI.

#### Step 3.1 — Write JDBC Repositories

**New files in `infrastructure/persistence/`:**
- `JdbcUserRepository.java` — targets `"user"` table with `user_id`, `first_name`, `last_name`, `password` (will be hashed later), `role` (snake_case columns). Uses `ConnectionProvider`.
- `JdbcMovieRepository.java` — targets `movie` table with `movie_id`, `title`, `genre`, `duration_min`, `rating`, `poster_path`, `language`, `release_date`, `description`, `is_active`.
- `JdbcHallRepository.java` — targets `hall` table with `hall_id`, `name`, `capacity`, `hall_type`.
- `JdbcSeatRepository.java` — targets `seat` table with `seat_id`, `hall_id`, `seat_number`, `seat_type`, `status`.
- `JdbcShowtimeRepository.java` — targets `showtime` table with `show_id`, `movie_id`, `hall_id`, `show_date`, `show_time`, `ticket_price`.
- `JdbcBookingRepository.java` — targets `booking` + `booking_seat` tables.
- `JdbcPaymentRepository.java` — targets `payment` table.

All repositories use `Long` IDs and parameterized queries.

#### Step 3.2 — Wire Composition Root in Main.java

```java
// Main.java — Composition Root
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        AppConfig config = AppConfig.load("/db.properties");
        ConnectionProvider connectionProvider = new DatabaseConnectionProvider(config);
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        // Run Flyway migrations
        FlywayMigrator.migrate(config);

        // Repositories
        UserRepository userRepo = new JdbcUserRepository(connectionProvider, passwordHasher);
        MovieRepository movieRepo = new JdbcMovieRepository(connectionProvider);
        HallRepository hallRepo = new JdbcHallRepository(connectionProvider);
        SeatRepository seatRepo = new JdbcSeatRepository(connectionProvider);
        ShowtimeRepository showtimeRepo = new JdbcShowtimeRepository(connectionProvider);
        BookingRepository bookingRepo = new JdbcBookingRepository(connectionProvider);
        PaymentRepository paymentRepo = new JdbcPaymentRepository(connectionProvider);

        // Domain Services
        AuthService authService = new AuthService(userRepo, passwordHasher);
        BookingService bookingService = new BookingService(bookingRepo, seatRepo);
        PaymentService paymentService = new PaymentService(paymentRepo);

        // Application Facades
        BookingFacade bookingFacade = new BookingFacade(bookingService, paymentService, connectionProvider);

        // Launch UI
        new WelcomeController(stage, authService, bookingFacade, …);
    }
}
```

#### Step 3.3 — Thin Controllers

Update all 21 controllers to:
1. Accept dependencies via constructor injection (no `new XxxDAOimp()`).
2. Delegate business logic to `AuthService`, `BookingFacade`, etc.
3. Remove duplicated UI constants → reference `Theme.java`.
4. Replace navigation `new NextController(stage, …)` with injected service calls where possible (e.g., login success → `authService.login()` returns user → controller dispatches to next screen).

**Files modified:** All controllers in `ui/controller/`.

#### Step 3.4 — Password Hashing Cutover

**Action:**
- `AuthService.login()`: hash the input password, compare with stored hash using `BCryptPasswordHasher.verify()`.
- `AuthService.register()`: hash the password before calling `userRepo.save()`.
- Add backward compatibility: if `verify()` fails and the stored password is in plaintext (legacy), accept it, hash it, and update the record in-place.

**Testing strategy:**
- TestContainers tests for every JDBC repository (insert, select, update, delete).
- Unit tests for thin controllers with mocked services.
- Integration test for `BookingFacade` — verify transactional rollback on payment failure.

**Breaking change?** YES — password storage changes, schema changes, controller constructors change. But the old DAO layer is still intact and working for rollback.

**Branch:** `refactor/phase-3-services-orchestration`

---

### Phase 4: Infrastructure + UI Wiring + Portfolio Polish

**Goal:** Remove old DAO package, delete dead code, fix the typo class name, consolidate UI theme, and finalize the new architecture. This is the cleanup and stabilization phase.

#### Step 4.1 — Remove Old DAO Package

**Files deleted:**
- `src/main/java/DAO/` — entire package (all interfaces, implementations, concrete classes)
- `src/main/java/Database/DatabaseConnection.java` — replaced by `ConnectionProvider` + `DatabaseConnectionProvider`
- `src/main/java/Model/` (optional: keep as deprecated wrappers that extend new domain models)

#### Step 4.2 — Rename Typo Class

`MovieManagmentContrller.java` → `MovieManagementController.java`. Update all references.

#### Step 4.3 — Consolidate UI Theme

**New file:** `ui/common/Theme.java`
```java
public class Theme {
    public static final String ACCENT = "#DB2777";
    public static final String TEXT_DARK = "#1E293B";
    public static final String TEXT_MUTED = "#64748B";
    public static final String BORDER = "#E2E8F0";
    public static final String SUCCESS = "#10B981";
    public static final String DANGER = "#DC2626";
    public static final String WARNING = "#F59E0B";
}
```

Remove all `private static final String ACCENT = …` duplicated constants from 21 controllers.

#### Step 4.4 — Remove Dead Code Stubs

- `SeatDAOimp.getSeatsByShow()` — delete (no longer needed; `SeatRepository.findByShowtimeId()` replaces it properly)
- `MovieHallDAO.searchMovieHallByName()`, `getMovieHallsByCapacity()`, etc. — delete

#### Step 4.5 — Fix Logging

Replace all `System.out.println()` and `e.printStackTrace()` with SLF4J logger calls. Already have `logback-classic` dependency and `logback.xml` config.

#### Step 4.6 — script.sql Cleanup

Convert `script.sql` from UTF-16LE to UTF-8. Optionally delete it (Flyway migrations are now the source of truth), or keep it as documentation pointing to `src/main/resources/db/migration/`.

#### Step 4.7 — docker-compose.yml Healthcheck

Add a healthcheck for the app service (optional — only if a web server is added).

**Testing strategy:**
- Full regression suite: all Phase 0 characterization tests + all new unit/integration tests must pass.
- `mvn clean compile` — zero warnings.
- Manual smoke test: `mvn javafx:run` — app starts, login works, booking flow works.

**Breaking change?** NO — this phase is pure deletion and cleanup. All functionality is already covered by the new architecture.

**Branch:** `refactor/phase-4-infra-ui-wiring`

---

## 6. DEPENDENCY UPGRADE SUMMARY (pom.xml)

| Dependency | Current | Target | Phase |
|---|---|---|---|
| JUnit 5 | — | 5.11.4 | 0 |
| Mockito | — | 5.14.2 | 0 |
| TestContainers | — | 1.20.4 | 0 |
| AssertJ | — | 3.26.3 | 0 |
| Flyway Core | — | 10.18.2 | 1 |
| Flyway Postgres | — | 10.18.2 | 1 |
| Flyway Maven Plugin | — | 10.18.2 | 1 |
| BCrypt (jbcrypt) | — | 0.4 | 2 |
| PostgreSQL driver | 42.7.3 | stay | — |
| HikariCP | 7.0.2 | stay | — |
| Logback | 1.5.34 | stay | — |
| JavaFX | 21.0.2 | stay | — |
| Maven Compiler | 3.13.0 | stay | — |

---

## 6.5 Git Workflow Rules

After each phase is fully completed and all tests pass, push the branch to the remote repository with `git push -u origin <branch-name>`.

- Do **NOT** push to `main` or `master`.
- Do **NOT** force-push (`--force`) unless I explicitly approve it.
- All branches must be merged via a Pull Request workflow (even if you are the only contributor) — this creates a professional GitHub history.

---

## 7. EXECUTION ORDER

```
Phase 0 ──► Phase 1 ──► Phase 2 ──► Phase 3 ──► Phase 4
(tests)    (schema)     (domain)    (services)  (cleanup)
```

### Why this order

1. **Phase 0:** Safety net. Cannot refactor without tests.
2. **Phase 1:** Schema comes early because the current DB schema is already broken (code queries tables that don't match `script.sql`). Fixing the schema foundation first prevents wasted work in later phases.
3. **Phase 2:** Domain models and repository interfaces are pure abstractions — they compile against the old and new worlds. No deployment risk.
4. **Phase 3:** Service implementations wire the new repositories (which query the V2 schema) behind the interfaces. Controllers become thin delegates. This is the highest-risk phase — mitigated by Phase 0 tests + Phase 1 migration validation.
5. **Phase 4:** Cleanup. No behavioral changes.

### Deployment Between Phases

| Phase | Deployable? | Mitigation |
|---|---|---|
| 0 | Yes | Only adds test dependencies. No production code change. |
| 1 | **With care** | V1 schema matches old `script.sql` exactly (idempotent). V2 runs ALTER TABLE — backward-compatible (only adds constraints, widens types). Old Java code still works with V2 schema because queries target `"user"`, `movie`, etc. which V1 created. |
| 2 | Yes | New domain models + repository interfaces are side-by-side with old code. Old controllers/DAOs unchanged. |
| 3 | **Feature-flag** | Deploy new thin controllers alongside old fat controllers. Feature-flag the new UI path. Rollback by switching flag off. |
| 4 | Yes | Deletion of dead code only. Zero behavioral change. |

---

## 8. SUMMARY

| Metric | Current State | Target (End of Phase 4) |
|---|---|---|
| Architecture | Transaction Script | Hexagonal + Pragmatic Services |
| Package structure | `Controller/`, `DAO/`, `Model/`, `View/`, `Database/` | `domain/`, `application/`, `infrastructure/`, `ui/` |
| Test coverage | 0% | >80% (unit + integration) |
| Password storage | Plaintext | BCrypt |
| Transaction safety | None | `BookingFacade` with shared connection |
| Schema management | Manual `script.sql` | Flyway (V1 + V2) |
| Schema alignment | Broken (code ≠ schema) | Fully aligned |
| ID types | Mixed `int`/`String` | Consistent `Long` |
| Controller size | 50–300 lines | 15–40 lines (thin delegation) |
| DI pattern | `new Xxx()` everywhere | Constructor injection from `Main.java` |
| Navigation | Static stack + direct instantiation | Service-delegated |
