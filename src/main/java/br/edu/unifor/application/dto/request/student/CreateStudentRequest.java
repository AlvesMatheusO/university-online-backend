package br.edu.unifor.application.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateStudentRequest {

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
