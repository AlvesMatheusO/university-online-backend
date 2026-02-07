package br.edu.unifor.domain.repository;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Professor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProfessorRepository implements PanacheRepository<Professor> {

    /**
     * Busca um professor pelo e-mail.
     * Útil para validações de login ou verificação de duplicidade.
     */
    public Optional<Professor> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    /**
     * Busca professores por departamento.
     * Útil para os filtros de pesquisa exigidos pelo coordenador.
     */
    public java.util.List<Professor> findByDepartment(String department) {
        return list("department", department);
    }

    /**
     * Verifica se um professor existe pela maticula.
     * Requisito obrigatório para validar a criação de aulas na matriz.
     */
    public boolean existsByRegistration(String registration) {
        return count("registration", registration) > 0;
    }
}
