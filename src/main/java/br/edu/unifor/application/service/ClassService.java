package br.edu.unifor.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import br.edu.unifor.domain.entity.Class;
import br.edu.unifor.domain.entity.Class.ClassStatus;
import br.edu.unifor.domain.entity.Course;
import br.edu.unifor.domain.entity.Professor;
import br.edu.unifor.domain.entity.Schedule;
import br.edu.unifor.domain.entity.Subject;
import br.edu.unifor.domain.repository.ClassRepository;
import br.edu.unifor.domain.repository.CourseRepository;
import br.edu.unifor.domain.repository.EnrollmentRepository;
import br.edu.unifor.domain.repository.ProfessorRepository;
import br.edu.unifor.domain.repository.ScheduleRepository;
import br.edu.unifor.domain.repository.SubjectRepository;
import br.edu.unifor.infrastructure.exception.ClassHasEnrollmentsException;
import br.edu.unifor.infrastructure.exception.ClassNotFoundException;
import br.edu.unifor.infrastructure.exception.CourseNotFoundException;
import br.edu.unifor.infrastructure.exception.ProfessorNotFoundException;
import br.edu.unifor.infrastructure.exception.ProfessorScheduleConflictException;

@ApplicationScoped
public class ClassService {

    @Inject
    ClassRepository classRepository;

    @Inject
    SubjectRepository subjectRepository;

    @Inject
    ProfessorRepository professorRepository;

    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    CourseRepository courseRepository;

    @Inject
    EnrollmentRepository enrollmentRepository;

    /**
     * Cria uma nova turma.
     * 
     * Validações 
     * - Existência de subject, professor, schedule, course
     * - Professor não pode ter 2 turmas no mesmo horário
     * - Código deve ser único
     */
    @Transactional
    public Class createClass(@Valid Class classEntity) {
        // Validar código único
        if (classRepository.existsByCode(classEntity.code)) {
            throw new IllegalArgumentException("Já existe uma turma com o código: " + classEntity.code);
        }

        // Validar existência de Subject
        Subject subject = subjectRepository.findByIdOptional(classEntity.subject.id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        // Validar existência de Professor
        Professor professor = professorRepository.findByIdOptional(classEntity.professor.id)
                .orElseThrow(() -> new ProfessorNotFoundException(classEntity.professor.id));

        // Validar existência de Schedule
        Schedule schedule = scheduleRepository.findByIdOptional(classEntity.schedule.id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado"));

        // Validar existência de Course
        Course course = courseRepository.findByIdOptional(classEntity.course.id)
                .orElseThrow(() -> new CourseNotFoundException(classEntity.course.id));

        // Validar conflito de horário do professor
        if (classRepository.hasProfessorScheduleConflict(professor.id, schedule.id, null)) {
            String scheduleInfo = schedule.dayOfWeek + " às " + schedule.startTime;
            throw new ProfessorScheduleConflictException(professor.name, scheduleInfo);
        }

        // Atribuir entidades carregadas
        classEntity.subject = subject;
        classEntity.professor = professor;
        classEntity.schedule = schedule;
        classEntity.course = course;
        classEntity.status = ClassStatus.ATIVA;
        classEntity.enrolledStudents = 0;

        classRepository.persist(classEntity);
        return classEntity;
    }

    public List<Class> getAllClasses() {
        return classRepository.listAll();
    }

    public List<Class> getActiveClasses() {
        return classRepository.findAllActive();
    }

    public Class findById(Long id) {
        return classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ClassNotFoundException(id));
    }

    public Class findByCode(String code) {
        return classRepository.findByCode(code)
                .orElseThrow(() -> new ClassNotFoundException("Turma não encontrada: " + code));
    }

    public List<Class> findBySubject(Long subjectId) {
        return classRepository.findBySubject(subjectId);
    }

    public List<Class> findByProfessor(Long professorId) {
        return classRepository.findByProfessor(professorId);
    }

    public List<Class> findByCourse(Long courseId) {
        return classRepository.findByCourse(courseId);
    }

    public List<Class> findActiveByCourse(Long courseId) {
        return classRepository.findActiveByCourse(courseId);
    }

    public List<Class> findBySemester(String semester) {
        return classRepository.findBySemester(semester);
    }

    public List<Class> findClassesWithAvailableSlots() {
        return classRepository.findActiveWithAvailableSlots();
    }

    public List<Class> findClassesByCourseWithSlots(Long courseId) {
        return classRepository.findActiveByCourseWithSlots(courseId);
    }

    /**
     * Atualiza turma.
     * 
     * - Professor
     * - Horário
     * - Capacidade máxima (desde que >= enrolledStudents)
     */
    @Transactional
    public Class update(Long id, Class classAtualizado) {
        Class classEntity = findById(id);

        // Validar professor se mudou
        if (!classEntity.professor.id.equals(classAtualizado.professor.id)) {
            Professor professor = professorRepository.findByIdOptional(classAtualizado.professor.id)
                    .orElseThrow(() -> new ProfessorNotFoundException(classAtualizado.professor.id));

            // Validar conflito de horário
            if (classRepository.hasProfessorScheduleConflict(
                    professor.id, classAtualizado.schedule.id, id)) {
                String scheduleInfo = classAtualizado.schedule.dayOfWeek + " às " + classAtualizado.schedule.startTime;
                throw new ProfessorScheduleConflictException(professor.name, scheduleInfo);
            }

            classEntity.professor = professor;
        }

        // Validar horário se mudou
        if (!classEntity.schedule.id.equals(classAtualizado.schedule.id)) {
            Schedule schedule = scheduleRepository.findByIdOptional(classAtualizado.schedule.id)
                    .orElseThrow(() -> new RuntimeException("Horário não encontrado"));

            // Validar conflito
            if (classRepository.hasProfessorScheduleConflict(
                    classEntity.professor.id, schedule.id, id)) {
                String scheduleInfo = schedule.dayOfWeek + " às " + schedule.startTime;
                throw new ProfessorScheduleConflictException(classEntity.professor.name, scheduleInfo);
            }

            classEntity.schedule = schedule;
        }

        // Validar capacidade (não pode ser menor que alunos matriculados)
        if (classAtualizado.maxCapacity < classEntity.enrolledStudents) {
            throw new IllegalArgumentException(
                "Capacidade não pode ser menor que alunos matriculados (" + classEntity.enrolledStudents + ")"
            );
        }

        classEntity.maxCapacity = classAtualizado.maxCapacity;

        return classEntity;
    }

    @Transactional
    public void cancel(Long id) {
        Class classEntity = findById(id);
        classEntity.status = ClassStatus.CANCELADA;
    }

    @Transactional
    public void complete(Long id) {
        Class classEntity = findById(id);
        classEntity.status = ClassStatus.CONCLUIDA;
    }

    /**
     * Remove turma.
     */
    @Transactional
    public void delete(Long id) {
        Class classEntity = findById(id);

        long activeEnrollments = enrollmentRepository.countActiveByClass(id);
        if (activeEnrollments > 0) {
            throw new ClassHasEnrollmentsException(classEntity.code, (int) activeEnrollments);
        }

        classRepository.delete(classEntity);
    }

    public long countActiveByProfessor(Long professorId) {
        return classRepository.countActiveByProfessor(professorId);
    }

    public long countActiveByCourse(Long courseId) {
        return classRepository.countActiveByCourse(courseId);
    }
}