package br.edu.unifor.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Coordinator;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CoordinatorRepository implements PanacheRepository<Coordinator> {
    /**
     * Busca um coordenador por email.
     * Útil para validações de login e verificação de duplicidade.
     */
    public Optional<Coordinator> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    /**
     * Busca um coordenador pela matrícula.
     */
    public Optional<Coordinator> findByRegistration(String registration) {
        return find("registration", registration).firstResultOptional();
    }

    /**
     * Busca coordenadores por departamento.
     */
    public List<Coordinator> findByDepartment(String department) {
        return list("department", department);
    }

    /**
     * Busca apenas coordenadores ativos.
     */
    public List<Coordinator> findAllActive() {
        return list("active", true);
    }

    /**
     * Busca coordenadores que gerenciam um curso específico.
     * Requisito do desafio: encontrar coordenadores responsáveis por um curso.
     * 
     * @param courseId ID do curso
     * @return Lista de coordenadores desse curso
     */
    public List<Coordinator> findByCourse(Long courseId) {
        return list("SELECT c FROM Coordinator c JOIN c.courses co WHERE co.id = ?1", courseId);
    }

    /**
     * Busca coordenadores ativos de um curso específico.
     * 
     * @param courseId ID do curso
     * @return Lista de coordenadores ativos desse curso
     */
    public List<Coordinator> findActiveByCourse(Long courseId) {
        return list("SELECT c FROM Coordinator c JOIN c.courses co WHERE co.id = ?1 AND c.active = true", courseId);
    }

    /**
     * Verifica se existe um coordenador com a matrícula informada.
     */
    public boolean existsByRegistration(String registration) {
        return count("registration", registration) > 0;
    }

    /**
     * Verifica se existe um coordenador com o email informado.
     */
    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }

    /**
     * Verifica se um curso específico tem coordenadores ativos.
     * Requisito: um curso deve ter pelo menos um coordenador ativo.
     * 
     * @param courseId ID do curso
     * @return true se o curso tem coordenadores ativos
     */
    public boolean courseHasActiveCoordinators(Long courseId) {
        return count(
                "SELECT COUNT(c) FROM Coordinator c JOIN c.courses co WHERE co.id = ?1 AND c.active = true",
                courseId) > 0;
    }

    /**
 * Verifica se um coordenador coordena algum dos cursos de uma turma
 */
public boolean coordinatesAnyCourseOfClass(String coordinatorUsername, Long classId) {
    String query = """
        SELECT COUNT(c) > 0 
        FROM Coordinator coord
        JOIN coord.courses course
        JOIN Class c 
        JOIN c.courses classCourse
        WHERE coord.username = ?1 
        AND c.id = ?2
        AND course.id = classCourse.id
        AND coord.active = true
        """;
    
    return getEntityManager()
            .createQuery(query, Boolean.class)
            .setParameter(1, coordinatorUsername)
            .setParameter(2, classId)
            .getSingleResult();
}

/**
 * Retorna IDs dos cursos que um coordenador gerencia
 */
public List<Long> findCourseIdsByUsername(String username) {
    String query = """
        SELECT course.id 
        FROM Coordinator coord
        JOIN coord.courses course
        WHERE coord.username = ?1 
        AND coord.active = true
        """;
    
    return getEntityManager()
            .createQuery(query, Long.class)
            .setParameter(1, username)
            .getResultList();
}
    
    /**
     * Conta quantos cursos um coordenador gerencia.
     * 
     * @param coordinatorId ID do coordenador
     * @return Número de cursos sob coordenação
     */
    public long countCoursesByCoordinator(Long coordinatorId) {
        return getEntityManager()
                .createQuery(
                        "SELECT COUNT(co) FROM Coordinator c JOIN c.courses co WHERE c.id = :id",
                        Long.class)
                .setParameter("id", coordinatorId)
                .getSingleResult();
    }

}
