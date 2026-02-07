package br.edu.unifor.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entidade que representa uma Disciplina do curso.
 * 
 * Exemplos: Cálculo I, Programação Orientada a Objetos, Banco de Dados.
 */

@Entity
@Table(name = "subjects")

public class Subject extends PanacheEntity {

    /**
     * Código da disciplina (ex: MAT001, ENG101)
     */
    @NotBlank(message = "O código da disciplina é obrigatório")
    @Column(nullable = false, unique = true, length = 10)
    public String code;

    /**
     * Nome da disciplina (ex: Cálculo I, Programação Orientada a Objetos)
     */
    @NotBlank(message = "O nome da disciplina é obrigatório")
    @Column(nullable = false, length = 100)
    public String name;

    /**
     * Carga horária da disciplina em horas
     */
    @Column(nullable = false)
    public Integer workload;

    /**
     * Número de creditos da disciplina
     */
    @NotBlank(message = "O número de créditos da disciplina é obrigatório")
    @Column(nullable = false)
    public Integer credits;

    /**
     * Descrição da disciplina (Não obrigatória)
     */
    @Column(length = 255)
    public String description;

    public Subject() {
    }

    public Subject(
            String code,
            String name,
            Integer workload,
            Integer credits,
            String description
        ) {
        this.code = code;
        this.name = name;
        this.workload = workload;
        this.credits = credits;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", workload=" + workload +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                '}';
    }

}
