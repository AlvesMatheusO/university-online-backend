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

CREATE SEQUENCE courses_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE coordinators_seq START WITH 1 INCREMENT BY 50;
-- =========================
-- TABLE: subjects
-- Stores academic subjects/disciplines
-- =========================

CREATE TABLE subjects (
    id BIGINT PRIMARY KEY DEFAULT nextval ('subjects_seq'),
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
    id BIGINT PRIMARY KEY DEFAULT nextval ('professors_seq'),
    registration VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    title VARCHAR(50) NOT NULL,
    department VARCHAR(100),
    CONSTRAINT professors_email_format CHECK (
        email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$'
    )
);

COMMENT ON TABLE professors IS 'Professor and instructor records';

COMMENT ON COLUMN professors.registration IS 'Unique registration number (YYXXXXX format)';

COMMENT ON COLUMN professors.title IS 'Academic title (Doutor, Mestre, Especialista)';

-- =========================
-- TABLE: schedules
-- Stores available class time slots
-- =========================

CREATE TABLE schedules (
    id BIGINT PRIMARY KEY DEFAULT nextval ('schedules_seq'),
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    period VARCHAR(10) NOT NULL,
    CONSTRAINT schedules_valid_period CHECK (
        period IN ('MANHA', 'TARDE', 'NOITE')
    ),
    CONSTRAINT schedules_valid_day CHECK (
        day_of_week IN (
            'MONDAY',
            'TUESDAY',
            'WEDNESDAY',
            'THURSDAY',
            'FRIDAY',
            'SATURDAY',
            'SUNDAY'
        )
    ),
    CONSTRAINT schedules_end_after_start CHECK (end_time > start_time)
);

COMMENT ON TABLE schedules IS 'Available time slots for classes';

COMMENT ON COLUMN schedules.day_of_week IS 'Day of week in English (MONDAY, TUESDAY, etc)';

COMMENT ON COLUMN schedules.period IS 'Period of day (MANHA, TARDE, NOITE)';

-- =========================
-- TABLE: courses
-- Stores available class time slots
-- =========================

CREATE TABLE courses (
    id BIGINT PRIMARY KEY DEFAULT nextval ('courses_seq'),
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    duration INTEGER,
    active BOOLEAN NOT NULL,
    CONSTRAINT courses_duration_positive CHECK (duration > 0)
);

-- =========================
-- TABLE: coordinators
-- Stores coordinator information
-- =========================
CREATE TABLE coordinators (
    id BIGINT PRIMARY KEY DEFAULT nextval ('coordinators_seq'),
    registration VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    department VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT coordinators_email_format CHECK (
        email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$'
    )
);

COMMENT ON
TABLE coordinators IS 'Academic coordinators who manage courses';

COMMENT ON COLUMN coordinators.registration IS 'Unique registration number (YYXXXXX format)';

COMMENT ON COLUMN coordinators.active IS 'Indicates if coordinator is active';

-- =========================
-- TABLE: coordinator_courses (Many-to-Many)
-- Junction table between coordinators and courses
-- =========================

CREATE TABLE coordinator_courses (
    coordinator_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY (coordinator_id, course_id),
    CONSTRAINT fk_coordinator_courses_coordinator FOREIGN KEY (coordinator_id) REFERENCES coordinators (id) ON DELETE CASCADE,
    CONSTRAINT fk_coordinator_courses_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

COMMENT ON
TABLE coordinator_courses IS 'Many-to-Many relationship between coordinators and courses';

COMMENT ON COLUMN coordinator_courses.coordinator_id IS 'Foreign key to coordinators table';

COMMENT ON COLUMN coordinator_courses.course_id IS 'Foreign key to courses table';

-- =========================
-- INDEXES
-- =========================

CREATE INDEX idx_subjects_code ON subjects (code);

CREATE INDEX idx_professors_registration ON professors (registration);

CREATE INDEX idx_professors_email ON professors (email);

CREATE INDEX idx_schedules_day_period ON schedules (day_of_week, period);

CREATE INDEX idx_courses_code ON courses (code);

CREATE INDEX idx_coordinators_registration ON coordinators(registration);
CREATE INDEX idx_coordinators_email ON coordinators(email);
CREATE INDEX idx_coordinators_active ON coordinators(active);
CREATE INDEX idx_coordinators_department ON coordinators(department);

CREATE INDEX idx_coordinator_courses_coordinator ON coordinator_courses(coordinator_id);
CREATE INDEX idx_coordinator_courses_course ON coordinator_courses(course_id);

-- ============================================
-- END OF V1
-- ============================================