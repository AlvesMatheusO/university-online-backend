package br.edu.unifor.domain.repository;

import java.util.List;
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
     * Busca um professor pela matrícula.
     */
    public Optional<Professor> findByRegistration(String registration) {
        return find("registration", registration).firstResultOptional();
    }

    /**
     * Busca professores por departamento.
     */
    public List<Professor> findByDepartment(String department) {
        return list("department", department);
    }

    /**
     * Verifica se um professor existe pela matrícula.
     */
    public boolean existsByRegistration(String registration) {
        return count("registration", registration) > 0;
    }
    
    /**
     * Verifica se um email já está cadastrado.
     */
    public boolean existsByEmail(String email) {  
        return count("email", email) > 0;
    }
}