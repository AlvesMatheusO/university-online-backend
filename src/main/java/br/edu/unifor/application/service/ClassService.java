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
import br.edu.unifor.domain.repository.ProfessorRepository;
import br.edu.unifor.domain.repository.ScheduleRepository;
import br.edu.unifor.domain.repository.SubjectRepository;
import br.edu.unifor.infrastructure.exception.ClassHasEnrollmentsException;
import br.edu.unifor.infrastructure.exception.ClassNotFoundException;
import br.edu.unifor.infrastructure.exception.CourseNotFoundException;
import br.edu.unifor.infrastructure.exception.ProfessorNotFoundException;
import br.edu.unifor.infrastructure.exception.ProfessorScheduleConflictException;

/**
 * Service para gerenciar turmas/aulas.
 * 
 * Implementa todas as regras de negócio do desafio:
 * - Validação de existência (professor, disciplina, horário, curso)
 * - Validação de conflito de horário do professor
 * - Controle de capacidade
 */
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

    /**
     * Cria uma nova turma.
     * 
     * VALIDAÇÕES:
     * - Subject, Professor, Schedule e Course devem existir
     * - Professor não pode ter conflito de horário
     * - Código deve ser único
     */
    @Transactional
    public Class createClass(@Valid Class classEntity) {

        // Validar se subject existe
        Subject subject = subjectRepository.findByIdOptional(classEntity.subject.id)
                .orElseThrow(() -> new RuntimeException(
                        "Disciplina não encontrada: " + classEntity.subject.id));

        // Validar se professor existe
        Professor professor = professorRepository.findByIdOptional(classEntity.professor.id)
                .orElseThrow(() -> new ProfessorNotFoundException(classEntity.professor.id));

        // Validar se schedule existe
        Schedule schedule = scheduleRepository.findByIdOptional(classEntity.schedule.id)
                .orElseThrow(() -> new RuntimeException(
                        "Horário não encontrado: " + classEntity.schedule.id));

        // Validar se course existe
        Course course = courseRepository.findByIdOptional(classEntity.course.id)
                .orElseThrow(() -> new CourseNotFoundException(classEntity.course.id));

        // Verificar conflito de horário do professor
        if (classRepository.hasProfessorScheduleConflict(professor.id, schedule.id, null)) {
            String scheduleInfo = String.format("%s às %s",
                    schedule.dayOfWeek, schedule.startTime);
            throw new ProfessorScheduleConflictException(professor.name, scheduleInfo);
        }

        // Validar código único
        if (classRepository.existsByCode(classEntity.code)) {
            throw new IllegalArgumentException(
                    "Já existe uma turma com o código: " + classEntity.code);
        }

        classEntity.subject = subject;
        classEntity.professor = professor;
        classEntity.schedule = schedule;
        classEntity.course = course;

        classEntity.enrolledStudents = 0;
        classEntity.status = ClassStatus.ATIVA;

        classRepository.persist(classEntity);
        return classEntity;
    }

    /**
     * Lista todas as turmas.
     */
    public List<Class> getAllClasses() {
        return classRepository.listAll();
    }

    /**
     * Lista turmas ativas.
     */
    public List<Class> getActiveClasses() {
        return classRepository.findAllActive();
    }

    /**
     * Busca turma por ID.
     */
    public Class findById(Long id) {
        return classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ClassNotFoundException(id));
    }

    /**
     * Busca turma por código.
     */
    public Class findByCode(String code) {
        return classRepository.findByCode(code)
                .orElseThrow(() -> new ClassNotFoundException(
                        "Turma não encontrada com código: " + code));
    }

    /**
     * Busca turmas por disciplina.
     */
    public List<Class> findBySubject(Long subjectId) {
        return classRepository.findBySubject(subjectId);
    }

    /**
     * Busca turmas por professor.
     */
    public List<Class> findByProfessor(Long professorId) {
        return classRepository.findByProfessor(professorId);
    }

    /**
     * Busca turmas por curso.
     */
    public List<Class> findByCourse(Long courseId) {
        return classRepository.findByCourse(courseId);
    }

    /**
     * Busca turmas ativas de um curso.
     */
    public List<Class> findActiveByCourse(Long courseId) {
        return classRepository.findActiveByCourse(courseId);
    }

    /**
     * Busca turmas por semestre.
     */
    public List<Class> findBySemester(String semester) {
        return classRepository.findBySemester(semester);
    }

    /**
     * Busca turmas com vagas disponíveis.
     */
    public List<Class> findClassesWithAvailableSlots() {
        return classRepository.findActiveWithAvailableSlots();
    }

    /**
     * Busca turmas de um curso com vagas disponíveis.
     */
    public List<Class> findClassesByCourseWithSlots(Long courseId) {
        return classRepository.findActiveByCourseWithSlots(courseId);
    }

    /**
     * Atualiza uma turma.
     * 
     * ATENÇÃO: Se alterar professor ou horário, valida conflito novamente.
     */
    @Transactional
    public Class update(Long id, Class classAtualizada) {
        Class classEntity = findById(id);

        // Se mudou professor OU horário, validar conflito
        boolean professorChanged = !classEntity.professor.id.equals(classAtualizada.professor.id);
        boolean scheduleChanged = !classEntity.schedule.id.equals(classAtualizada.schedule.id);

        if (professorChanged || scheduleChanged) {
            Long newProfessorId = classAtualizada.professor.id;
            Long newScheduleId = classAtualizada.schedule.id;

            // Verificar conflito (excluindo a própria turma)
            if (classRepository.hasProfessorScheduleConflict(newProfessorId, newScheduleId, id)) {
                Professor professor = professorRepository.findByIdOptional(newProfessorId)
                        .orElseThrow(() -> new ProfessorNotFoundException(newProfessorId));
                Schedule schedule = scheduleRepository.findByIdOptional(newScheduleId)
                        .orElseThrow(() -> new RuntimeException("Horário não encontrado"));

                String scheduleInfo = String.format("%s às %s",
                        schedule.dayOfWeek, schedule.startTime);
                throw new ProfessorScheduleConflictException(professor.name, scheduleInfo);
            }

            // Carregar novas entidades
            classEntity.professor = professorRepository.findByIdOptional(newProfessorId)
                    .orElseThrow(() -> new ProfessorNotFoundException(newProfessorId));
            classEntity.schedule = scheduleRepository.findByIdOptional(newScheduleId)
                    .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
        }

        // Atualizar outros campos
        if (classAtualizada.subject != null && !classEntity.subject.id.equals(classAtualizada.subject.id)) {
            classEntity.subject = subjectRepository.findByIdOptional(classAtualizada.subject.id)
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
        }

        if (classAtualizada.course != null && !classEntity.course.id.equals(classAtualizada.course.id)) {
            classEntity.course = courseRepository.findByIdOptional(classAtualizada.course.id)
                    .orElseThrow(() -> new CourseNotFoundException(classAtualizada.course.id));
        }

        classEntity.maxCapacity = classAtualizada.maxCapacity;
        classEntity.semester = classAtualizada.semester;

        return classEntity;
    }

    /**
     * Cancela uma turma
     */
    @Transactional
    public void cancel(Long id) {
        Class classEntity = findById(id);
        classEntity.status = ClassStatus.CANCELADA;
    }

    /**
     * Marca turma como concluída.
     */
    @Transactional
    public void complete(Long id) {
        Class classEntity = findById(id);
        classEntity.status = ClassStatus.CONCLUIDA;
    }

    /**
     * Remove permanentemente uma turma.
     */
    @Transactional
    public void delete(Long id) {
        Class classEntity = findById(id);

        if (classEntity.enrolledStudents > 0) {
            throw new ClassHasEnrollmentsException(classEntity.code, classEntity.enrolledStudents);
        }

        classRepository.delete(classEntity);
    }

    /**
     * Conta turmas ativas de um professor.
     */
    public long countActiveByProfessor(Long professorId) {
        return classRepository.countActiveByProfessor(professorId);
    }

    /**
     * Conta turmas ativas de um curso.
     */
    public long countActiveByCourse(Long courseId) {
        return classRepository.countActiveByCourse(courseId);
    }
}