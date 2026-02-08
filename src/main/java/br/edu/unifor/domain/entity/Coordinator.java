package br.edu.unifor.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Entidade que representa um coordenador acadêmico.
 * 
 * Um coordenador pode gerenciar múltiplos cursos simultaneamente.
 * Relacionamento Many-to-Many com Course através da tabela coordinator_courses.
 * 
 * Responsabilidades do Coordenador:
 * - Gerenciar matriz curricular dos cursos sob sua coordenação
 * - Criar e gerenciar turmas (classes) com professores e disciplinas
 * - Visualizar relatórios de matrículas e desempenho
 * 
 */

@Entity
@Table(name = "coordinators")
public class Coordinator extends PanacheEntity {

    /**
     * Matrícula do coordenador, deve ser única e contém 7 dígitos.
     * Formato: YYXXXXX (2 dígitos do ano + 5 dígitos aleatórios)
     * Exemplo: 2612345
     */
    @NotBlank(message = "A matrícula é obrigatória")
    @Column(unique = true, nullable = false, length = 7)
    public String registration;

    /**
     * Nome completo do coordenador
     */
    @NotBlank(message = "O nome do coordenador é obrigatório")
    @Column(nullable = false, length = 150)
    public String name;

    /**
     * Email institucional do coordenador
     */
    @Email(message = "O email deve ser válido")
    @NotBlank(message = "O email é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    public String email;

    /**
     * Telefone de contato
     */
    @Column(length = 15)
    public String phone;

    /**
     * Departamento ao qual o coordenador pertence
     */
    @Column(length = 100)
    public String department;

    /**
     * Cursos sob coordenação deste coordenador.
     * Relacionamento Many-to-Many: um coordenador pode gerenciar vários cursos
     * e um curso pode ter vários coordenadores (ex: co-coordenadores).
     */
    @ManyToMany
    @JoinTable(name = "coordinator_courses", joinColumns = @JoinColumn(name = "coordinator_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    public List<Course> courses = new ArrayList<>();

    /**
     * Indica se o coordenador está ativo no sistema.
     * Coordenadores inativos não podem gerenciar cursos.
     */
    @Column(nullable = false)
    public Boolean active = true;

    public Coordinator() {
    }

    public Coordinator(
            String registration,
            String name,
            String email,
            String phone,
            String department) {
        this.registration = registration;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    /**
     * Adiciona um curso à lista de cursos coordenados.
     * 
     * @param course Curso a ser adicionado
     */
    public void addCourse(Course course) {
        if (!this.courses.contains(course)) {
            this.courses.add(course);
        }
    }

    /**
     * Remove um curso da lista de cursos coordenados.
     * 
     * @param course Curso a ser removido
     */
    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

    /**
     * Verifica se o coordenador gerencia um curso específico.
     * 
     * @param courseId ID do curso
     * @return true se coordena o curso
     */
    public boolean coordinatesCourse(Long courseId) {
        return this.courses.stream()
                .anyMatch(course -> course.id.equals(courseId));
    }

    @Override
    public String toString() {
        return "Coordinator{" +
                "id=" + id +
                ", registration='" + registration + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", coursesCount=" + (courses != null ? courses.size() : 0) +
                ", active=" + active +
                '}';
    }

}
