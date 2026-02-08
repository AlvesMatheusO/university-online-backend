package br.edu.unifor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "professors")
public class Professor extends PanacheEntity {

    /**
     * Matricula do professor, deve ser única e contem 7 digitos,
     * os 2 primeiros são o ano de cadastro e os 5 últimos são um número aleatório.
     */
    @NotBlank(message = "A matrícula é obrigatória")
    @Pattern(regexp = "\\d{7}", message = "A matrícula deve ter exatamente 7 dígitos")
    @Column(unique = true, nullable = false, length = 20)
    public String registration;

    @NotBlank(message = "O nome do professor é obrigatório")
    @Column(nullable = false, length = 150)
    public String name;

    @Email(message = "O email do professor deve ser válido")
    @NotBlank(message = "O email do professor é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    public String email;

    @NotBlank(message = "A titulação é obrigatória")
    @Column(nullable = false, length = 50)
    public String title;

    @Column(length = 100)
    public String department;

    public Professor() {
    }

    public Professor(
            String name,
            String email,
            String title,
            String department) {
        this.name = name;
        this.email = email;
        this.title = title;
        this.department = department;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", registration='" + registration + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

}
