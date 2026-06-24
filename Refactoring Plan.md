# REFACTORING PLAN — Cinema Booking System (Consolidated)

## Progress Tracker

| Phase | Status |
|---|---|
| Phase 0: Testing Foundation | ✅ |
| Phase 1: Flyway Schema | ✅ |
| Phase 2: Domain Extraction | ✅ |
| Phase 3: Repository + DI | ✅ |
| Phase 4: Infrastructure & Tooling | ✅ |
| Phase 5: Architecture Polish | ✅ |
| Phase 6: UI/UX Polish | ✅ |
| Phase 7: Seed Data & Demo Readiness | ✅ (code), ⬜ (branch merge, final verification) |

**Test count**: 48 @Test methods across 5 test files (DomainModelTest, AuthServiceTest, BookingServiceTest, PaymentServiceTest, FlywayMigrationTest).

**CI pipeline** (`make ci`): `mvn clean verify spotless:check spotbugs:check`. PMD is excluded from CI due to noise; run `mvn pmd:check` manually.

---

## Phase 0: Testing Foundation ✅

**Branch:** `chore/test-framework` → merged to `dev`

**Context:** Original code had zero tests — no safety net for refactoring. Needed to characterize existing behavior before touching anything.

**What was done:**
- Bootstrapped JUnit 5, Mockito, TestFX, AssertJ, H2 (in-memory DB for Flyway)
- Wrote **48 characterization tests** across 5 files to lock down domain models, auth, booking, payment, and Flyway migration
- Verified all tests pass with `mvn verify`

**Key decisions:**
- H2 over Testcontainers (no Docker dependency for tests)
- TestFX for UI-layer testing (not yet used in current suite)
- Initial 53-test suite was pruned to 48 when redundant characterization tests were removed after refactoring stabilized

**Key files:** `pom.xml` (surefire + dependencies), `src/test/java/domain/model/DomainModelTest.java`, `src/test/java/domain/service/AuthServiceTest.java`, `src/test/java/domain/service/BookingServiceTest.java`, `src/test/java/domain/service/PaymentServiceTest.java`, `src/test/java/infrastructure/persistence/FlywayMigrationTest.java`

**Delivery:** 48 tests pass, zero infrastructure dependencies for testing.

---

## Phase 1: Flyway Schema ✅

**Branch:** `chore/flyway-migration` → merged to `dev`

**Context:** Original `script.sql` dumped all tables in one file with no versioning. Needed migration-based schema management.

**What was done:**
- Created `V1__initial_schema.sql` — full original schema (users, movies, halls, shows, seats, bookings, payments, tickets)
- Created `V2__modernize_schema.sql` — BIGINT PKs, CHECK constraints, NOT NULL, audit columns (`created_at`, `updated_at`), indexes
- Configured Flyway in persistence layer, initialized via `SessionFactory` on startup
- Removed `script.sql`; schema now managed entirely through migrations

**Key decisions:**
- Two-step migration (V1 = original, V2 = clean) rather than one monolithic V1
- `hibernate.hbm2ddl.auto=validate` to ensure code matches schema at runtime

**Key files:** `src/main/resources/db/migration/V1__initial_schema.sql`, `V2__modernize_schema.sql`, `src/main/java/infrastructure/persistence/HibernateUtil.java`, `pom.xml` (flyway-core, flyway-maven-plugin)

**Delivery:** Schema versioned, validated on startup, all original defects (BIGINT, CHECK, audit) fixed.

---

## Phase 2: Domain Extraction ✅

**Branch:** `refactor/domain-extraction` → merged to `dev`

**Context:** All domain logic was embedded in UI controllers. Business rules (price calculation, seat validation, booking constraints) were scattered and untestable.

**What was done:**
- Extracted domain model POJOs (`User`, `Movie`, `MovieHall`, `Show`, `Seat`, `Booking`, `Payment`, `Ticket`, `SeatStatus`)
- Built domain services (`AuthService`, `BookingService`, `PaymentService`, `SeatService`, `ShowService`, `MovieService`, `UserService`, `TicketService`)
- Moved all business logic out of controllers into services
- Made domain package dependency-free (no UI, no infrastructure imports)

**Key decisions:**
- Domain module has zero outward dependencies — pure business logic
- Services are stateless; state lives in models
- No anemic domain model — services encapsulate operations

**Key files:** `src/main/java/domain/model/*.java`, `src/main/java/domain/service/*.java`

**Delivery:** All business logic extracted into testable domain services and models.

---

## Phase 3: Repository + DI ✅

**Branch:** `refactor/repository-di` → merged to `dev`

**Context:** Controllers were creating their own DB sessions and running raw HQL — no separation of persistence concerns.

