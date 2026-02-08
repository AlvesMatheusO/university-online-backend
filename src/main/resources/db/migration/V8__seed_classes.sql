-- ============================================
-- V8__seed_classes.sql
-- Seeds: Classes (6 turmas de exemplo)
-- ============================================
-- DEPENDENCIES: V2 (subjects), V3 (professors), V4 (schedules), V5 (courses)
-- ============================================

INSERT INTO classes (code, subject_id, professor_id, schedule_id, course_id, max_capacity, semester, status) VALUES 

-- Engenharia de Software - Turma A (Manhã)
('MAT001-A-2024.1',
 (SELECT id FROM subjects WHERE code = 'MAT001'),
 (SELECT id FROM professors WHERE registration = '2600101'),
 (SELECT id FROM schedules WHERE day_of_week = 'MONDAY' AND start_time = '08:00:00' LIMIT 1),
 (SELECT id FROM courses WHERE code = 'ENG_SOFT'),
 20, '2024.1', 'ATIVA'),

-- Engenharia de Software - Turma B (Manhã, outro horário)
('PRG001-A-2024.1',
 (SELECT id FROM professors WHERE registration = '2600102'),
 (SELECT id FROM professors WHERE registration = '2600102'),
 (SELECT id FROM schedules WHERE day_of_week = 'TUESDAY' AND start_time = '08:00:00' LIMIT 1),
 (SELECT id FROM courses WHERE code = 'ENG_SOFT'),
 30, '2024.1', 'ATIVA'),

-- Ciência da Computação - Turma A (Tarde)
('BDD001-A-2024.1',
 (SELECT id FROM subjects WHERE code = 'BDD001'),
 (SELECT id FROM professors WHERE registration = '2600103'),
 (SELECT id FROM schedules WHERE day_of_week = 'WEDNESDAY' AND start_time = '14:00:00' LIMIT 1),
 (SELECT id FROM courses WHERE code = 'CC'),
 25, '2024.1', 'ATIVA'),

-- Ciência da Computação - Turma B (Noite)
('PRG002-A-2024.1',
 (SELECT id FROM subjects WHERE code = 'PRG002'),
 (SELECT id FROM professors WHERE registration = '2600104'),
 (SELECT id FROM schedules WHERE day_of_week = 'THURSDAY' AND start_time = '19:00:00' LIMIT 1),
 (SELECT id FROM courses WHERE code = 'CC'),
 3, '2024.1', 'ATIVA'),

-- Sistemas de Informação - Turma A
('WEB001-A-2024.1',
 (SELECT id FROM subjects WHERE code = 'WEB001'),
 (SELECT id FROM professors WHERE registration = '2600105'),
 (SELECT id FROM schedules WHERE day_of_week = 'FRIDAY' AND start_time = '14:00:00' LIMIT 1),
 (SELECT id FROM courses WHERE code = 'SI'),
 10, '2024.1', 'ATIVA'),

-- Análise e Desenvolvimento de Sistemas - Turma A
('ENG001-A-2024.1',
 (SELECT id FROM subjects WHERE code = 'ENG001'),
 (SELECT id FROM professors WHERE registration = '2600106'),
 (SELECT id FROM schedules WHERE day_of_week = 'MONDAY' AND start_time = '19:00:00' LIMIT 1),
 (SELECT id FROM courses WHERE code = 'ADS'),
 25, '2024.1', 'ATIVA');
