-- Legacy schema matching the current DAO layer.
-- Some DAOs query OLD table names (User_, Movie_, Movie_Hall_, Show_Table),
-- while others (BookingDAO) query MODERN names (booking, "user").
-- This reflects the real hybrid state of the codebase.

-- "user" table — used by BookingDAO (joins with "user")
CREATE TABLE IF NOT EXISTS "user" (
    user_id       SERIAL PRIMARY KEY,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    email         VARCHAR(100),
    phone         VARCHAR(20),
    username      VARCHAR(100) UNIQUE NOT NULL,
    password      VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL DEFAULT 'customer',
    created_at    TIMESTAMPTZ DEFAULT NOW()
);

-- User_ table — used by UserDAOimp (legacy name)
CREATE TABLE IF NOT EXISTS User_ (
    User_ID       SERIAL PRIMARY KEY,
    First_N       VARCHAR(50),
    Last_N        VARCHAR(50),
    Email         VARCHAR(100),
    Phone         VARCHAR(20),
    user_Role     VARCHAR(20) DEFAULT 'customer',
    Username      VARCHAR(100) UNIQUE NOT NULL,
    user_Password VARCHAR(255) NOT NULL
);

-- Movie_ table — used by MovieDAO
CREATE TABLE IF NOT EXISTS Movie_ (
    Movie_ID      SERIAL PRIMARY KEY,
    Title         VARCHAR(200) NOT NULL,
    Genre         VARCHAR(100),
    Duration      INT,
    Rating        DECIMAL(3,1),
    Poster_Path   VARCHAR(255),
    M_language    VARCHAR(50) DEFAULT 'English',
    releaseDate   DATE,
    M_description TEXT
);

-- Movie_Hall_ table — used by MovieHallDAO
CREATE TABLE IF NOT EXISTS Movie_Hall_ (
    Movie_hall_ID SERIAL PRIMARY KEY,
    Name          VARCHAR(100) NOT NULL,
    Capacity      INT NOT NULL
);

-- Show_Table — used by ShowDAOimp
CREATE TABLE IF NOT EXISTS Show_Table (
    Show_ID       SERIAL PRIMARY KEY,
    Show_date     DATE NOT NULL,
    Show_time     TIME NOT NULL,
    Movie_ID      INT NOT NULL,
    Movie_hall_ID INT NOT NULL
);

-- Seat table — used by SeatDAOimp
CREATE TABLE IF NOT EXISTS Seat (
    Seat_ID       SERIAL PRIMARY KEY,
    Seat_number   VARCHAR(10) NOT NULL,
    Seat_Type     VARCHAR(30) DEFAULT 'regular',
    Status        VARCHAR(20) DEFAULT 'available',
    Movie_hall_ID INT NOT NULL
);

-- booking table — used by BookingDAO
CREATE TABLE IF NOT EXISTS booking (
    booking_id    SERIAL PRIMARY KEY,
    booking_date  DATE,
    user_id       INT,
    show_id       INT,
    movie_name    VARCHAR(200),
    status        VARCHAR(50) DEFAULT 'pending'
);

-- Payment table — used by PaymentDAOimp
CREATE TABLE IF NOT EXISTS Payment (
    PaymentID     SERIAL PRIMARY KEY,
    Status        VARCHAR(20) DEFAULT 'pending',
    TotalAmount   DECIMAL(10,2) NOT NULL,
    OTP           VARCHAR(20),
    BookingID     INT UNIQUE
);
