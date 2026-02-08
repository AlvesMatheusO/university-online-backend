package br.edu.unifor.application.dto.request.enrollment;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class CompleteEnrollmentRequest {

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    public BigDecimal grade;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    public BigDecimal attendance;
}