**What was done:**
- Created repository interfaces per aggregate (`UserRepository`, `MovieRepository`, `ShowRepository`, `SeatRepository`, `BookingRepository`, `PaymentRepository`, `TicketRepository`)
- Implemented with Hibernate `Session` via `PersistenceContext` (thread-local Session, per-request open/close)
- Wired repositories into services, services into controllers via manual constructor injection
- Each controller now receives all dependencies through its constructor
- All 48 tests pass

**Key decisions:**
- Manual DI (no Spring/Guice) — keeps the project lightweight
- `PersistenceContext` as thin Session management wrapper
- Constructor injection for testability

**Key files:** `src/main/java/infrastructure/persistence/PersistenceContext.java`, `src/main/java/infrastructure/persistence/repository/*.java`, all controllers

**Delivery:** 7 repository implementations, zero DAO imports in main sources, all controllers constructor-injected.

---

## Phase 4: Infrastructure & Tooling ✅

**Branches:** `cleanup/warnings`, `Merge phase 5` (linting), `refactor/phase-6-reorg` → merged to `dev`

**Context:** Project had compiler warnings, no formatting standard, no linting, dead packages, unused code, and manual cleanup patterns. Needed professional tooling and a clean project structure.

### 4.1 — Compiler Warning Fixes

- Fixed all `-Xlint:all` warnings: unchecked casts, missing serialVersionUID, unused imports, dead code paths

### 4.2 — Linting & Formatting

| Tool | Purpose | In `make ci`? |
|---|---|---|
| **Spotless** (Google AOSP style) | Auto-format code, remove unused imports | ✅ |
| **SpotBugs 7.0.15** | Bug/security analysis (max effort) | ✅ |
| **PMD 7.3.0** (upgraded from 3.21.0) | Code quality checks (bestpractices + errorprone + design) | ❌ (manual only — `mvn pmd:check`) |

SpotBugs fixes:
- `NP_ALWAYS_NULL` — removed dead null-check in `PaymentManagmentController.handleBack()`
- `DMI_RANDOM_USED_ONLY_ONCE` — promoted `Random` to static field in `PaymentController`
- `UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD` — removed unused `lblTotalPayments` / `lblPendingPayments`
- `URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD` — removed unused `seatContainer` in `SeatSelectionPage`

### 4.3 — Logger Adoption

Replaced `System.out.println` / `printStackTrace` with `java.util.logging.Logger` in all controllers and services. Consistent log levels: FINE for flow, WARNING for recoverable issues, SEVERE for failures.

### 4.4 — Package Reorganization + Dead Code Removal

- Grouped UI classes by role: `ui/controller/admin/`, `ui/controller/customer/`, `ui/controller/common/`, mirrored view structure
- Removed redundant `ui/model` package and `ModelConverter` (models live in `domain/model/`)
- Moved image assets to `src/main/resources/images/`
- Removed stale `output_run.txt` from version control; added to `.gitignore`
- pom.xml mainClass updated to `ui.Main`

### 4.5 — Makefile + Docker Automation

- `Makefile` with `ci`, `run`, `clean`, `db-up`, `db-down` targets
- `run-app.sh`: starts Docker DB → traps exit → runs `mvn javafx:run` → auto-stops DB
- Surefire `argLine` configured with `--add-opens`, `-XX:+EnableDynamicAgentLoading` (Mockito agent fix)

**Key files:** `Makefile`, `run-app.sh`, `pom.xml`, `.gitignore`, `spotbugs-exclude.xml`

### 4.6 — Theme Consolidation (CSS Centralization)

- Moved color constants from `Theme.java` into `src/main/resources/css/styles.css` as CSS custom properties
- Replaced 147 inline `.setStyle()` / `.setFont()` calls across all 15 controllers with CSS class references
- Result: all styling lives in one CSS file (85 classes, ~600 lines); Java code controls logic only

---

## Phase 5: Architecture Polish ✅

**Branch:** `refactor/booking-facade` + `refactor/navigation-manager` → both merged to `dev`

**Context:** Transactions were not atomic (booking could partially fail leaving orphaned payments) and navigation was scattered — each controller directly created the next page.

### 5.1 — Transaction Safety (BookingFacade)

- Extracted `BookingFacade` service that wraps booking + payment + ticket generation in a single Hibernate transaction
- If any step fails (payment declined, seat already taken), the entire transaction rolls back
- Controllers now call one `bookingFacade.createBooking(...)` instead of orchestrating manually

**Key file:** `src/main/java/domain/service/BookingFacade.java`

### 5.2 — Navigation Refactor (NavigationManager)

