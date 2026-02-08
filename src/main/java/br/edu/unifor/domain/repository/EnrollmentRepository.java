package br.edu.unifor.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Enrollment;
import br.edu.unifor.domain.entity.Enrollment.EnrollmentStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * Repository para gerenciar matrículas.
 */
@ApplicationScoped
public class EnrollmentRepository implements PanacheRepository<Enrollment> {

    /**
     * Busca matrículas de um aluno.
     */
    public List<Enrollment> findByStudent(Long studentId) {
        return list("student.id", studentId);
    }

    /**
     * Busca matrículas ativas de um aluno.
     * Requisito: listar disciplinas em que o aluno está matriculado.
     */
    public List<Enrollment> findActiveByStudent(Long studentId) {
        return list("student.id = ?1 and status = ?2", studentId, EnrollmentStatus.ATIVA);
    }

    /**
     * Busca matrículas de uma turma.
     */
    public List<Enrollment> findByClass(Long classId) {
        return list("classEntity.id", classId);
    }

    /**
     * Busca matrículas ativas de uma turma.
     */
    public List<Enrollment> findActiveByClass(Long classId) {
        return list("classEntity.id = ?1 and status = ?2", classId, EnrollmentStatus.ATIVA);
    }

    /**
     * Busca matrícula específica de um aluno em uma turma.
     * Usado para validar duplicação.
     */
    public Optional<Enrollment> findByStudentAndClass(Long studentId, Long classId) {
        return find("student.id = ?1 and classEntity.id = ?2", studentId, classId)
                .firstResultOptional();
    }

    /**
     * Verifica se aluno já está matriculado na turma.
     * 
     * @param studentId ID do aluno
     * @param classId ID da turma
     * @return true se já existe matrícula ativa
     */
    public boolean isStudentEnrolledInClass(Long studentId, Long classId) {
        return count("student.id = ?1 and classEntity.id = ?2 and status = ?3", 
                     studentId, classId, EnrollmentStatus.ATIVA) > 0;
    }

    /**
     * Verifica conflito de horário do aluno.
     * 
     * Um aluno NÃO pode ter 2 turmas no mesmo horário.
     * 
     * @param studentId ID do aluno
     * @param scheduleId ID do horário
     * @param excludeEnrollmentId ID da matrícula a excluir (para updates)
     * @return true se há conflito
     */
    public boolean hasStudentScheduleConflict(Long studentId, Long scheduleId, Long excludeEnrollmentId) {
        String query = "student.id = ?1 and classEntity.schedule.id = ?2 and status = ?3";
        List<Enrollment> conflicts;
        
        if (excludeEnrollmentId != null) {
            conflicts = list(query + " and id != ?4", 
                studentId, scheduleId, EnrollmentStatus.ATIVA, excludeEnrollmentId);
        } else {
            conflicts = list(query, studentId, scheduleId, EnrollmentStatus.ATIVA);
        }
        
        return !conflicts.isEmpty();
    }

    /**
     * Conta matrículas ativas de um aluno.
     * Usado para validação de remoção de aluno.
     */
    public long countActiveByStudent(Long studentId) {
        return count("student.id = ?1 and status = ?2", studentId, EnrollmentStatus.ATIVA);
    }

    /**
     * Conta matrículas ativas de uma turma.
     * Usado para validação de remoção de turma.
     */
    public long countActiveByClass(Long classId) {
        return count("classEntity.id = ?1 and status = ?2", classId, EnrollmentStatus.ATIVA);
    }

    /**
     * Busca todas as matrículas ativas.
     */
    public List<Enrollment> findAllActive() {
        return list("status", EnrollmentStatus.ATIVA);
    }

    /**
     * Busca matrículas por status.
     */
    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        return list("status", status);
    }

    /**
     * Busca matrículas de um aluno em um semestre específico.
     */
    public List<Enrollment> findByStudentAndSemester(Long studentId, String semester) {
        return list("student.id = ?1 and classEntity.semester = ?2", studentId, semester);
    }

    /**
     * Busca matrículas de um curso (através das turmas).
     */
    public List<Enrollment> findByCourse(Long courseId) {
        return list("classEntity.course.id", courseId);
    }

    /**
     * Conta total de matrículas de um curso.
     */
    public long countByCourse(Long courseId) {
        return count("classEntity.course.id", courseId);
    }
}