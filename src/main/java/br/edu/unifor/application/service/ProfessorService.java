package br.edu.unifor.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import br.edu.unifor.domain.entity.Professor;
import br.edu.unifor.domain.repository.ProfessorRepository;
import br.edu.unifor.infrastructure.exception.EmailAlreadyExistException;
import br.edu.unifor.infrastructure.exception.ProfessorNotFoundException;

@ApplicationScoped
public class ProfessorService {

    @Inject
    ProfessorRepository professorRepository;

    @Inject
    RegistrationService registrationService;

    /**
     * Cria um novo professor no sistema.
     * Gera matrícula automaticamente e valida email único.
     */
    @Transactional
    public Professor createProfessor(@Valid Professor professor) {

        if (professorRepository.existsByEmail(professor.email)) {
            throw new EmailAlreadyExistException(professor.email);
        }

        // Gerar matrícula única
        professor.registration = registrationService.generateUnique(
                professorRepository::existsByRegistration);

        professorRepository.persist(professor);
        return professor;
    }

    /**
     * Lista todos os professores cadastrados.
     */
    public List<Professor> getAllProfessors() {
        return professorRepository.listAll();
    }

    /**
     * Busca um professor por ID.
     */
    public Professor findById(Long id) {
        return professorRepository.findByIdOptional(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
    }

    /**
     * Busca um professor pela matrícula.
     */
    public Professor findByRegistration(String registration) {
        return professorRepository.findByRegistration(registration)
                .orElseThrow(() -> new ProfessorNotFoundException(
                        "Professor não encontrado com matrícula: " + registration));
    }

    /**
     * Busca um professor por email.
     */
    public Professor findByEmail(String email) {
        return professorRepository.findByEmail(email)
                .orElseThrow(() -> new ProfessorNotFoundException(
                        "Professor não encontrado com email: " + email));
    }

    /**
     * Busca professores por departamento.
     */
    public List<Professor> findByDepartment(String department) {
        return professorRepository.findByDepartment(department);
    }

    /**
     * Atualiza dados de um professor.
     * A matrícula não pode ser alterada.
     */
    @Transactional
    public Professor update(Long id, Professor professorAtualizado) {
        Professor professor = findById(id);

        // Validar email se foi alterado
        if (!professor.email.equals(professorAtualizado.email)) {
            if (professorRepository.existsByEmail(professorAtualizado.email)) {
                throw new EmailAlreadyExistException(professorAtualizado.email);
            }
        }

        // Atualizar campos (matrícula não muda)
        professor.name = professorAtualizado.name;
        professor.email = professorAtualizado.email;
        professor.title = professorAtualizado.title;
        professor.department = professorAtualizado.department;

        return professor;
    }

    /**
     * Remove um professor do sistema.
     * Validar se professor não tem aulas vinculadas
     */
    @Transactional
    public void delete(Long id) {
        Professor professor = findById(id);
        professorRepository.delete(professor);
    }

    
}