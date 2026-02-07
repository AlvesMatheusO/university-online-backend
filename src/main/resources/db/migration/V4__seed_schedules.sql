-- ============================================
-- V4__seed_schedules.sql
-- Seeds: Class Schedules (Mon-Fri, 6 slots per day)
-- ============================================

INSERT INTO schedules (day_of_week, start_time, end_time, period) VALUES 
-- Monday
('MONDAY', '08:00:00', '10:00:00', 'MANHA'),
('MONDAY', '10:00:00', '12:00:00', 'MANHA'),
('MONDAY', '14:00:00', '16:00:00', 'TARDE'),
('MONDAY', '16:00:00', '18:00:00', 'TARDE'),
('MONDAY', '19:00:00', '21:00:00', 'NOITE'),
('MONDAY', '21:00:00', '23:00:00', 'NOITE'),

-- Tuesday
('TUESDAY', '08:00:00', '10:00:00', 'MANHA'),
('TUESDAY', '10:00:00', '12:00:00', 'MANHA'),
('TUESDAY', '14:00:00', '16:00:00', 'TARDE'),
('TUESDAY', '16:00:00', '18:00:00', 'TARDE'),
('TUESDAY', '19:00:00', '21:00:00', 'NOITE'),
('TUESDAY', '21:00:00', '23:00:00', 'NOITE'),

-- Wednesday
('WEDNESDAY', '08:00:00', '10:00:00', 'MANHA'),
('WEDNESDAY', '10:00:00', '12:00:00', 'MANHA'),
('WEDNESDAY', '14:00:00', '16:00:00', 'TARDE'),
('WEDNESDAY', '16:00:00', '18:00:00', 'TARDE'),
('WEDNESDAY', '19:00:00', '21:00:00', 'NOITE'),
('WEDNESDAY', '21:00:00', '23:00:00', 'NOITE'),

-- Thursday
('THURSDAY', '08:00:00', '10:00:00', 'MANHA'),
('THURSDAY', '10:00:00', '12:00:00', 'MANHA'),
('THURSDAY', '14:00:00', '16:00:00', 'TARDE'),
('THURSDAY', '16:00:00', '18:00:00', 'TARDE'),
('THURSDAY', '19:00:00', '21:00:00', 'NOITE'),
('THURSDAY', '21:00:00', '23:00:00', 'NOITE'),

-- Friday
('FRIDAY', '08:00:00', '10:00:00', 'MANHA'),
('FRIDAY', '10:00:00', '12:00:00', 'MANHA'),
('FRIDAY', '14:00:00', '16:00:00', 'TARDE'),
('FRIDAY', '16:00:00', '18:00:00', 'TARDE'),
('FRIDAY', '19:00:00', '21:00:00', 'NOITE'),
('FRIDAY', '21:00:00', '23:00:00', 'NOITE');