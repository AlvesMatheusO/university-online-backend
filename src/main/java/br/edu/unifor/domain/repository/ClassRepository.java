package br.edu.unifor.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Class;
import br.edu.unifor.domain.entity.Class.ClassStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ClassRepository implements PanacheRepository<Class> {

    // ========== BUSCAS BÁSICAS ==========

    public Optional<Class> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    // ========== BUSCAS POR RELACIONAMENTO ==========

    public List<Class> findBySubject(Long subjectId) {
        return list("subject.id", subjectId);
    }

    public List<Class> findByProfessor(Long professorId) {
        return list("professor.id", professorId);
    }

    public List<Class> findByCourse(Long courseId) {
        return list("course.id", courseId);
    }

    public List<Class> findBySchedule(Long scheduleId) {
        return list("schedule.id", scheduleId);
    }

    public List<Class> findBySemester(String semester) {
        return list("semester", semester);
    }

    // ========== BUSCAS COM STATUS ==========

    public List<Class> findAllActive() {
        return list("status", ClassStatus.ATIVA);
    }

    public List<Class> findActiveByCourse(Long courseId) {
        return list("course.id = ?1 and status = ?2", courseId, ClassStatus.ATIVA);
    }

    public List<Class> findActiveWithAvailableSlots() {
        return list("status = ?1 and enrolledStudents < maxCapacity", ClassStatus.ATIVA);
    }

    public List<Class> findActiveByCourseWithSlots(Long courseId) {
        return list(
            "course.id = ?1 and status = ?2 and enrolledStudents < maxCapacity",
            courseId, ClassStatus.ATIVA
        );
    }

    // ========== VALIDAÇÕES ==========

    /**
     * Verifica se professor tem conflito de horário.
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

    // ========== CONTADORES ==========

    public long countActiveByProfessor(Long professorId) {
        return count("professor.id = ?1 and status = ?2", professorId, ClassStatus.ATIVA);
    }

    public long countActiveByCourse(Long courseId) {
        return count("course.id = ?1 and status = ?2", courseId, ClassStatus.ATIVA);
    }

    public long countActiveBySubject(Long subjectId) {
        return count("subject.id = ?1 and status = ?2", subjectId, ClassStatus.ATIVA);
    }
}