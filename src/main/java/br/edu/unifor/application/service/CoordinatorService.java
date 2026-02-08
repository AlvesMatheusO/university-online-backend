package br.edu.unifor.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.application.dto.request.coordinator.CreateCoordinatorRequest;
import br.edu.unifor.application.dto.request.coordinator.UpdateCoordinatorRequest;
import br.edu.unifor.domain.entity.Coordinator;
import br.edu.unifor.domain.entity.Course;
import br.edu.unifor.domain.repository.CoordinatorRepository;
import br.edu.unifor.domain.repository.CourseRepository;
import br.edu.unifor.infrastructure.exception.CoordinatorHasActiveCoursesException;
import br.edu.unifor.infrastructure.exception.CoordinatorNotFoundException;
import br.edu.unifor.infrastructure.exception.CourseNotFoundException;
import br.edu.unifor.infrastructure.exception.EmailAlreadyExistException;

/**
 * Service responsável pela lógica de negócios relacionada aos coordenadores
 * acadêmicos.
 * Responsável por criar, atualizar, buscar e inativar coordenadores,
 * além de gerenciar o relacionamento Many-to-Many com cursos.
 */

@ApplicationScoped
public class CoordinatorService {
    @Inject
    CoordinatorRepository coordinatorRepository;

    @Inject
    CourseRepository courseRepository;

    @Inject
    RegistrationService registrationService;

    /**
     * Cria um novo coordenador no sistema.
     * 
     * Validações:
     * - Email deve ser único
     * - Matrícula é gerada automaticamente
     * 
     * @param coordinator Dados do coordenador
     * @return Coordenador criado com matrícula gerada
     */

    @Transactional
    public Coordinator create(CreateCoordinatorRequest dto) {

        // Validar email duplicado
        if (coordinatorRepository.existsByEmail(dto.email)) {
            throw new EmailAlreadyExistException(dto.email);
        }

        Coordinator coordinator = new Coordinator();
        coordinator.name = dto.name;
        coordinator.email = dto.email;
        coordinator.phone = dto.phone;
        coordinator.department = dto.department;

        // Gerar matrícula única
        coordinator.registration = registrationService.generateUnique(
                coordinatorRepository::existsByRegistration);

        coordinator.active = true;

        coordinatorRepository.persist(coordinator);
        return coordinator;
    }

    /**
     * Lista todos os coordenadores (ativos e inativos).
     */
    public List<Coordinator> getAllCoordinators() {
        return coordinatorRepository.listAll();
    }

    /**
     * Lista apenas coordenadores ativos.
     */
    public List<Coordinator> getActiveCoordinators() {
        return coordinatorRepository.findAllActive();
    }

    /**
     * Busca um coordenador por ID.
     */
    public Coordinator findById(Long id) {
        return coordinatorRepository.findByIdOptional(id)
                .orElseThrow(() -> new CoordinatorNotFoundException(id));
    }

    /**
     * Busca um coordenador pela matrícula.
     */
    public Coordinator findByRegistration(String registration) {
        return coordinatorRepository.findByRegistration(registration)
                .orElseThrow(() -> new CoordinatorNotFoundException(
                        "Coordenador não encontrado com matrícula: " + registration));
    }

    /**
     * Busca um coordenador por email.
     */
    public Coordinator findByEmail(String email) {
        return coordinatorRepository.findByEmail(email)
                .orElseThrow(() -> new CoordinatorNotFoundException(
                        "Coordenador não encontrado com email: " + email));
    }

    /**
     * Busca coordenadores por departamento.
     */
    public List<Coordinator> findByDepartment(String department) {
        return coordinatorRepository.findByDepartment(department);
    }

    /**
     * Busca coordenadores que gerenciam um curso específico.
     * 
     */
    public List<Coordinator> findByCourse(Long courseId) {
        return coordinatorRepository.findByCourse(courseId);
    }

    /**
     * Busca coordenadores ativos de um curso.
     */
    public List<Coordinator> findActiveByCourse(Long courseId) {
        return coordinatorRepository.findActiveByCourse(courseId);
    }

    /**
     * Adiciona um curso à coordenação de um coordenador.
     * 
     * @param coordinatorId ID do coordenador
     * @param courseId      ID do curso
     */
    @Transactional
    public Coordinator addCourse(Long coordinatorId, Long courseId) {
        Coordinator coordinator = findById(coordinatorId);

        // Validar se o curso existe
        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        // Adicionar curso (método addCourse já verifica duplicação)
        coordinator.addCourse(course);

        return coordinator;
    }

    /**
     * Remove um curso da coordenação de um coordenador.
     * 
     * VALIDAÇÃO: Não permitir remoção se for o último coordenador ativo do curso.
     * 
     * @param coordinatorId ID do coordenador
     * @param courseId      ID do curso
     */
    @Transactional
    public Coordinator removeCourse(Long coordinatorId, Long courseId) {
        Coordinator coordinator = findById(coordinatorId);

        // Validar se o curso existe
        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        // Validar se não é o último coordenador ativo do curso
        List<Coordinator> activeCoordinators = coordinatorRepository.findActiveByCourse(courseId);
        if (activeCoordinators.size() == 1 && activeCoordinators.get(0).id.equals(coordinatorId)) {
            throw new IllegalStateException(
                    "Não é possível remover o curso. Este é o último coordenador ativo do curso '" +
                            course.name + "'. Um curso deve ter pelo menos um coordenador ativo.");
        }

        coordinator.removeCourse(course);

        return coordinator;
    }

    /**
     * Atualiza dados de um coordenador.
     * A matrícula não pode ser alterada.
     * 
     * @param id                    ID do coordenador
     * @param coordinatorAtualizado Dados atualizados
     * @return Coordenador atualizado
     */
    @Transactional
    public Coordinator update(Long id, UpdateCoordinatorRequest dto) {
        Coordinator coordinator = findById(id);

        // Validar email se mudou
        if (!coordinator.email.equals(dto.email)) {
            if (coordinatorRepository.existsByEmail(dto.email)) {
                throw new EmailAlreadyExistException(dto.email);
            }
        }

        coordinator.name = dto.name;
        coordinator.email = dto.email;
        coordinator.phone = dto.phone;
        coordinator.department = dto.department;

        return coordinator;
    }

    /**
     * Inativa um coordenador (soft delete).
     */
    @Transactional
    public void inactivate(Long id) {
        Coordinator coordinator = findById(id);
        coordinator.active = false;
    }

    /**
     * Reativa um coordenador inativo.
     */
    @Transactional
    public void activate(Long id) {
        Coordinator coordinator = findById(id);
        coordinator.active = true;
    }

    /**
     * Remove permanentemente um coordenador (hard delete).
     * 
     * Validações:
     * - Não pode ter cursos vinculados
     * - Deve usar inactivate() se tiver cursos
     */
    @Transactional
    public void delete(Long id) {
        Coordinator coordinator = findById(id);

        // Validar se tem cursos vinculados
        if (coordinator.courses != null && !coordinator.courses.isEmpty()) {
            throw new CoordinatorHasActiveCoursesException(id, coordinator.courses.size());
        }

        coordinatorRepository.delete(coordinator);
    }

    /**
     * Conta quantos cursos um coordenador gerencia.
     */
    public long countCourses(Long coordinatorId) {
        return coordinatorRepository.countCoursesByCoordinator(coordinatorId);
    }

}
