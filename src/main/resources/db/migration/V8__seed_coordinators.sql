INSERT INTO coordinators (registration, name, email, phone, department, active) VALUES 
('2600201', 'Dr. Joel Sotero', 'joel.sotero@unifor.br', '85991234567', 'Engenharia', true),
('2600202', 'Dra. Patricia Mendes Costa', 'patricia.mendes@unifor.br', '85992345678', 'Ciências Exatas', true),
('2600203', 'Prof. Fernando Santos Lima', 'fernando.santos@unifor.br', '85993456789', 'Tecnologia', true);

-- Assign courses to coordinators (Many-to-Many relationship)

-- Dr. Joel coordena Engenharias
INSERT INTO coordinator_courses (coordinator_id, course_id) VALUES 
((SELECT id FROM coordinators WHERE registration = '2600201'), 
 (SELECT id FROM courses WHERE code = 'ENG_SOFT')),

((SELECT id FROM coordinators WHERE registration = '2600201'), 
 (SELECT id FROM courses WHERE code = 'ENG_COMP'));

-- Dra. Patricia coordena Ciências
INSERT INTO coordinator_courses (coordinator_id, course_id) VALUES 
((SELECT id FROM coordinators WHERE registration = '2600202'), 
 (SELECT id FROM courses WHERE code = 'CC')),

((SELECT id FROM coordinators WHERE registration = '2600202'), 
 (SELECT id FROM courses WHERE code = 'SI'));

-- Prof. Fernando coordena Tecnologia
INSERT INTO coordinator_courses (coordinator_id, course_id) VALUES 
((SELECT id FROM coordinators WHERE registration = '2600203'), 
 (SELECT id FROM courses WHERE code = 'ADS')),

((SELECT id FROM coordinators WHERE registration = '2600203'), 
 (SELECT id FROM courses WHERE code = 'TADS')),

((SELECT id FROM coordinators WHERE registration = '2600203'), 
 (SELECT id FROM courses WHERE code = 'GTI')),

((SELECT id FROM coordinators WHERE registration = '2600203'), 
 (SELECT id FROM courses WHERE code = 'REDES')),

((SELECT id FROM coordinators WHERE registration = '2600203'), 
 (SELECT id FROM courses WHERE code = 'SEG_INFO'));
