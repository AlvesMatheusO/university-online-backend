-- ============================================
-- V2__seed_subjects.sql
-- Seeds: Academic Subjects
-- ============================================

INSERT INTO subjects (code, name, workload, credits, description) VALUES 
-- Mathematics
('MAT001', 'Cálculo I', 80, 4, 'Introdução ao cálculo diferencial e integral'),
('MAT002', 'Cálculo II', 80, 4, 'Continuação de Cálculo I'),
('MAT003', 'Álgebra Linear', 60, 3, 'Vetores, matrizes e sistemas lineares'),

-- Programming
('PRG001', 'Programação I', 80, 4, 'Introdução à programação'),
('PRG002', 'Programação Orientada a Objetos', 80, 4, 'POO com Java'),
('PRG003', 'Estruturas de Dados', 80, 4, 'Listas, pilhas, filas e árvores'),

-- Database
('BDD001', 'Banco de Dados I', 60, 3, 'Modelagem e SQL'),
('BDD002', 'Banco de Dados II', 60, 3, 'Otimização e NoSQL'),

-- Software Engineering
('ENG001', 'Engenharia de Software I', 60, 3, 'Processos e metodologias'),
('ENG002', 'Engenharia de Software II', 60, 3, 'Arquitetura e padrões'),

-- Web/Mobile
('WEB001', 'Desenvolvimento Web', 80, 4, 'HTML, CSS, JavaScript'),
('WEB002', 'Desenvolvimento Mobile', 80, 4, 'Apps Android e iOS'),

-- Others
('RED001', 'Redes de Computadores', 60, 3, 'Protocolos e arquitetura'),
('SEG001', 'Segurança da Informação', 60, 3, 'Criptografia e segurança'),
('IAR001', 'Inteligência Artificial', 60, 3, 'ML e Deep Learning');