package br.edu.unifor.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Professor;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Professor.
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessorResponse {

    private Long id;
    private String name;
    private String email;
    private String department;

    public ProfessorResponse() {
    }

    public ProfessorResponse(Professor professor) {
        if (professor != null) {
            this.id = professor.id;
            this.name = professor.name;
            this.email = professor.email;
            this.department = professor.department;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}