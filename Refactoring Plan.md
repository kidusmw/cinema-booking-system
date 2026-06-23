# REFACTORING PLAN V2.0 — Cinema Booking System

## 1. CURRENT STATE (As-Is)

### Architecture: Hybrid — Old UI shell wrapping new domain core

```
src/main/java/
├── Controller/        ← accept AppContext via DI, delegate to services/repos, use ModelConverter
│                       (no more new XxxDAOimp() — all gone)
├── DAO/               ← DEAD CODE — zero imports from main sources (only characterization tests)
├── Database/          ← lazy singleton HikariCP — still exists but unused by controllers
├── Model/             ← old anemic types — still REQUIRED by all View/ files (transitional)
├── View/              ← JavaFX builders referencing old Model types
├── application/       ← AppContext.java (DI container), ModelConverter.java (bridge)
├── domain/            ← rich models + services + repository ports
├── infrastructure/    ← JDBC repos, Flyway, BCrypt, ConnectionProvider
│   └── resources/db/migration/ ← V1 + V2 Flyway migrations
└── ui/common/         ← Theme.java (consolidated UI constants)
```

### Actual Coupling (Post-DI Migration)

| Coupling | Severity | Status |
|---|---|---|
| `Controller` → `new XxxDAOimp()` | **Critical** | ✅ Eliminated — all use injected `AppContext` |
| `DAO` → `DatabaseConnection.getConnection()` (static) | **Critical** | ✅ Eliminated — DAOs are dead code in main |
| `PaymentController` → raw DAOs (no txn) | **Critical** | ⚠️ Now uses services, but still no txn boundary |
| Global mutable state (`UserSession`, `NavigationManager`) | **High** | ❌ Still present |
| Controller → Controller via `new NextController(stage,…)` | **High** | ❌ Still present |

### What's Actually Running

Runtime uses the new architecture:
- `Main.java` → creates `AppContext` (7 repos + 3 services) → passes to `WelcomeController`
- All 21 controllers accept `ctx` + old Model types for View compatibility
- `ModelConverter` bridges domain ↔ old Model at each controller ⇄ repo boundary
- `DatabaseConnection` is only hit by dead DAO code (never called in normal flow)

---

## 2. CRITICAL SMELLS (Prioritized)

### P0 — Security & Data Integrity

| # | Smell | Location | Impact | Status |
|---|---|---|---|---|
| 1 | **Plaintext passwords** | `UserDAOimp.login()` | Full credential exposure | ✅ Fixed — `AuthService` uses BCrypt |
| 2 | **No password hashing** | `UserDAOimp.addUser()` | Same as above | ✅ Fixed — `AuthService.register()` hashes |
| 3 | **No transactional boundary** | `PaymentController` — booking in one connection, payment in another | Orphan bookings on crash | ❌ Still open |
| 4 | **DB schema mismatch (code ≠ SQL)** | Old DAOs reference `User_`, `Movie_Hall_`, `Show_Table`, `Movie_` | App cannot run against provided schema | ✅ Fixed — DAOs dead, new repos target Flyway schema |
| 5 | **Hardcoded DB credentials** | `docker-compose.yml` + `script.sql` | Leaked to git | ❌ Still open |
| 6 | **T-SQL in Postgres code** | `MovieDAO.getTopRatedMovies()` | Runtime crash on Postgres | ✅ Dead code — DAO not imported |

### P1 — Architectural

| # | Smell | Location | Status |
|---|---|---|---|
| 7 | **God Controller** (206 lines) | `PaymentController` | ⚠️ Partially — uses services but still fat (OTP, countdown, nav, UI) |
| 8 | **Inconsistent DAO pattern** | Old package | ✅ Resolved — all new repos have ports + implementations |
| 9 | **Mixed ID types** | Old `Model.*` types | ✅ Domain uses Long; old Model transitional |
| 10 | **Controllers create controllers** | Every screen transition does `new NextController(stage, …)` | ❌ Still open |

### P2 — Maintainability

