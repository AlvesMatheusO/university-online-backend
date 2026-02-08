package br.edu.unifor.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Subject;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Subject (Disciplina).
 * 
 * Usado para retornar informações de disciplinas ao cliente.
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectResponse {

    private Long id;
    private String code;
    private String name;
    private Integer workload;

    public SubjectResponse() {
    }

    public SubjectResponse(Subject subject) {
        if (subject != null) {
            this.id = subject.id;
            this.code = subject.code;
            this.name = subject.name;
            this.workload = subject.workload;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWorkload() {
        return workload;
    }

    public void setWorkload(Integer workload) {
        this.workload = workload;
    }
}