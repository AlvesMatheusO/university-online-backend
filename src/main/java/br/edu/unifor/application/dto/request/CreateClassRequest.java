package br.edu.unifor.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateClassRequest {

    @NotBlank
    public String code;

    @NotNull
    @Positive
    public Long subjectId;

    @NotNull
    @Positive
    public Long professorId;

    @NotNull
    @Positive
    public Long scheduleId;

    @NotNull
    @Positive
    public Long courseId;

    @NotNull
    @Positive
    public Integer maxCapacity;

    @NotBlank
    public String semester;

    @NotBlank
    public String status;
}