| # | Smell | Status |
|---|---|---|
| 11 | Typo filename: `MovieManagmentContrller.java` | ❌ Still open |
| 12 | Duplicated UI constants | ✅ Fixed — `Theme.java` |
| 13 | Dead stubs in old DAOs | ✅ Dead code |
| 14 | Zero test dependencies | ✅ Fixed — JUnit 5, Mockito, TestContainers |
| 15 | `script.sql` UTF-16LE | ❌ Still open |

---

## 3. AUDIT: script.sql Defects

*(Historical record — all schema defects addressed by Flyway V1 + V2 migrations)*

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

## 3.5 PROGRESS TRACKER

### ✅ Completed

| Phase | What | Delivered |
|---|---|---|
| 0 | Testing Foundation | JUnit 5, Mockito, TestContainers, AssertJ. 4 characterization tests + unit tests = 53 total |
| 1 | Flyway Schema | V1 (exact script.sql copy) + V2 (BIGINT, CHECK, audit cols, indexes). `FlywayMigrator` class |
| 2 | Domain Extraction | 7 rich domain models (User, Movie, Hall, Seat, Showtime, Booking, Payment, BookingSeat). 7 port interfaces. 3 domain services (AuthService, BookingService, PaymentService). ConnectionProvider + BCryptPasswordHasher |
| 3 | Repositories + DI | 7 Jdbc repos targeting V2 schema. `AppContext` (DI container). Full controller DI migration (all 21 controllers). `ModelConverter` for old↔new bridging. Password hashing cutover with legacy plaintext compat |
| 4 | Cleanup (partial) | Theme.java consolidation. Dead code stubs removed from flow. DAO/ package dead in main sources |

### ❌ Remaining

| Item | Priority | Notes |
|---|---|---|
| Transaction boundary (BookingFacade) | P0 | Last critical data-integrity hole |
| Navigation refactor (replace `new NextController()`) | P1 | Tangled graph, hard to test |
| Typo filename fix | P2 | `MovieManagmentContrller.java` |
| Hardcoded DB credentials | P0 | `docker-compose.yml`, `script.sql` |
| script.sql UTF-16LE cleanup | P2 | Convert to UTF-8 or delete |
| Remove dead DAO/ package | P2 | Tests still reference it |
| Remove dead Database/ package | P2 | `DatabaseConnection.java` unused by main |
| Remove old Model/ types (views updated) | P3 | Views must adopt new domain models first |
| Package reorganization | P3 | Move Controller/ → ui/controller/, View/ → ui/view/, Main → ui/ |

---

## 4. TARGET ARCHITECTURE (To-Be)

### Hexagonal / Clean Architecture with Pragmatic Services

