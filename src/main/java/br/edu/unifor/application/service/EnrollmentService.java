package br.edu.unifor.application.service;

import java.math.BigDecimal;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.application.dto.request.enrollment.CreateEnrollmentRequest;
import br.edu.unifor.domain.entity.Class;
import br.edu.unifor.domain.entity.Enrollment;
import br.edu.unifor.domain.entity.Enrollment.EnrollmentStatus;
import br.edu.unifor.domain.entity.Schedule;
import br.edu.unifor.domain.entity.Student;
import br.edu.unifor.domain.repository.ClassRepository;
import br.edu.unifor.domain.repository.EnrollmentRepository;
import br.edu.unifor.domain.repository.StudentRepository;
import br.edu.unifor.infrastructure.exception.AlreadyEnrolledException;
import br.edu.unifor.infrastructure.exception.ClassFullException;
import br.edu.unifor.infrastructure.exception.ClassNotFoundException;
import br.edu.unifor.infrastructure.exception.EnrollmentNotFoundException;
import br.edu.unifor.infrastructure.exception.StudentNotFoundException;
import br.edu.unifor.infrastructure.exception.StudentScheduleConflictException;

/**
 * Service para gerenciar matrículas.
 * 
 * Implementa todas as regras de negócio do desafio:
 * - Aluno deve estar ativo
 * - Turma deve estar ativa e ter vagas
 * - Não permitir matrícula duplicada
 * - Validar conflito de horário do aluno
 * - Incrementar/decrementar contador da turma
 */
@ApplicationScoped
public class EnrollmentService {

    @Inject
    EnrollmentRepository enrollmentRepository;

    @Inject
    StudentRepository studentRepository;

    @Inject
    ClassRepository classRepository;

