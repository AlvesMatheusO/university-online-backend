package br.edu.unifor.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class StudentRepository implements PanacheRepository<Student> {

    /**
     * Busca um aluno por email.
     * Útil para validações de login e verificação de duplicidade.
     */
    public Optional<Student> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    /**
     * Busca um aluno pela matrícula.
     * Requisito essencial para validar alunos ao fazer matrículas em aulas
     */
    public Optional<Student> findByRegistration(String registration) {
        return find("registration", registration).firstResultOptional();
    }

    /**
     * Busca um aluno por CPF
     * Evita duplicação de alunos no sistema
     */
    public Optional<Student> findByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional();
    }

    /**
     * Busca alunos por curso
     * Requisito do desafio: listar alunos de um curso específico
     */
    public List<Student> findByCourse(Long courseId) {
        return list("course.id", courseId);
    }

    /**
     * Busca apenas alunos ativos
     * Alunos inativos não aparecem nas listagens
     */
    public List<Student> findAllActive() {
        return list("active", true);
    }

     /**
     * Busca alunos ativos de um curso específico
     * Útil para relatórios e listagens filtradas
     */
    public List<Student> findActiveByCourse(Long courseId) {
        return list("course.id = ?1 and active = true", courseId);
    }

     /**
     * Verifica se existe um aluno com a matrícula informada
     * Requisito para validar unicidade ao gerar matrículas
     */
    public boolean existsByRegistration(String registration) {
        return count("registration", registration) > 0;
    }


    /**
     * Verifica se existe um aluno com o email informado
     * Evita duplicação de emails no sistema
     */
    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }

     /**
     * Verifica se existe um aluno com o CPF informado
     * Evita duplicação de CPFs no sistema
     */
    public boolean existsByCpf(String cpf) {
        return count("cpf", cpf) > 0;
    }


    /**
     * Conta quantos alunos ativos existem em um curso
     * Útil para relatórios e estatísticas
     */
    public long countActiveByCourse(Long courseId) {
        return count("course.id = ?1 and active = true", courseId);
    }

}