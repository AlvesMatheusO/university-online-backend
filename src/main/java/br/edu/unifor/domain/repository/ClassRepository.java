package br.edu.unifor.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Class;
import br.edu.unifor.domain.entity.Class.ClassStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ClassRepository implements PanacheRepository<Class> {

    /**
     * Busca uma turma por código.
     */
    public Optional<Class> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }

    /**
     * Busca turmas por disciplina.
     * Requisito: listar todas as turmas de uma disciplina.
     */
    public List<Class> findBySubject(Long subjectId) {
        return list("subject.id", subjectId);
    }

    /**
     * Busca turmas por professor.
     * Requisito: listar todas as turmas de um professor.
     */
    public List<Class> findByProfessor(Long professorId) {
        return list("professor.id", professorId);
    }

    /**
     * Busca turmas por curso.
     * Requisito: listar turmas de um curso específico.
     */
    public List<Class> findByCourse(Long courseId) {
        return list("course.id", courseId);
    }

    /**
     * Busca turmas por horário.
     * Usado para validar conflitos de horário.
     */
    public List<Class> findBySchedule(Long scheduleId) {
        return list("schedule.id", scheduleId);
    }

    /**
     * Busca turmas por semestre.
     */
    public List<Class> findBySemester(String semester) {
        return list("semester", semester);
    }

    /**
     * Busca turmas ativas.
     */
    public List<Class> findAllActive() {
        return list("status", ClassStatus.ATIVA);
    }

    /**
     * Busca turmas ativas de um curso.
     */
    public List<Class> findActiveByCourse(Long courseId) {
        return list("course.id = ?1 and status = ?2", courseId, ClassStatus.ATIVA);
    }

    /**
     * Busca turmas ativas com vagas disponíveis.
     * Requisito: aluno só pode se matricular em turma com vagas.
     */
    public List<Class> findActiveWithAvailableSlots() {
        return list("status = ?1 and enrolledStudents < maxCapacity", ClassStatus.ATIVA);
    }

    /**
     * Busca turmas ativas de um curso com vagas disponíveis.
     */
    public List<Class> findActiveByCourseWithSlots(Long courseId) {
        return list(
                "course.id = ?1 and status = ?2 and enrolledStudents < maxCapacity",
                courseId,
                ClassStatus.ATIVA);
    }

    /**
     * VALIDAÇÃO CRÍTICA: Verifica se professor tem conflito de horário.
     * 
     * Um professor NÃO pode ter 2 turmas no mesmo horário.
     * 
     * @param professorId    ID do professor
     * @param scheduleId     ID do horário
     * @param excludeClassId ID da turma a excluir da verificação (para updates)
     * @return true se há conflito
     */
    public boolean hasProfessorScheduleConflict(Long professorId, Long scheduleId, Long excludeClassId) {
        String query = "professor.id = ?1 and schedule.id = ?2 and status = ?3";
        List<Class> conflicts;

        if (excludeClassId != null) {
            conflicts = list(query + " and id != ?4",
                    professorId, scheduleId, ClassStatus.ATIVA, excludeClassId);
        } else {
            conflicts = list(query, professorId, scheduleId, ClassStatus.ATIVA);
        }

        return !conflicts.isEmpty();
    }

    /**
     * Verifica se existe uma turma com o código informado.
     */
    public boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    /**
     * Conta quantas turmas ativas um professor tem.
     */
    public long countActiveByProfessor(Long professorId) {
        return count("professor.id = ?1 and status = ?2", professorId, ClassStatus.ATIVA);
    }

    /**
     * Conta quantas turmas ativas existem em um curso.
     */
    public long countActiveByCourse(Long courseId) {
        return count("course.id = ?1 and status = ?2", courseId, ClassStatus.ATIVA);
    }

    public long countActiveBySubject(Long subjectId) {
        return count("subject.id = ?1 and status = ?2", subjectId, ClassStatus.ATIVA);
    }
}
