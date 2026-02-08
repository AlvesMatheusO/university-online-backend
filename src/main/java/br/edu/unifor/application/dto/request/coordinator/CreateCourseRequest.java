package br.edu.unifor.application.dto.request.coordinator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(

    @NotBlank(message = "O código do curso é obrigatório")
    String code,

    @NotBlank(message = "O nome do curso é obrigatório")
    String name,

    @NotBlank(message = "O departamento do curso é obrigatório")
    String department,

    @NotBlank(message = "A duração do curso é obrigatória")
    Integer duration,

    @NotNull(message = "O status do curso é obrigatório")
    Boolean active

) {}
