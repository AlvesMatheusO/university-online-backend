package br.edu.unifor.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class StudentRepository implements PanacheRepository<Student> {

    public Optional<Student> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<Student> findByRegistration(String registration) {
        return find("registration", registration).firstResultOptional();
    }

    public Optional<Student> findByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional();
    }

    public List<Student> findByCourse(Long courseId) {
        return list("course.id", courseId);
    }

    public List<Student> findAllActive() {
        return list("active", true);
    }

    public List<Student> findActiveByCourse(Long courseId) {
        return list("course.id = ?1 and active = true", courseId);
    }

    public boolean existsByRegistration(String registration) {
        return count("registration", registration) > 0;
    }

    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }

    public boolean existsByCpf(String cpf) {
        return count("cpf", cpf) > 0;
    }

    public long countActiveByCourse(Long courseId) {
        return count("course.id = ?1 and active = true", courseId);
    }

    // ========== MÉTODOS ADICIONAIS NECESSÁRIOS ==========

    /**
     * Busca aluno por username (para autenticação Keycloak)
     */
    public Optional<Student> findByUsername(String username) {
        return find("username = ?1", username).firstResultOptional();
    }

    /**
     * Retorna o ID do curso do aluno pelo username
     */
    public Long getCourseIdByUsername(String username) {
        String query = "SELECT s.course.id FROM Student s WHERE s.username = ?1";
        return getEntityManager()
                .createQuery(query, Long.class)
                .setParameter(1, username)
                .getSingleResult();
    }

    /**
     * Lista alunos matriculados em uma turma específica
     */
    public List<Student> findByClassId(Long classId) {
        String query = """
            SELECT DISTINCT s 
            FROM Student s
            JOIN Enrollment e ON e.student.id = s.id
            WHERE e.classEntity.id = ?1
            AND e.active = true
            ORDER BY s.name
            """;
        
        return getEntityManager()
                .createQuery(query, Student.class)
                .setParameter(1, classId)
                .getResultList();
    }

    /**
     * Conta quantos alunos estão matriculados em uma turma
     */
    public long countByClassId(Long classId) {
        String query = """
            SELECT COUNT(DISTINCT s) 
            FROM Student s
            JOIN Enrollment e ON e.student.id = s.id
            WHERE e.classEntity.id = ?1
            AND e.active = true
            """;
        
        return getEntityManager()
                .createQuery(query, Long.class)
                .setParameter(1, classId)
                .getSingleResult();
    }

    /**
     * Conta matrículas ativas de um aluno
     */
    public long countActiveEnrollments(Long studentId) {
        String query = "SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = ?1 AND e.active = true";
        return getEntityManager()
                .createQuery(query, Long.class)
                .setParameter(1, studentId)
                .getSingleResult();
    }

    /**
     * Lista alunos de um curso específico (usado pelo COORDINATOR)
     */
    public List<Student> findByCourseId(Long courseId) {
        String query = """
            SELECT s 
            FROM Student s
            WHERE s.course.id = ?1
            AND s.active = true
            ORDER BY s.name
            """;
        
        return getEntityManager()
                .createQuery(query, Student.class)
                .setParameter(1, courseId)
                .getResultList();
    }
}