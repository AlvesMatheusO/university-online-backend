package br.edu.unifor.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateSubjectRequest {

    @NotBlank
    public String code;

    @NotBlank
    public String name;

    @NotNull
    @Positive
    public Integer workload;

    @NotNull
    @Positive
    public Integer credits;

    public String description;
}
