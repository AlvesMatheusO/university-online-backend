-- ============================================
-- V1__create_initial_schema.sql
-- Academic Enrollment System - Initial Database Schema
-- ============================================

-- =========================
-- SEQUENCES
-- =========================

CREATE SEQUENCE subjects_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE professors_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE schedules_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE courses_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE students_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE coordinators_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE classes_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE enrollments_seq START WITH 1 INCREMENT BY 50;

-- =========================
-- TABLE: subjects
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
-- TABLE: courses
-- =========================

CREATE TABLE courses (
    id BIGINT PRIMARY KEY DEFAULT nextval('courses_seq'),
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    duration INTEGER,
    active BOOLEAN NOT NULL,
    
    CONSTRAINT courses_duration_positive CHECK (duration > 0)
);

COMMENT ON TABLE courses IS 'Academic courses and programs';
COMMENT ON COLUMN courses.code IS 'Unique course code (e.g., ENG_SOFT)';
COMMENT ON COLUMN courses.department IS 'Department offering the course';
COMMENT ON COLUMN courses.duration IS 'Course duration in semesters';
COMMENT ON COLUMN courses.active IS 'Whether course is currently active';

-- =========================
-- TABLE: students
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
-- TABLE: coordinators
-- =========================

CREATE TABLE coordinators (
    id BIGINT PRIMARY KEY DEFAULT nextval('coordinators_seq'),
    registration VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    department VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT true,
    
    CONSTRAINT coordinators_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
);

COMMENT ON TABLE coordinators IS 'Academic coordinators who manage courses';
COMMENT ON COLUMN coordinators.registration IS 'Unique registration number (YYXXXXX format)';
COMMENT ON COLUMN coordinators.active IS 'Indicates if coordinator is active';

-- =========================
-- TABLE: coordinator_courses
-- =========================

