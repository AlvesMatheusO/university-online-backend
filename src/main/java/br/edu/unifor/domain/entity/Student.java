package br.edu.unifor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Entidade que representa um aluno do sistema acadêmico.
 * 
 * Um aluno está vinculado a um curso específico e possui matrícula única.
 * A matrícula é gerada automaticamente no formato YYXXXXX.
 */

@Entity
@Table(name = "students")
public class Student extends PanacheEntity {
 
     /**
     * Matrícula do aluno, deve ser única e contém 7 dígitos.
     * Formato: YYXXXXX (2 dígitos do ano + 5 dígitos aleatórios)
     * Exemplo: 2612345
     */
    @NotBlank(message = "A matrícula do aluno é obrigatória")
    @Column(nullable = false, unique = true, length = 7)
    public String registration;

    /**
     * Nome completo do aluno
     */
    @NotBlank(message = "O nome do aluno é obrigatório")
    @Column(nullable = false, length = 150)
    public String name;


    /**
     * Email institucional do aluno
     */
    @Email(message = "O email deve ser válido")
    @NotBlank(message = "O email é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    public String email;

    /**
     * CPF do aluno (apenas números)
     */
    @NotBlank(message = "O CPF do aluno é obrigatório")
    @Column(nullable = false, unique = true, length = 11)   
    public String cpf;

    /**
     * Telefone de contato
     */
     @Column(length = 15)
    public String phone;

    /**
     * Curso ao qual o aluno está matriculado.
     * Relacionamento Many-to-One: vários alunos podem estar no mesmo curso.
     */
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    public Course course;

    /**
     * Indica se o aluno está ativo no sistema.
     * Alunos inativos não podem fazer matrículas.
     */
    @Column(name = "active", nullable = false)
    public Boolean isActive = true;


    public Student() {}

    public Student(String name, String email, String cpf, String phone, Course course) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phone = phone;
        this.course = course;
        this.isActive = true;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", registration='" + registration + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", course=" + (course != null ? course.name : "null") +
                ", isActive=" + isActive +
                '}';
    }

}
