-- ============================================
-- V9__seed_enrollments.sql
-- Seeds: Enrollments (matrículas de exemplo)
-- ============================================
-- DEPENDENCIES: V9 (students), V11 (classes)
-- 
-- STRATEGY: Creates sample enrollments ensuring:
-- - No duplicate enrollments (student + class unique)
-- - No schedule conflicts for students
-- - Respects class capacity
-- ============================================

-- Ana Silva Santos (student 2610001) - Engenharia de Software
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610001'),
 (SELECT id FROM classes WHERE code = 'MAT001-A-2024.1'),
 'ATIVA'),

((SELECT id FROM students WHERE registration = '2610001'),
 (SELECT id FROM classes WHERE code = 'PRG001-A-2024.1'),
 'ATIVA');

-- Carlos Eduardo Lima (student 2610002) - Engenharia de Software
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610002'),
 (SELECT id FROM classes WHERE code = 'MAT001-A-2024.1'),
 'ATIVA'),

((SELECT id FROM students WHERE registration = '2610002'),
 (SELECT id FROM classes WHERE code = 'PRG001-A-2024.1'),
 'ATIVA');

-- Beatriz Costa Oliveira (student 2610003) - Ciência da Computação
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610003'),
 (SELECT id FROM classes WHERE code = 'BDD001-A-2024.1'),
 'ATIVA'),

((SELECT id FROM students WHERE registration = '2610003'),
 (SELECT id FROM classes WHERE code = 'PRG002-A-2024.1'),
 'ATIVA');

-- Diego Fernandes Rocha (student 2610004) - Ciência da Computação
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610004'),
 (SELECT id FROM classes WHERE code = 'BDD001-A-2024.1'),
 'ATIVA'),

((SELECT id FROM students WHERE registration = '2610004'),
 (SELECT id FROM classes WHERE code = 'PRG002-A-2024.1'),
 'ATIVA');

-- Eduarda Martins Souza (student 2610005) - Sistemas de Informação
INSERT INTO enrollments (student_id, class_id, status) VALUES 
((SELECT id FROM students WHERE registration = '2610005'),
 (SELECT id FROM classes WHERE code = 'WEB001-A-2024.1'),
 'ATIVA');

-- ============================================
-- Summary:
-- - 9 matrículas criadas
-- - 5 alunos matriculados
-- - Distribuídas em 5 turmas diferentes
-- - Sem conflitos de horário
-- ============================================