CREATE TABLE coordinator_courses (
    coordinator_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    
    PRIMARY KEY (coordinator_id, course_id),
    
    CONSTRAINT fk_coordinator_courses_coordinator FOREIGN KEY (coordinator_id) REFERENCES coordinators(id) ON DELETE CASCADE,
    CONSTRAINT fk_coordinator_courses_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

COMMENT ON TABLE coordinator_courses IS 'Many-to-Many relationship between coordinators and courses';
COMMENT ON COLUMN coordinator_courses.coordinator_id IS 'Foreign key to coordinators table';
COMMENT ON COLUMN coordinator_courses.course_id IS 'Foreign key to courses table';

-- =========================
-- TABLE: classes
-- =========================

CREATE TABLE classes (
    id BIGINT PRIMARY KEY DEFAULT nextval('classes_seq'),
    code VARCHAR(30) NOT NULL UNIQUE,
    subject_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    max_capacity INTEGER NOT NULL,
    enrolled_students INTEGER NOT NULL DEFAULT 0,
    semester VARCHAR(10),
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    
    CONSTRAINT fk_class_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_class_professor FOREIGN KEY (professor_id) REFERENCES professors(id),
    CONSTRAINT fk_class_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(id),
    CONSTRAINT fk_class_course FOREIGN KEY (course_id) REFERENCES courses(id),
    
    CONSTRAINT classes_max_capacity_positive CHECK (max_capacity > 0),
    CONSTRAINT classes_enrolled_non_negative CHECK (enrolled_students >= 0),
    CONSTRAINT classes_enrolled_not_exceed_max CHECK (enrolled_students <= max_capacity),
    CONSTRAINT classes_valid_status CHECK (status IN ('ATIVA', 'CANCELADA', 'CONCLUIDA', 'SUSPENSA'))
);

COMMENT ON TABLE classes IS 'Class offerings (turmas) combining subject, professor, schedule and course';
COMMENT ON COLUMN classes.code IS 'Unique class code (e.g., MAT001-A-2024.1)';
COMMENT ON COLUMN classes.max_capacity IS 'Maximum number of students allowed';
COMMENT ON COLUMN classes.enrolled_students IS 'Current number of enrolled students';
COMMENT ON COLUMN classes.semester IS 'Academic semester (e.g., 2024.1, 2024.2)';
COMMENT ON COLUMN classes.status IS 'Class status (ATIVA, CANCELADA, CONCLUIDA, SUSPENSA)';

-- =========================
-- TABLE: enrollments
-- =========================

CREATE TABLE enrollments (
    id BIGINT PRIMARY KEY DEFAULT nextval('enrollments_seq'),
    student_id BIGINT NOT NULL,
    class_id BIGINT NOT NULL,
    enrollment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    final_grade NUMERIC(4,2),
    attendance NUMERIC(5,2),
    cancellation_date TIMESTAMP,
    cancellation_reason VARCHAR(500),
    
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enrollment_class FOREIGN KEY (class_id) REFERENCES classes(id),
    
    CONSTRAINT enrollments_valid_status CHECK (status IN ('ATIVA', 'CANCELADA', 'CONCLUIDA', 'TRANCADA')),
    CONSTRAINT enrollments_grade_range CHECK (final_grade >= 0 AND final_grade <= 10),
    CONSTRAINT enrollments_attendance_range CHECK (attendance >= 0 AND attendance <= 100),
    CONSTRAINT enrollments_unique_student_class UNIQUE (student_id, class_id)
);

COMMENT ON TABLE enrollments IS 'Student enrollments in classes';
COMMENT ON COLUMN enrollments.student_id IS 'Foreign key to students table';
COMMENT ON COLUMN enrollments.class_id IS 'Foreign key to classes table';
COMMENT ON COLUMN enrollments.enrollment_date IS 'Date and time of enrollment';
COMMENT ON COLUMN enrollments.status IS 'Enrollment status (ATIVA, CANCELADA, CONCLUIDA, TRANCADA)';
COMMENT ON COLUMN enrollments.final_grade IS 'Final grade (0-10)';
COMMENT ON COLUMN enrollments.attendance IS 'Attendance percentage (0-100%)';
COMMENT ON COLUMN enrollments.cancellation_date IS 'Date of cancellation (if applicable)';
COMMENT ON COLUMN enrollments.cancellation_reason IS 'Reason for cancellation (if applicable)';

-- =========================
-- INDEXES
-- =========================

CREATE INDEX idx_subjects_code ON subjects(code);

CREATE INDEX idx_professors_registration ON professors(registration);
CREATE INDEX idx_professors_email ON professors(email);

CREATE INDEX idx_schedules_day_period ON schedules(day_of_week, period);

CREATE INDEX idx_courses_code ON courses(code);
CREATE INDEX idx_courses_active ON courses(active);

CREATE INDEX idx_students_registration ON students(registration);
CREATE INDEX idx_students_email ON students(email);
CREATE INDEX idx_students_cpf ON students(cpf);
CREATE INDEX idx_students_course ON students(course_id);
CREATE INDEX idx_students_active ON students(active);
CREATE INDEX idx_students_course_active ON students(course_id, active);

CREATE INDEX idx_coordinators_registration ON coordinators(registration);
CREATE INDEX idx_coordinators_email ON coordinators(email);
CREATE INDEX idx_coordinators_active ON coordinators(active);
CREATE INDEX idx_coordinators_department ON coordinators(department);

CREATE INDEX idx_coordinator_courses_coordinator ON coordinator_courses(coordinator_id);
CREATE INDEX idx_coordinator_courses_course ON coordinator_courses(course_id);

CREATE INDEX idx_classes_code ON classes(code);
CREATE INDEX idx_classes_subject ON classes(subject_id);
CREATE INDEX idx_classes_professor ON classes(professor_id);
CREATE INDEX idx_classes_schedule ON classes(schedule_id);
CREATE INDEX idx_classes_course ON classes(course_id);
CREATE INDEX idx_classes_semester ON classes(semester);
CREATE INDEX idx_classes_status ON classes(status);
CREATE INDEX idx_classes_professor_schedule ON classes(professor_id, schedule_id, status);

CREATE INDEX idx_enrollments_student ON enrollments(student_id);
CREATE INDEX idx_enrollments_class ON enrollments(class_id);
CREATE INDEX idx_enrollments_status ON enrollments(status);
CREATE INDEX idx_enrollments_student_status ON enrollments(student_id, status);
CREATE INDEX idx_enrollments_class_status ON enrollments(class_id, status);
CREATE INDEX idx_enrollments_student_schedule ON enrollments(student_id, class_id, status);

-- ============================================
-- END OF V1
-- ============================================