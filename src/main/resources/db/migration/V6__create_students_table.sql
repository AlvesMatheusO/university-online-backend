-- ============================================
-- V6__create_students_table.sql
-- Creates students table with foreign key to courses
-- ============================================

-- =========================
-- SEQUENCE
-- =========================

CREATE SEQUENCE students_seq START WITH 1 INCREMENT BY 50;

-- =========================
-- TABLE: students
-- Stores student information
-- =========================

CREATE TABLE students (
    id BIGINT PRIMARY KEY DEFAULT nextval('students_seq'),
    registration VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    phone VARCHAR(15),
    course_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    
    CONSTRAINT fk_student_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT students_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$'),
    CONSTRAINT students_cpf_format CHECK (cpf ~ '^[0-9]{11}$')
);

COMMENT ON TABLE students IS 'Student records with course enrollment';
COMMENT ON COLUMN students.registration IS 'Unique student registration number (YYXXXXX format)';
COMMENT ON COLUMN students.cpf IS 'Brazilian tax ID (CPF) - 11 digits only';
COMMENT ON COLUMN students.active IS 'Indicates if student is active (can enroll in classes)';

-- =========================
-- INDEXES
-- =========================

CREATE INDEX idx_students_registration ON students(registration);
CREATE INDEX idx_students_email ON students(email);
CREATE INDEX idx_students_cpf ON students(cpf);
CREATE INDEX idx_students_course ON students(course_id);
CREATE INDEX idx_students_active ON students(active);
CREATE INDEX idx_students_course_active ON students(course_id, active);