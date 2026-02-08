-- ============================================
-- V7__seed_students.sql
-- Seeds: Students (5 students using course codes)
-- ============================================

INSERT INTO students (registration, name, email, cpf, phone, course_id, active) VALUES 
-- Engenharia de Software
('2610001', 'Ana Silva Santos', 'ana.silva@aluno.unifor.br', '12345678901', '85987654321', 
 (SELECT id FROM courses WHERE code = 'ENG_SOFT'), true),

('2610002', 'Carlos Eduardo Lima', 'carlos.eduardo@aluno.unifor.br', '23456789012', '85988765432', 
 (SELECT id FROM courses WHERE code = 'ENG_SOFT'), true),

-- Engenharia de Computação
('2610003', 'Beatriz Costa Oliveira', 'beatriz.costa@aluno.unifor.br', '34567890123', '85989876543', 
 (SELECT id FROM courses WHERE code = 'ENG_COMP'), true),

-- Ciência da Computação
('2610004', 'Diego Fernandes Rocha', 'diego.fernandes@aluno.unifor.br', '45678901234', '85981234567', 
 (SELECT id FROM courses WHERE code = 'CC'), true),

-- Sistemas de Informação
('2610005', 'Eduarda Martins Souza', 'eduarda.martins@aluno.unifor.br', '56789012345', '85982345678', 
 (SELECT id FROM courses WHERE code = 'SI'), true);