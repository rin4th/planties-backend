-- Menggunakan Database "planties"
\c planties;

-- Tabel User
DROP TABLE IF EXISTS Users cascade;
CREATE TABLE Users(
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    fullname TEXT NOT NULL,
    profileimage TEXT NOT NULL
);

-- Tabel Role
DROP TABLE IF EXISTS Role;
CREATE TABLE IF NOT EXISTS Role (
    id VARCHAR(255) PRIMARY KEY,
    role VARCHAR(255) NOT NULL
);

-- Tabel Oxygen
DROP TABLE IF EXISTS Oxygen cascade;
CREATE TABLE IF NOT EXISTS Oxygen (
    user_id VARCHAR(255) PRIMARY KEY,
    oxygen DOUBLE,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Tabel Garden
DROP TABLE IF EXISTS Garden cascade;
CREATE TABLE IF NOT EXISTS Garden (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(255),
    photos TEXT[],
    plants TEXT[],
    user_id VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Tabel Plant
DROP TABLE IF EXISTS Plant cascade;
CREATE TABLE IF NOT EXISTS Plant (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255),
    banner VARCHAR(255),
    photo TEXT[],
    garden_id VARCHAR(255),
    user_id VARCHAR(255),
    FOREIGN KEY (garden_id) REFERENCES Garden(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Tabel Reminder
DROP TABLE IF EXISTS Reminder cascade;
CREATE TABLE IF NOT EXISTS Reminder (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(255),
    duration INT,
    garden_id VARCHAR(255),
    FOREIGN KEY (garden_id) REFERENCES Garden(id)
);


