package br.edu.unifor.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateEnrollmentRequest {

    @NotNull
    @Positive
    public Long studentId;

    @NotNull
    @Positive
    public Long classId;
}
