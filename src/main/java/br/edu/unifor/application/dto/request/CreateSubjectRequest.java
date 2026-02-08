package br.edu.unifor.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateSubjectRequest {

    @NotBlank
    public String code;

    @NotBlank
    public String name;

    @NotNull
    @Positive(message = "A carga horária deve ser maior que zero")
    public Integer workload;

    @NotNull
    @Positive(message = "Os créditos devem ser maiores que zero")
    public Integer credits;

    public String description;
}
