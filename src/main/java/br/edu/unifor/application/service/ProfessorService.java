package br.edu.unifor.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.application.dto.request.CreateProfessorRequest;
import br.edu.unifor.application.dto.request.UpdateProfessorRequest;
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
    public Professor create(CreateProfessorRequest dto) {

        if (professorRepository.existsByEmail(dto.email)) {
            throw new EmailAlreadyExistException(dto.email);
        }

        Professor professor = new Professor();
        professor.name = dto.name;
        professor.email = dto.email;
        professor.title = dto.title;
        professor.department = dto.department;

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
    public Professor update(Long id, UpdateProfessorRequest dto) {
        Professor professor = findById(id);

        if (!professor.email.equals(dto.email)) {
            if (professorRepository.existsByEmail(dto.email)) {
                throw new EmailAlreadyExistException(dto.email);
            }
        }

        professor.name = dto.name;
        professor.email = dto.email;
        professor.title = dto.title;
        professor.department = dto.department;

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