package br.edu.unifor.application.dto.request.professor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateProfessorRequest {

    @NotBlank
    public String name;

    @NotBlank
    @Email
    public String email;

    @NotBlank
    public String title;

    @NotBlank
    public String department;
}
