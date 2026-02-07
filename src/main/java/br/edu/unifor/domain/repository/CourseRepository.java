package br.edu.unifor.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Course;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CourseRepository implements PanacheRepository<Course> {
    /**
     * Retorna apenas cursos ativos.
     * Regra de negócio: cursos inativos não devem ser usados no sistema.
     */

    public List<Course> findAllActive() {
        return list("active", true);
    }

    /**
     * Busca curso ativo pelo código.
     * Usado para validações e matrícula.
     */
    public Optional<Course> findActiveByCode(String code) {
        return find("code = ?1 and active = true", code)
                .firstResultOptional();
    }

    /**
     * Verifica se já existe curso com o código informado.
     */
    public boolean existsByCode(String code) {
        return find("code", code).count() > 0;
    }

    /**
     * Desativação lógica do curso.
     * Regra: cursos não devem ser apagados fisicamente.
     */
    public void deactivate(Course course) {
        course.active = false;
        persist(course);
    }
}
