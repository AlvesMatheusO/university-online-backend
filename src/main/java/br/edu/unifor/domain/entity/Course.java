package br.edu.unifor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "courses")
public class Course extends PanacheEntity {
    
    @NotBlank(message = "O código do curso é obrigatório")
    @Column(nullable = false, unique = true, length = 20)
    public String code;  // ← Mude para public (ou crie getter/setter)

    @NotBlank(message = "O nome do curso é obrigatório")
    @Column(nullable = false, length = 100)
    public String name;

    @NotBlank(message = "O departamento do curso é obrigatório")
    @Column(nullable = false, length = 100)
    public String department;
    
    @Positive(message = "A duração deve ser positiva")  
    @Column
    public Integer duration;  

    @NotNull(message = "O status deve ter um valor booleano")
    @Column(nullable = false)
    public Boolean active;

    public Course() {}

    public Course(String code, String name, String department, Integer duration, Boolean active) {
        this.code = code;
        this.name = name;
        this.department = department;
        this.duration = duration;  
        this.active = active;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", duration=" + duration +  
                ", active=" + active +
                '}';
    }
}