```
src/main/java/
├── domain/                        ← Innermost — zero framework deps
│   ├── model/                     ← Rich domain models (behavior + data)
│   │   ├── User.java              · isAdmin(), isCustomer()
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
│   │   └── AuthService.java       · login, register
│   └── port/                      ← Outbound port interfaces
│       ├── UserRepository.java
│       ├── MovieRepository.java
│       ├── HallRepository.java
│       ├── SeatRepository.java
│       ├── ShowtimeRepository.java
│       ├── BookingRepository.java
│       └── PaymentRepository.java
│
├── application/                   ← Application layer (orchestration, bridging)
│   ├── AppContext.java            ← Manual DI container (holds all repos + services)
│   ├── ModelConverter.java        ← Bidirectional old↔new model conversion
│   └── service/                   ← Application services (txn management)
│       └── BookingFacade.java     ← Wraps BookingService + PaymentService + txn (TO DO)
│
├── infrastructure/                ← Adapter implementations
│   ├── persistence/
│   │   ├── ConnectionProvider.java
│   │   ├── DatabaseConnectionProvider.java
│   │   ├── JdbcUserRepository.java
│   │   ├── JdbcMovieRepository.java
│   │   ├── JdbcHallRepository.java
│   │   ├── JdbcSeatRepository.java
│   │   ├── JdbcShowtimeRepository.java
│   │   ├── JdbcBookingRepository.java
│   │   └── JdbcPaymentRepository.java
│   ├── security/
│   │   ├── PasswordHasher.java
│   │   └── BCryptPasswordHasher.java
│   └── migration/
│       └── FlywayMigrator.java
│
├── ui/                            ← Inbound adapters (JavaFX)
│   ├── controller/                ← (TO DO: move from Controller/)
│   ├── view/                      ← (TO DO: move from View/)
│   ├── common/
│   │   └── Theme.java             ← Single source for ACCENT, TEXT_DARK, etc.
│   └── Main.java                  ← Composition Root (TO DO: move from root)
│
├── (old packages to delete)
│   ├── Controller/                → ui/controller/
│   ├── View/                      → ui/view/
│   ├── DAO/                       → delete
│   ├── Database/                  → delete
│   └── Model/                     → delete (after views adopt new domain models)
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
| **ModelConverter as transitional bridge** | Avoids rewriting all View files in one shot. Convert at repo boundaries. |
| **Controller/View stay in top-level packages** | Moves are cosmetic (package rename, no behavioral change). Planned for Phase 5. |

---

## 5. REFACTORING STRATEGY — Execution Phases

```
Phase 0 ──► Phase 1 ──► Phase 2 ──► Phase 3 ──► Phase 4 ──► Phase 5 ──► Phase 6 ──► Phase 7
(tests)    (schema)     (domain)    (services)  (cleanup)   (reorg)    (txn)      (nav)
```

### Phase 0: Testing Foundation ✅ DONE

**Goal:** Add test infrastructure and write characterization tests before touching production code.

Delivered: JUnit 5, Mockito, TestContainers, AssertJ, Flyway deps. 53 tests total (4 characterization + 5 service + 21 domain + 3 migration + rest).

**Branch:** `refactor/phase-0-test-foundation`

---

### Phase 1: Flyway Schema Setup + Migration ✅ DONE

**Goal:** Establish Flyway, convert `script.sql` to V1, write V2 modernization.

Delivered:
- `V1__initial_schema.sql` — exact DDL from script.sql (UTF-8, no seed data)
- `V2__modernize_schema.sql` — BIGINT PKs, CHECK constraints, NOT NULL, audit columns, indexes
- `FlywayMigrator.java` — auto-migrate on startup
- `FlywayMigrationTest` — validates both migrations + idempotency

**Branch:** `refactor/phase-1-flyway-schema`

---

### Phase 2: Domain Extraction + Rich Models + Repository Interfaces ✅ DONE

**Goal:** Build domain layer with rich models, repository ports, services, ConnectionProvider, PasswordHasher.

Delivered:
- 8 domain models in `domain/model/` (with behavior)
- 7 repository interfaces in `domain/port/`
- 3 domain services in `domain/service/`
- `ConnectionProvider` + `DatabaseConnectionProvider`
- `PasswordHasher` + `BCryptPasswordHasher`

**Branch:** `refactor/phase-2-domain-models`

---

### Phase 3: Repository Implementations + Controller DI ✅ DONE

**Goal:** Write 7 Jdbc repos, wire DI, migrate all controllers away from old DAOs.

Delivered:
- 7 Jdbc repositories in `infrastructure/persistence/`
- `AppContext.java` — DI container holding all 7 repos + 3 services
- All 21 controllers accept `AppContext` via constructor
- `ModelConverter` — bidirectional conversion for all 7 entity types
- `LoginController` + `SignUpController` → `AuthService` (BCrypt, P0 security fix)
- `PaymentController` → `BookingService.createBooking()` + `PaymentService.processPayment()`
- Remaining 12 controllers → injected repo calls with `ModelConverter` conversion
- `ShowManagmentController` — raw JDBC to legacy tables replaced with repo calls
- `PaymentRepository.findAll()` added for admin dashboard
- 53 tests pass, main sources have zero `import DAO.*`

**Branch:** `refactor/phase-3-services-orchestration` → merged to `dev`

---

### Phase 4: Infrastructure + UI Polish ✅ DONE (partial)

**Goal:** Cleanup — Theme, dead stubs, System.out → SLF4J.

#### 4.1 — Consolidate UI Theme ✅
Single `ui/common/Theme.java` with all constants. Inline duplicates removed.

#### 4.2 — Logger Adoption
SLF4J loggers present in several controllers. Some `System.out.println()` remain.

#### 4.3 — Dead Stubs
Old DAO package is dead code in main sources (zero imports). Not yet deleted (tests reference it).

**Remaining:**
- Remove old DAO/ package from main sources
- Remove old Database/ package
- Fix typo filename `MovieManagmentContrller.java`
- Convert/delete `script.sql`
- Remove old Model/types after views update

---

### Phase 5: Package Reorganization 🔜 NEXT

**Goal:** Move files to match the target architecture layout. This is purely cosmetic — no behavioral changes. All imports are updated mechanically.

#### 5.1 — Move Controller/ → ui/controller/

**Files to move (21):**
```
Controller/WelcomeController.java          → ui/controller/WelcomeController.java
Controller/AuthChoiceController.java       → ui/controller/AuthChoiceController.java
Controller/UsertypeController.java         → ui/controller/UsertypeController.java
Controller/LoginController.java            → ui/controller/LoginController.java
Controller/SignUpController.java           → ui/controller/SignUpController.java
Controller/AdminDashboardController.java   → ui/controller/AdminDashboardController.java
Controller/MovieManagementController.java  → ui/controller/MovieManagementController.java
Controller/ShowManagmentController.java    → ui/controller/ShowManagmentController.java
Controller/MoviehallManagmentController.java → ui/controller/MoviehallManagmentController.java
Controller/SeatManagmentController.java    → ui/controller/SeatManagmentController.java
Controller/BookingManagmentController.java → ui/controller/BookingManagmentController.java
Controller/PaymentManagmentController.java → ui/controller/PaymentManagmentController.java
Controller/UserManagmentController.java    → ui/controller/UserManagmentController.java
Controller/CustomerDashboardController.java → ui/controller/CustomerDashboardController.java
Controller/MovieBrowserController.java     → ui/controller/MovieBrowserController.java
Controller/ShowSelectionController.java    → ui/controller/ShowSelectionController.java
Controller/MovieHallSelectionController.java → ui/controller/MovieHallSelectionController.java
Controller/SeatSelectionController.java    → ui/controller/SeatSelectionController.java
Controller/PaymentController.java          → ui/controller/PaymentController.java
Controller/TicketController.java           → ui/controller/TicketController.java
Controller/NavigationManager.java          → ui/controller/NavigationManager.java
```

**Changes needed per file:**
- `package Controller;` → `package ui.controller;`
- Any cross-references to other controllers must use the new package in `import` statements
- `import View.Xxx` → `import ui.view.Xxx` (after View/ is moved)

#### 5.2 — Move View/ → ui/view/

**Files to move (~21):**
Same pattern — `package View;` → `package ui.view;`, update imports.

#### 5.3 — Move Main.java → ui/Main.java

**Changes:**
- `package ui;`
- Update `pom.xml` javafx-maven-plugin `mainClass` to `ui.Main`
- Update references (none, it's the entry point)

#### 5.4 — Delete dead packages

| Package | Reason | Risk |
|---|---|---|
| `DAO/` | Zero imports from main sources | Low — characterization tests reference DAO classes directly by fully qualified name or via `import DAO.*`. Tests must be updated to use new repo paths, or the DAO package stays in test scope only |
| `Database/` | `DatabaseConnection.java` unused by main sources | Low — same as DAO; tests may reference it |
| `Model/` | **Views still reference these.** Can only delete after Phase 5.5 | High — will break compile |

#### 5.5 — Update Views to use domain.models (optional, large effort)

Instead of converting old→new on every controller call, update the View files to reference `domain.model.*` directly. This lets us delete the `Model/` package entirely.

**Mechanical change:** Each `import Model.Movie;` → `import domain.model.Movie;`, update getter calls (`getMovieID()` → `getMovieId()`, etc.). This touches every View file and every Controller that passes models to views.

**Effort estimate:** ~20 View files × ~5-10 getter renames each. 1-2 hours of mechanical work.

#### 5.6 — Fix typo filename

`MovieManagmentContrller.java` → `MovieManagementController.java`
Update all references in imports and usages.

#### 5.7 — Convert/delete script.sql

Either convert from UTF-16LE to UTF-8 and keep as reference, or delete it (Flyway migrations are source of truth).

#### 5.8 — Testing strategy

- `mvn clean compile` — must succeed with zero warnings
- All 53 tests must pass
- Manual: `mvn javafx:run` — app must start, all screens render

**Breaking change?** YES — all imports change. Controllers in new package. Full regression test required.

**Branch:** `refactor/phase-5-reorg`

---

### Phase 6: Transaction Safety (BookingFacade) 🔜 NEXT

**Goal:** Eliminate orphan bookings when payment fails after booking succeeds.

#### The Problem

`PaymentController` calls:
```java
ctx.bookingService.createBooking(...);   // opens connection A, inserts booking
ctx.paymentService.processPayment(...);  // opens connection B, inserts payment
```

If connection B fails after connection A commits, there's an orphan booking with no payment.

#### The Fix: BookingFacade

**New file:** `application/service/BookingFacade.java`

Shares a single JDBC connection across both operations and commits/rolls back atomically:

```java
public class BookingFacade {
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final ConnectionProvider connectionProvider;

