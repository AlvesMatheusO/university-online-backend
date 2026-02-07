-- ============================================
-- V1__create_initial_schema.sql
-- Academic Enrollment System - Initial Database Schema
-- ============================================
-- Description: Creates base tables and sequences for the academic system
-- ============================================

-- =========================
-- SEQUENCES
-- =========================

CREATE SEQUENCE subjects_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE professors_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE schedules_seq START WITH 1 INCREMENT BY 50;

-- =========================
-- TABLE: subjects
-- Stores academic subjects/disciplines
-- =========================

CREATE TABLE subjects (
    id BIGINT PRIMARY KEY DEFAULT nextval('subjects_seq'),
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    workload INTEGER NOT NULL,
    credits INTEGER NOT NULL,
    description VARCHAR(255),
    
    CONSTRAINT subjects_workload_positive CHECK (workload > 0),
    CONSTRAINT subjects_credits_positive CHECK (credits > 0)
);

COMMENT ON TABLE subjects IS 'Academic subjects and disciplines';
COMMENT ON COLUMN subjects.code IS 'Unique subject code (e.g., MAT001)';
COMMENT ON COLUMN subjects.workload IS 'Total hours of study required';
COMMENT ON COLUMN subjects.credits IS 'Academic credits earned';

-- =========================
-- TABLE: professors
-- Stores professor/instructor information
-- =========================

CREATE TABLE professors (
    id BIGINT PRIMARY KEY DEFAULT nextval('professors_seq'),
    registration VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    title VARCHAR(50) NOT NULL,
    department VARCHAR(100),
    
    CONSTRAINT professors_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
);

COMMENT ON TABLE professors IS 'Professor and instructor records';
COMMENT ON COLUMN professors.registration IS 'Unique registration number (YYXXXXX format)';
COMMENT ON COLUMN professors.title IS 'Academic title (Doutor, Mestre, Especialista)';

-- =========================
-- TABLE: schedules
-- Stores available class time slots
-- =========================

CREATE TABLE schedules (
    id BIGINT PRIMARY KEY DEFAULT nextval('schedules_seq'),
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    period VARCHAR(10) NOT NULL,
    
    CONSTRAINT schedules_valid_period CHECK (period IN ('MANHA', 'TARDE', 'NOITE')),
    CONSTRAINT schedules_valid_day CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    CONSTRAINT schedules_end_after_start CHECK (end_time > start_time)
);

COMMENT ON TABLE schedules IS 'Available time slots for classes';
COMMENT ON COLUMN schedules.day_of_week IS 'Day of week in English (MONDAY, TUESDAY, etc)';
COMMENT ON COLUMN schedules.period IS 'Period of day (MANHA, TARDE, NOITE)';

-- =========================
-- INDEXES
-- =========================

CREATE INDEX idx_subjects_code ON subjects(code);
CREATE INDEX idx_professors_registration ON professors(registration);
CREATE INDEX idx_professors_email ON professors(email);
CREATE INDEX idx_schedules_day_period ON schedules(day_of_week, period);

-- ============================================
-- END OF V1
-- ============================================