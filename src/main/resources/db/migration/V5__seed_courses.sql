-- ============================================
-- V5__seed_courses.sql
-- Seeds: Academic Courses
-- ============================================

INSERT INTO courses (code, name, department, duration, active) VALUES 
-- Engenharia
('ENG_SOFT', 'Engenharia de Software', 'Engenharia', 10, true),
('ENG_COMP', 'Engenharia de Computação', 'Engenharia', 10, true),

-- Ciências
('CC', 'Ciência da Computação', 'Ciências Exatas', 8, true),
('SI', 'Sistemas de Informação', 'Tecnologia', 8, true),

-- Tecnologia
('ADS', 'Análise e Desenvolvimento de Sistemas', 'Tecnologia', 6, true),
('TADS', 'Tecnologia em Análise e Desenvolvimento de Sistemas', 'Tecnologia', 5, true),

-- Gestão
('GTI', 'Gestão da Tecnologia da Informação', 'Gestão', 4, true),
('REDES', 'Redes de Computadores', 'Tecnologia', 6, true),

-- Segurança
('SEG_INFO', 'Segurança da Informação', 'Tecnologia', 6, true);