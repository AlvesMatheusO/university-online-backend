package br.edu.unifor.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateStudentRequest {

    @NotBlank
    public String name;

    @NotBlank
    public String email;

    @NotBlank
    public String cpf;

    public String phone;

    @NotNull
    public Long courseId;
}