    /**
     * Cria uma nova matrícula.
     * 
     * VALIDAÇÕES IMPLEMENTADAS:
     * 1. Aluno deve existir e estar ativo
     * 2. Turma deve existir e estar ativa
     * 3. Turma deve ter vagas disponíveis
     * 4. Aluno não pode estar matriculado 2x na mesma turma
     * 5. Aluno não pode ter conflito de horário
     * 
     * @param enrollment Dados da matrícula
     * @return Matrícula criada
     */
    @Transactional
    public Enrollment create(CreateEnrollmentRequest dto) {

        Student student = studentRepository.findByIdOptional(dto.studentId)
                .orElseThrow(() -> new StudentNotFoundException(dto.studentId));

        if (!student.isActive) {
            throw new IllegalStateException(
                    "Não é possível matricular o aluno '" + student.name + "' pois ele está inativo.");
        }

        Class classEntity = classRepository.findByIdOptional(dto.classId)
                .orElseThrow(() -> new ClassNotFoundException(dto.classId));

        if (classEntity.status != Class.ClassStatus.ATIVA) {
            throw new IllegalStateException(
                    "Não é possível matricular na turma '" + classEntity.code +
                            "' pois ela não está ativa. Status atual: " + classEntity.status);
        }

        if (!classEntity.hasAvailableSlots()) {
            throw new ClassFullException(classEntity.code, classEntity.maxCapacity);
        }

        if (enrollmentRepository.isStudentEnrolledInClass(student.id, classEntity.id)) {
            throw new AlreadyEnrolledException(student.name, classEntity.code);
        }

        Schedule schedule = classEntity.schedule;
        if (enrollmentRepository.hasStudentScheduleConflict(student.id, schedule.id, null)) {
            String scheduleInfo = String.format("%s às %s",
                    schedule.dayOfWeek, schedule.startTime);
            throw new StudentScheduleConflictException(student.name, scheduleInfo);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.student = student;
        enrollment.classEntity = classEntity;
        enrollment.status = EnrollmentStatus.ATIVA;

        classEntity.incrementEnrollment();

        enrollmentRepository.persist(enrollment);
        return enrollment;
    }

    /**
     * Lista todas as matrículas.
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.listAll();
    }

    /**
     * Lista matrículas ativas.
     */
    public List<Enrollment> getActiveEnrollments() {
        return enrollmentRepository.findAllActive();
    }

    /**
     * Busca matrícula por ID.
     */
    public Enrollment findById(Long id) {
        return enrollmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
    }

    /**
     * Busca matrículas de um aluno.
     */
    public List<Enrollment> findByStudent(Long studentId) {
        return enrollmentRepository.findByStudent(studentId);
    }

    /**
     * Busca matrículas ativas de um aluno.
     * Requisito: listar disciplinas em que o aluno está matriculado.
     */
    public List<Enrollment> findActiveByStudent(Long studentId) {
        return enrollmentRepository.findActiveByStudent(studentId);
    }

    /**
     * Busca matrículas de uma turma.
     */
    public List<Enrollment> findByClass(Long classId) {
        return enrollmentRepository.findByClass(classId);
    }

    /**
     * Busca matrículas ativas de uma turma.
     */
    public List<Enrollment> findActiveByClass(Long classId) {
        return enrollmentRepository.findActiveByClass(classId);
    }

    /**
     * Busca matrículas de um aluno em um semestre.
     */
    public List<Enrollment> findByStudentAndSemester(Long studentId, String semester) {
        return enrollmentRepository.findByStudentAndSemester(studentId, semester);
    }

    /**
     * Busca matrículas por status.
     */
    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status);
    }

    /**
     * Busca matrículas de um curso.
     */
    public List<Enrollment> findByCourse(Long courseId) {
        return enrollmentRepository.findByCourse(courseId);
    }

    /**
     * Cancela uma matrícula.
     * 
     * 
     * @param id     ID da matrícula
     * @param reason Motivo do cancelamento
     */
    @Transactional
    public void cancel(Long id, String reason) {
        Enrollment enrollment = findById(id);

        if (enrollment.status == EnrollmentStatus.CANCELADA) {
            throw new IllegalStateException("Matrícula já está cancelada");
        }

        // Cancelar matrícula
        enrollment.cancel(reason);
        Class classEntity = enrollment.classEntity;
        classEntity.decrementEnrollment();
    }

    /**
     * Marca matrícula como concluída.
     * 
     * @param id         ID da matrícula
     * @param grade      Nota final (0-10)
     * @param attendance Frequência (0-100%)
     */
    @Transactional
    public void complete(Long id, BigDecimal grade, BigDecimal attendance) {
        Enrollment enrollment = findById(id);

        if (enrollment.status == EnrollmentStatus.CONCLUIDA) {
            throw new IllegalStateException("Matrícula já está concluída");
        }

        enrollment.complete(grade, attendance);
    }

    /**
     * Atualiza nota e frequência.
     */
    @Transactional
    public Enrollment updateGradeAndAttendance(Long id, BigDecimal grade, BigDecimal attendance) {
        Enrollment enrollment = findById(id);

        if (enrollment.status != EnrollmentStatus.ATIVA) {
            throw new IllegalStateException(
                    "Só é possível atualizar nota de matrículas ativas. Status atual: " + enrollment.status);
        }

        enrollment.finalGrade = grade;
        enrollment.attendance = attendance;

        return enrollment;
    }

    /**
     * Remove permanentemente uma matrícula.
     * 
     * ATENÇÃO: Remove registro do banco. Use cancel() para soft delete.
     */
    @Transactional
    public void delete(Long id) {
        Enrollment enrollment = findById(id);

        if (enrollment.status == EnrollmentStatus.ATIVA) {
            Class classEntity = enrollment.classEntity;
            classEntity.decrementEnrollment();
        }

        enrollmentRepository.delete(enrollment);
    }

    /**
     * Conta matrículas ativas de um aluno.
     */
    public long countActiveByStudent(Long studentId) {
        return enrollmentRepository.countActiveByStudent(studentId);
    }

    /**
     * Conta matrículas ativas de uma turma.
     */
    public long countActiveByClass(Long classId) {
        return enrollmentRepository.countActiveByClass(classId);
    }

    /**
     * Conta total de matrículas de um curso.
     */
    public long countByCourse(Long courseId) {
        return enrollmentRepository.countByCourse(courseId);
    }
}