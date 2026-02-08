package br.edu.unifor.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.application.dto.request.student.CreateStudentRequest;
import br.edu.unifor.application.dto.request.student.UpdateStudentRequest;
import br.edu.unifor.domain.entity.Course;
import br.edu.unifor.domain.entity.Student;
import br.edu.unifor.domain.repository.CourseRepository;
import br.edu.unifor.domain.repository.EnrollmentRepository;
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

    @Inject
    EnrollmentRepository enrollmentRepository;

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
    public Student create(CreateStudentRequest dto) {

        if (studentRepository.existsByEmail(dto.email)) {
            throw new EmailAlreadyExistException(dto.email);
        }

        if (studentRepository.existsByCpf(dto.cpf)) {
            throw new CpfAlreadyExistsException(dto.cpf);
        }

        Course course = courseRepository.findByIdOptional(dto.courseId)
                .orElseThrow(() -> new CourseNotFoundException(dto.courseId));

        Student student = new Student();
        student.name = dto.name;
        student.email = dto.email;
        student.cpf = dto.cpf;
        student.phone = dto.phone;
        student.course = course;

        student.registration = registrationService.generateUnique(
                studentRepository::existsByRegistration);

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
    public Student update(Long id, UpdateStudentRequest dto) {
        Student student = findById(id);

        if (!student.email.equals(dto.email)) {
            if (studentRepository.existsByEmail(dto.email)) {
                throw new EmailAlreadyExistException(dto.email);
            }
        }

        if (!student.cpf.equals(dto.cpf)) {
            if (studentRepository.existsByCpf(dto.cpf)) {
                throw new CpfAlreadyExistsException(dto.cpf);
            }
        }

        Course course = courseRepository.findByIdOptional(dto.courseId)
                .orElseThrow(() -> new CourseNotFoundException(dto.courseId));

        student.name = dto.name;
        student.email = dto.email;
        student.cpf = dto.cpf;
        student.phone = dto.phone;
        student.course = course;

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
        long activeEnrollments = enrollmentRepository.countActiveByStudent(student.id);
        if (activeEnrollments > 0) {
            throw new StudentHasActiveEnrollmentsException(student.name, activeEnrollments);
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
