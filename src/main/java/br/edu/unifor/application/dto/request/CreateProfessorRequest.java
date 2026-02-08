package br.edu.unifor.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateProfessorRequest {

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
