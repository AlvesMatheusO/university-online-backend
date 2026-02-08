package br.edu.unifor.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import br.edu.unifor.domain.entity.Course;
import br.edu.unifor.domain.entity.Student;
import br.edu.unifor.domain.repository.CourseRepository;
import br.edu.unifor.domain.repository.StudentRepository;
import br.edu.unifor.infrastructure.exception.CourseNotFoundException;
import br.edu.unifor.infrastructure.exception.CpfAlreadyExistsException;
import br.edu.unifor.infrastructure.exception.EmailAlreadyExistException;
import br.edu.unifor.infrastructure.exception.StudentHasActiveEnrollmentsException;
import br.edu.unifor.infrastructure.exception.StudentNotFoundException;

@ApplicationScoped
public class StudentService {

    @Inject
    StudentRepository studentRepository;

    @Inject
    CourseRepository courseRepository;

    @Inject
    RegistrationService registrationService;

    /**
     * Cria um novo aluno no sistema.
     * 
     * Validações:
     * - Email deve ser único
     * - CPF deve ser único
     * - Curso deve existir
     * - Matrícula é gerada automaticamente
     * 
     * @param student Dados do aluno a ser criado
     * @return Aluno criado com matrícula gerada
     * @throws EmailAlreadyExistException se email já estiver cadastrado
     * @throws CpfAlreadyExistsException  se CPF já estiver cadastrado
     * @throws CourseNotFoundException    se curso não existir
     */

    @Transactional
    public Student createStudent(@Valid Student student) {

        // Validar email duplicado
        if (studentRepository.existsByEmail(student.email)) {
            throw new EmailAlreadyExistException(student.email);
        }

        // Validar CPF duplicado
        if (studentRepository.existsByCpf(student.cpf)) {
            throw new CpfAlreadyExistsException(student.cpf);
        }

        // Validar se o curso existe
        Course course = courseRepository.findByIdOptional(student.course.id)
                .orElseThrow(() -> new CourseNotFoundException(student.course.id));

        student.course = course;

        // Gerar matrícula única
        student.registration = registrationService.generateUnique(
                studentRepository::existsByRegistration);

        // Garantir que o aluno começa ativo
        student.isActive = true;

        studentRepository.persist(student);
        return student;
    }

    /**
     * Lista todos os alunos cadastrados (ativos e inativos).
     */
    public List<Student> getAllStudents() {
        return studentRepository.listAll();
    }

    /**
     * Lista apenas alunos ativos.
     */
    public List<Student> getActiveStudents() {
        return studentRepository.findAllActive();
    }

    /**
     * Busca um aluno por ID.
     * 
     * @param id ID do aluno
     * @return Aluno encontrado
     * @throws StudentNotFoundException se aluno não existir
     */
    public Student findById(Long id) {
        return studentRepository.findByIdOptional(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    /**
     * Busca um aluno pela matrícula.
     * 
     * @param registration Matrícula do aluno
     * @return Aluno encontrado
     * @throws StudentNotFoundException se aluno não existir
     */
    public Student findByRegistration(String registration) {
        return studentRepository.findByRegistration(registration)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Aluno não encontrado com matrícula: " + registration));
    }

    /**
     * Busca um aluno por email.
     * 
     * @param email Email do aluno
     * @return Aluno encontrado
     * @throws StudentNotFoundException se aluno não existir
     */
    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Aluno não encontrado com email: " + email));
    }

    /**
     * Busca alunos por curso.
     * 
     * @param courseId ID do curso
     * @return Lista de alunos do curso
     */
    public List<Student> findByCourse(Long courseId) {
        return studentRepository.findByCourse(courseId);
    }

    /**
     * Busca alunos ativos de um curso.
     * 
     * @param courseId ID do curso
     * @return Lista de alunos ativos do curso
     */
    public List<Student> findActiveByCourse(Long courseId) {
        return studentRepository.findActiveByCourse(courseId);
    }

    /**
     * Atualiza dados de um aluno.
     * A matrícula não pode ser alterada.
     * 
     * @param id                ID do aluno a ser atualizado
     * @param studentAtualizado Dados atualizados
     * @return Aluno atualizado
     * @throws StudentNotFoundException   se aluno não existir
     * @throws EmailAlreadyExistException se email já estiver em uso
     * @throws CpfAlreadyExistsException  se CPF já estiver em uso
     */

    @Transactional
    public Student update(Long id, Student studentAtualizado) {
        Student student = findById(id);

        // Validar email se foi alterado
        if (!student.email.equals(studentAtualizado.email)) {
            if (studentRepository.existsByEmail(studentAtualizado.email)) {
                throw new EmailAlreadyExistException(studentAtualizado.email);
            }
        }

        // Validar CPF se foi alterado
        if (!student.cpf.equals(studentAtualizado.cpf)) {
            if (studentRepository.existsByCpf(studentAtualizado.cpf)) {
                throw new CpfAlreadyExistsException(studentAtualizado.cpf);
            }
        }

        // Atualizar campos (matrícula não muda)
        student.name = studentAtualizado.name;
        student.email = studentAtualizado.email;
        student.cpf = studentAtualizado.cpf;
        student.phone = studentAtualizado.phone;

        // Validar e atualizar curso se mudou
        if (!student.course.id.equals(studentAtualizado.course.id)) {
            Course course = courseRepository.findByIdOptional(studentAtualizado.course.id)
                    .orElseThrow(() -> new CourseNotFoundException(studentAtualizado.course.id));
            student.course = course;
        }

        return student;
    }

    /**
     * Inativa um aluno
     * Alunos inativos não podem fazer novas matrículas
     * 
     * @param id ID do aluno
     * @throws StudentNotFoundException se aluno não existir
     */
    @Transactional
    public void inactivate(Long id) {
        Student student = findById(id);
        student.isActive = false;
    }

    /**
     * Reativa um aluno inativo
     * 
     * @param id ID do aluno
     * @throws StudentNotFoundException se aluno não existir
     */
    @Transactional
    public void activate(Long id) {
        Student student = findById(id);
        student.isActive = true;
    }

    /**
     * Remove permanentemente um aluno do sistema
     * 
     * @param id ID do aluno
     * @throws StudentNotFoundException se aluno não existir
     */
    @Transactional
    public void delete(Long id) {
        Student student = findById(id);
        long activeEnrollments = countActiveByCourse(student.id);
        if (activeEnrollments > 0) {
            throw new StudentHasActiveEnrollmentsException(student.id, activeEnrollments);
        }
        studentRepository.delete(student);
    }

    /**
     * Conta quantos alunos ativos existem em um curso.
     * 
     * @param courseId ID do curso
     * @return Número de alunos ativos no curso
     */
    public long countActiveByCourse(Long courseId) {
        return studentRepository.countActiveByCourse(courseId);
    }

}
