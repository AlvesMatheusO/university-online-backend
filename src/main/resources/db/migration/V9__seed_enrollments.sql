-- ============================================
-- V9__seed_enrollments.sql
-- Seeds: Enrollments
-- ============================================

-- Ana Silva Santos
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610001'),
 (SELECT id FROM classes WHERE code = 'MAT001-A-2024.1'), 'ATIVA'),
((SELECT id FROM students WHERE registration = '2610001'),
 (SELECT id FROM classes WHERE code = 'PRG001-A-2024.1'), 'ATIVA');

-- Carlos Eduardo Lima
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610002'),
 (SELECT id FROM classes WHERE code = 'MAT001-A-2024.1'), 'ATIVA'),
((SELECT id FROM students WHERE registration = '2610002'),
 (SELECT id FROM classes WHERE code = 'PRG001-A-2024.1'), 'ATIVA');

-- Beatriz Costa Oliveira
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610003'),
 (SELECT id FROM classes WHERE code = 'BDD001-A-2024.1'), 'ATIVA'),
((SELECT id FROM students WHERE registration = '2610003'),
 (SELECT id FROM classes WHERE code = 'PRG002-A-2024.1'), 'ATIVA');

-- João Almeida (Matrículas para teste no Angular)
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE email = 'joao.almeida@unifor.br'),
 (SELECT id FROM classes WHERE code = 'MAT001-A-2024.1'), 'ATIVA'),
((SELECT id FROM students WHERE email = 'joao.almeida@unifor.br'),
 (SELECT id FROM classes WHERE code = 'PRG001-A-2024.1'), 'ATIVA');