    public BookingResult createBookingWithPayment(Long userId, Long showId,
            List<Long> seatIds, String paymentMethod) {
        Connection conn = connectionProvider.getConnection();
        try {
            conn.setAutoCommit(false);
            // pass the shared connection to both services (or make repos accept a connection arg)
            Booking booking = bookingService.createBooking(userId, showId, seatIds, ...);
            Payment payment = paymentService.processPayment(booking.getBookingId(), ...);
            conn.commit();
            return new BookingResult(booking, payment);
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}
```

**Challenges:**
- Current `BookingService` and `PaymentService` don't accept a connection parameter
- Repositories get connections from `ConnectionProvider` internally
- Options:
  a. Add `Connection` parameter to service/repo methods (invasive)
  b. Add `beginTransaction()` / `commit()` / `rollback()` to `ConnectionProvider` (simpler)
  c. Create a `TransactionTemplate` callback pattern (cleanest but more code)

#### Testing strategy:
- TestContainers test: call `BookingFacade.createBookingWithPayment()`, verify both booking and payment rows exist
- Simulate payment failure: verify booking is rolled back

**Breaking change?** NO — new class added, existing code only needs to use it instead of calling services separately. Old call path still works (no transaction safety).

**Branch:** `refactor/phase-6-txn`

---

### Phase 7: Navigation Refactor 🔜 NEXT

**Goal:** Replace `new NextController(stage, ctx, ...)` chaining with a screen navigation service.

#### The Problem

Every screen transition:
```java
view.btnBack.setOnAction(e -> new CustomerDashboardController(stage, ctx, currentUser));
view.movieCard.setOnMouseClicked(e -> new ShowSelectionController(stage, ctx, currentUser, movie));
```

This creates tight coupling between controllers and makes it impossible to test navigation logic.

#### The Fix: NavigationService

**New file:** `ui/controller/NavigationService.java` (or integrate into existing `NavigationManager`)

```java
public class NavigationService {
    private final Stage stage;
    private final AppContext ctx;
    private final Deque<BiFunction<Stage, AppContext, ?>> history = new ArrayDeque<>();

    public void navigateTo(BiFunction<Stage, AppContext, Object> screenFactory) {
        history.push(screenFactory);
        screenFactory.apply(stage, ctx);
    }

    public void back() {
        history.pop();
        if (!history.isEmpty()) {
            history.peek().apply(stage, ctx);
        }
    }

    // Named navigation methods
    public void toWelcome() { navigateTo(WelcomeController::new); }
    public void toLogin() { navigateTo(LoginController::new); }
    public void toDashboard(Customer customer) {
        navigateTo((stage, ctx) -> new CustomerDashboardController(stage, ctx, customer));
    }
}
```

Controllers would accept `NavigationService` instead of `Stage` directly. The pattern becomes:
```java
// Before:
new ShowSelectionController(stage, ctx, currentUser, movie);

// After:
nav.toShowSelection(currentUser, movie);
```

#### Testing strategy:
- Unit test `NavigationService` with mock screen factories
- Verify back-stack behavior (push, pop, empty)
- Controllers become testable with mocked navigation

**Breaking change?** YES — every controller constructor changes (add `NavigationService` param). All 21 call sites updated.

**Branch:** `refactor/phase-7-nav`

---

## 6. DEPENDENCY UPGRADE SUMMARY (pom.xml)

| Dependency | Current | Target | Phase |
|---|---|---|---|
| JUnit 5 | 5.11.4 | stay | 0 |
| Mockito | 5.14.2 | stay | 0 |
| TestContainers | 1.20.4 | stay | 0 |
| AssertJ | 3.26.3 | stay | 0 |
| Flyway Core | 10.18.2 | stay | 1 |
| Flyway Postgres | 10.18.2 | stay | 1 |
| Flyway Maven Plugin | 10.18.2 | stay | 1 |
| BCrypt (jbcrypt) | 0.4 | stay | 2 |
| PostgreSQL driver | 42.7.3 | stay | — |
| HikariCP | 7.0.2 | stay | — |
| Logback | 1.5.34 | stay | — |
| JavaFX | 21.0.2 | stay | — |
| Maven Compiler | 3.13.0 | stay | — |

---

## 6.5 Git Workflow Rules

After each phase is fully completed and all tests pass:
1. Create feature branch from `dev`: `git checkout dev && git checkout -b refactor/phase-N-desc`
2. Make changes, commit with descriptive messages
3. Run full test suite: `mvn clean test`
4. Merge back to `dev` with `--no-ff`: `git checkout dev && git merge --no-ff refactor/phase-N-desc`
5. Delete feature branch: `git branch -d refactor/phase-N-desc`

- Do **NOT** push to `main` or `master`.
- Do **NOT** force-push (`--force`) without explicit approval.
- Only push `master` to remote when stable.

---

## 7. EXECUTION ORDER

```
Phase 0 ──► Phase 1 ──► Phase 2 ──► Phase 3 ──► Phase 4 ──► Phase 5 ──► Phase 6 ──► Phase 7
(tests)    (schema)     (domain)    (services)  (cleanup)   (reorg)    (txn)      (nav)
```

### Why this order

1. **Phase 0–4:** Completed. See Section 3.5 for details.
2. **Phase 5 (Reorg):** Next because it's pure mechanical work — no logic changes. Gets the project structure matching the target architecture before any more complex work.
3. **Phase 6 (Txn):** Critical data-integrity fix. Must come after reorg so the BookingFacade goes in the right package.
4. **Phase 7 (Nav):** Largest API change — saves the best for last, after everything else is stable.

### Deployment Risk

| Phase | Deployable? | Risk |
|---|---|---|
| 0–4 | ✅ | All tested, merged to dev |
| 5 | ✅ | Pure file moves + import changes. Full recompile + regression test |
| 6 | ⚠️ Moderate | New BookingFacade — no existing code changes, but txn rollback could mask bugs |
| 7 | ⚠️ Moderate | All controller constructors change — must test every screen transition |

---

## 8. SUMMARY

| Metric | Initial (Pre-Phase 0) | Current (Post-Phase 3) | Target (End of Phase 7) |
|---|---|---|---|
| Architecture | Transaction Script | Hybrid (old UI + new core) | Hexagonal + Pragmatic Services |
| Package structure | `Controller/`, `DAO/`, `Model/`, `View/`, `Database/` | Same + `domain/`, `application/`, `infrastructure/`, `ui/common/` | `domain/`, `application/`, `infrastructure/`, `ui/` (old pkgs deleted) |
| Test coverage | 0% | ~40% | >80% (unit + integration) |
| Password storage | Plaintext | BCrypt | BCrypt |
| Transaction safety | None | None (services called separately) | `BookingFacade` with shared connection |
| Schema management | Manual `script.sql` | Flyway (V1 + V2) | Flyway (V1 + V2) |
| Schema alignment | Broken (code ≠ schema) | Fully aligned (DAOs dead, new repos target Flyway) | Fully aligned |
| ID types | Mixed `int`/`String` | Domain: Long. Views: old mixed types (bridged by ModelConverter) | Consistent `Long` everywhere |
| Controller size | 50–300 lines | 50–300 lines (still fat, esp. PaymentController) | 15–40 lines (thin delegation to services) |
| DI pattern | `new Xxx()` everywhere | Constructor injection via `AppContext` | Constructor injection via `AppContext` |
| Navigation | Static stack + direct instantiation | Same (still `new NextController()`) | `NavigationService` (testable) |
