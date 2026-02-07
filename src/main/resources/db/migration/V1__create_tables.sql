-- ============================================
-- V1__create_tables.sql
-- Academic System - Initial schema
-- ============================================

-- =========================
-- SEQUENCES
-- =========================

CREATE SEQUENCE subjects_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE professors_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE schedules_seq START WITH 1 INCREMENT BY 1;

-- =========================
-- SUBJECTS
-- =========================

CREATE TABLE subjects (
    id BIGINT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    workload INTEGER NOT NULL,
    credits INTEGER NOT NULL,
    description VARCHAR(255)
);

-- =========================
-- PROFESSORS
-- =========================

CREATE TABLE professors (
    id BIGINT PRIMARY KEY,
    registration VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    title VARCHAR(50) NOT NULL,
    department VARCHAR(100) NOT NULL
);

-- =========================
-- SCHEDULES
-- =========================

CREATE TABLE schedules (
    id BIGINT PRIMARY KEY,
    dayofweek VARCHAR(15) NOT NULL,
    starttime TIME NOT NULL,
    endtime TIME NOT NULL,
    period VARCHAR(10) NOT NULL
);
