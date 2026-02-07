package br.edu.unifor.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.domain.entity.Professor;
import br.edu.unifor.domain.repository.ProfessorRepository;

@ApplicationScoped
public class ProfessorService {

    @Inject
    ProfessorRepository professorRepository;

    @Inject
    RegistrationService registrationService;

    @Transactional
    public Professor creatProfessor(Professor professor) {
        professor.registration = registrationService.generateUnique(professorRepository::existsByRegistration);

        professorRepository.persist(professor);
        return professor;
    }
}