- Created `NavigationManager` with `go(...)`, `goFresh(...)`, and `back(...)` methods
  - `go(...)` — push current page onto back-stack, load new page
  - `goFresh(...)` — clear entire back-stack, load new page (for logout, fresh start)
  - `back(...)` — pop back-stack to previous page
- All controllers navigate through `NavigationManager` instead of direct page construction
- Back-stack enables browser-like navigation behavior

**Key file:** `src/main/java/ui/common/NavigationManager.java`

---

## Phase 6: UI/UX Polish ✅

**Branch:** `ui/css-styling` (not yet merged to `dev`)

**Context:** The app worked but looked rough — inconsistent window sizes, no sidebar on customer screens, no design system, broken table selection, scattered navigation logic.

### 6.1 — Window Manager

- `WindowManager.java` — single source of truth for all Stage/window configuration
- `.configure(stage, pageTitle)` sets: title, icon, size (1200×800), min/max dimensions, center-on-screen
- All 15 controllers now call `WindowManager.configure(...)` on page load — eliminates 40+ duplicated lines of Stage boilerplate
- All screens now render at consistent size and are centered

**Key file:** `src/main/java/ui/common/WindowManager.java`

### 6.2 — Design System Expansion

- **`Theme.java`**: extended with font constants (`FONT_FAMILY`, `S_LOGIN`, `S_HEADING`, `S_BODY`) and spacing constants (`P_SM`, `P_MD`, `P_LG`, `RADIUS`)
- **`UITheme.java`**: convenience methods like `.applyStyle(region, "panel")`, `.headingStyle(...)`, `.bodyStyle(...)`
- Replaced remaining `.setFont(...)` calls with CSS class references — only 5 intentional one-offs remain
- `styles.css` grew from 50→85 CSS class selectors (~600 lines). Key additions: `.heading`, `.label-text`, `.link`, `.panel`, `.form-grid`, `.table-view`, `.sidebar`, `.sidebar-btn`, `.sidebar-btn:hover`, `.sidebar-btn.selected`, `.navbar`, `.status-bar`, `.btn-primary`, `.btn-danger`, `.btn-small`, `.card`, `.card:hover`, input validation states (`.error`, `.success`, `.warning`)

### 6.3 — Reusable Sidebar

- Extracted shared sidebar into **`Sidebar.java`** — a `VBox` with nav buttons, active-state highlighting (`.selected` CSS class), and logout
- Both `AdminDashboardController` and `CustomerDashboardController` now instantiate `Sidebar` instead of duplicating sidebar HTML-in-Java
- Customer dashboard now has a proper sidebar (previously only had a top bar)
- Sidebar highlight corrects when navigating via back-stack

### 6.4 — Table View Fixes

- Fixed 9 broken `PropertyValueFactory` references across all 7 TableViews:

| File | Column | Broken | Fix |
|---|---|---|---|
| `BookingManagmentPage` | Booking ID | `"bookingID"` | `"bookingId"` |
| `BookingManagmentPage` | User | `"customerName"` (no such field) | Callback: `"User #" + getUserId()` |
| `BookingManagmentPage` | Show ID | `"showID"` | `"showId"` |
| `MovieHallManagmentPage` | Hall ID | `"MovieHallID"` | `"hallId"` |
| `PaymentManagment` | Payment ID | `"paymentID"` | `"paymentId"` |
| `PaymentManagment` | Verification Code | `"verificationCode"` (no such field) | → "Method" → `"paymentMethod"` |
| `PaymentManagment` | Booking ID | `"bookingID"` | `"bookingId"` |
| `UserManagmentPage` | Status | `"loginStatus"` (no such field) | → "Role" → `"role"` |
| `SeatManagmentPage` | 3 columns | No cellValueFactory at all | `"seatId"`, `"seatNumber"`, `"seatType"` |

- Added `.getSelectionModel().setSelectionMode(SelectionMode.SINGLE)` to all 7 tables
- Added `selectedItemProperty().addListener(...)` to enable/disable edit/delete buttons based on selection

### 6.5 — Navigation & UX Fixes

| Issue | File(s) | Fix |
|---|---|---|
| `MoviehallManagmentController` creates redundant Scene | `MoviehallManagmentController.java` | Remove lines that `new Scene(...)` + `stage.setScene(...)` since embedded via `injectView()` |
| Sidebar highlight not updated after back | `AdminDashboardController.java` | Add `setActiveMenu(btnDashboard)` in `showDashboard()` |
| `UserTypePage` both buttons say "Continue as User" | `UserTypePage.java:42` | Change admin button text to "Continue as Admin" |

### Known Issues After Phase 6

| Issue | Severity |
|---|---|
| `TicketController` receives empty `bookingIds` — backend OTP flow incomplete | ⚠️ Low |
| `BookingManagmentPage` "User" column uses callback `"User #" + userId` (no `customerName` in Booking model) | ⚠️ Low |

