package br.edu.unifor.domain.repository;

import br.edu.unifor.domain.entity.Subject;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Repository para operações com Disciplina.
 * 
 * Panache já fornece métodos básicos como:
 * - findAll()
 * - findById(Long id)
 * - persist(Disciplina)
 * - delete(Disciplina)
 */

@ApplicationScoped
public class SubjectRepository implements PanacheRepository<Subject> {

    /**
     * Busca disciplina por código
     */
    public Optional<Subject> findByCode(String codigo) {
        return find("code", codigo).firstResultOptional();
    }

    /**
     * Verifica se já existe disciplina com o código informado
     */
    public boolean existsByCode(String codigo) {
       return count("code", codigo) > 0;
    }
    
}