package br.edu.unifor.application.dto.response;

/**
 * DTO resumido de estudante.
 * Usado para: COORDINATOR ver alunos de suas turmas, listagens de turmas.
 * Expõe apenas nome e matrícula (sem dados sensíveis).
 */
public class StudentSummaryResponse {
    
    private Long id;
    private String name;
    private String registration;

    // Construtores
    public StudentSummaryResponse() {}

    // Getters e Setters
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

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }
}