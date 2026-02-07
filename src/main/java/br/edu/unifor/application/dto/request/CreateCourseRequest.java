package br.edu.unifor.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(

    @NotBlank(message = "O código do curso é obrigatório")
    String code,

    @NotBlank(message = "O nome do curso é obrigatório")
    String name,

    @NotBlank(message = "O departamento do curso é obrigatório")
    String department,

    @NotNull(message = "O status do curso é obrigatório")
    Boolean active

) {}
