-- ============================================
-- DISCIPLINAS
-- ============================================
INSERT INTO subjects (id, code, name, workload, credits, description) VALUES 
(nextval('subjects_seq'), 'MAT001', 'Cálculo I', 80, 4, 'Introdução ao cálculo diferencial e integral'),
(nextval('subjects_seq'), 'MAT002', 'Cálculo II', 80, 4, 'Continuação de Cálculo I'),
(nextval('subjects_seq'), 'MAT003', 'Álgebra Linear', 60, 3, 'Vetores, matrizes e sistemas lineares'),
(nextval('subjects_seq'), 'PRG001', 'Programação I', 80, 4, 'Introdução à programação'),
(nextval('subjects_seq'), 'PRG002', 'Programação Orientada a Objetos', 80, 4, 'POO com Java'),
(nextval('subjects_seq'), 'PRG003', 'Estruturas de Dados', 80, 4, 'Listas, pilhas, filas e árvores'),
(nextval('subjects_seq'), 'BDD001', 'Banco de Dados I', 60, 3, 'Modelagem e SQL'),
(nextval('subjects_seq'), 'BDD002', 'Banco de Dados II', 60, 3, 'Otimização e NoSQL'),
(nextval('subjects_seq'), 'ENG001', 'Engenharia de Software I', 60, 3, 'Processos e metodologias'),
(nextval('subjects_seq'), 'ENG002', 'Engenharia de Software II', 60, 3, 'Arquitetura e padrões'),
(nextval('subjects_seq'), 'WEB001', 'Desenvolvimento Web', 80, 4, 'HTML, CSS, JavaScript'),
(nextval('subjects_seq'), 'WEB002', 'Desenvolvimento Mobile', 80, 4, 'Apps Android e iOS'),
(nextval('subjects_seq'), 'RED001', 'Redes de Computadores', 60, 3, 'Protocolos e arquitetura'),
(nextval('subjects_seq'), 'SEG001', 'Segurança da Informação', 60, 3, 'Criptografia e segurança'),
(nextval('subjects_seq'), 'IAR001', 'Inteligência Artificial', 60, 3, 'ML e Deep Learning');

-- ============================================
-- PROFESSORES
-- ============================================
INSERT INTO professors (id, registration, name, email, title, department) VALUES 
(nextval('professors_seq'), '2600101', 'Maria Iana Araujo', 'maria.iana@unifor.br', 'Doutor', 'Ciência da Computação'),
(nextval('professors_seq'), '2600102', 'Lia Avelino', 'lia.avelino@unifor.br', 'Doutora', 'Engenharia de Software'),
(nextval('professors_seq'), '2600103', 'Maria Clara', 'maria.clara@unifor.br', 'Mestre', 'Sistemas de Informação'),
(nextval('professors_seq'), '2600104', 'Jorge Luiz', 'jorge.luiz@unifor.br', 'Especialista', 'Arquitetura de Computadores'),
(nextval('professors_seq'), '2600105', 'Leticia Amb', 'leticia.amb@unifor.br', 'Doutora', 'Engenharia de Software'),
(nextval('professors_seq'), '2600106', 'Julia Emilia', 'julia.emilia@unifor.br', 'Doutora', 'Ciência da Computação');
(nextval('professors_seq'), '2600107', 'Ana Carvalho', 'ana.carvalho@unifor.br', 'Doutora', 'Ciência da Computação');
(nextval('professors_seq'), '2600108', 'Agatha Ferreira', 'agatha.ferreira@unifor.br', 'Doutora', 'Ciência da Computação');
(nextval('professors_seq'), '2600109', 'Jana Moreira', 'jana.moreira@unifor.br', 'Doutora', 'Ciência da Computação');