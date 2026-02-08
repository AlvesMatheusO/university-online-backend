package br.edu.unifor.application.dto.request.coordinator;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateCoordinatorRequest {

    @NotBlank
    public String name;

    @NotBlank
    @Email
    public String email;

    public String phone;

    @NotBlank
    public String department;
}
