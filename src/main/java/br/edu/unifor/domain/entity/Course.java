package br.edu.unifor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Entidade que representa Cursos.
 * 
 * Exemplos: Ciência da Computação, Engenharia de Software, Sistemas de Informação.
 */

@Entity
@Table(name = "courses")
public class Course extends PanacheEntity{
    
    @NotBlank(message = "O código do curso é obrigatório")
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @NotBlank(message = "O nome do curso é obrigatório")
    @Column(nullable = false, length = 100)
    public String name;

    @NotBlank(message = "O departamento do curso é obrigatório")
    @Column(nullable = false, length = 100)
    public String department;

    @NotNull(message = "o status deve ter um valor booleano")
    @Column(nullable = false)
    public Boolean active;

    public Course() {}

    public Course(
        String code, 
        String name, 
        String department, 
        Boolean active) {
        this.code = code;
        this.name = name;
        this.department = department;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", active=" + active +
                '}';
    }

}