---

## Phase 7: Seed Data & Demo Readiness ✅ (code) / ⬜ (branch merge, final verification)

**Context:** For demo purposes, the database starts empty (no customers, no movies, no shows). The app needs realistic demo data to showcase all features.

### 7.1 — V3 Seed Migration (`V3__seed_test_data.sql`)

- 3 users: admin (BCrypt-hashed password), customer, second customer
- 5 movies (including upcoming showing so the "now showing" filter works)
- 2 cinema halls
- Shows spanning past and future dates (demonstrates filtering)
- Pre-seeded seat grid (40 seats per hall, mix of available/booked)
- Several completed bookings with associated payments and tickets
- `ticket_price` column correctly populated on each show
- Resolves all `script.sql` defects: no duplicate users, no plaintext passwords, no missing `ticket_price`

### 7.2 — Final CI Pipeline

- `make ci` confirmed green: `mvn clean verify spotless:check spotbugs:check`
- All 48 tests pass
- `make run` / `run-app.sh` start DB + app in one command
- Docker auto-stops on app exit (via `trap` in `run-app.sh`)

**Key file:** `src/main/resources/db/migration/V3__seed_test_data.sql`

**Remaining:** Merge Phase 6 → dev, create phase-7 branch, run final `make ci`, merge to master.

---

## Git Workflow

```
                    ┌──────────┐
                    │  master  │ ← production-ready, tag on release
                    └────┬─────┘
                         │ merge (--no-ff)
                    ┌────┴─────┐
                    │   dev    │ ← integration branch
                    └────┬─────┘
                         │ merge (--no-ff)
              ┌──────────┼──────────┐
              │          │          │
        ┌─────┴───┐ ┌───┴────┐ ┌───┴────┐
        │ phase-6 │ │phase-7 │ │  fix/*  │
        └─────────┘ └────────┘ └────────┘
```

- Feature/phase work on topic branches
- Merge to `dev` via `--no-ff` for reviewable history
- `dev` → `master` when `make ci` is green and demo-ready
- `make ci` = `mvn clean verify spotless:check spotbugs:check` (no PMD)
- `make run` starts the app; `run-app.sh` starts DB + app + auto-cleanup

---

## Dependency Summary

| Dependency | Version | Purpose |
|---|---|---|
| JUnit 5 | 5.11.4 | Test framework |
| Mockito | 5.14.2 | Test mocking |
| TestContainers | 1.20.4 | Integration test DB (not used in CI) |
| AssertJ | 3.26.3 | Fluent test assertions |
| Flyway Core | 10.18.2 | Schema migration |
| BCrypt (jbcrypt) | 0.4 | Password hashing |
| PostgreSQL driver | 42.7.3 | DB connectivity |
| HikariCP | 7.0.2 | Connection pooling |
| Logback | 1.5.34 | Logging |
| JavaFX | 21.0.2 | UI framework |
| Spotless Maven | 2.43.0 | Auto-formatting |
| SpotBugs Maven | 4.8.6.2 | Static analysis |
| PMD Maven | 3.25.0 | Code quality (manual only) |

---

## Summary

| Metric | Initial (Pre-Phase 0) | Current (Post-Phase 7) |
|---|---|---|
| Architecture | Transaction Script | Hexagonal + Pragmatic Services |
| Package structure | `Controller/`, `DAO/`, `Model/`, `View/`, `Database/` | `domain/`, `application/`, `infrastructure/`, `ui/` |
| Test coverage | 0% | 48 tests (5 files) |
| Password storage | Plaintext | BCrypt |
| Transaction safety | None | `BookingFacade` with atomic txn |
| Schema management | Manual `script.sql` | Flyway (V1 + V2 + V3) |
| Inline styling (`setStyle`) | ~147 calls | 0 |
| Inline fonts (`setFont`/`setTextFill`) | ~223 calls | 5 (intentional) |
| CSS classes | None | 85 classes, ~600 lines |
| Window management | 8 unique sizes, no centering | `WindowManager` — centralized, centered, fixed 1200×800 |
| Sidebar | Admin only, inline | Reusable `Sidebar.java` in Admin + Customer |
| Table SelectionMode.SINGLE | Not set | 7/7 tables |
| Table PropertyValueFactory | 9 broken refs | 0 broken refs |
| Navigation | Static stack + direct instantiation | Instance-based `NavigationManager` |
| CI pipeline | None | `make ci` (verify + spotless + spotbugs) |
| Demo data | None | V3 seed (3 users, 5 movies, 2 halls, past/future shows) |
| Docker automation | None | `run-app.sh` — auto-start/stop DB |
