package br.edu.unifor.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Coordinator;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Coordinator (Coordenador).
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoordinatorResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private List<CourseResponse> courses;

    public CoordinatorResponse() {
    }

    public CoordinatorResponse(Coordinator coordinator) {
        if (coordinator != null) {
            this.id = coordinator.id;
            this.name = coordinator.name;
            this.email = coordinator.email;
            this.phone = coordinator.phone;
            this.department = coordinator.department;

            if (coordinator.courses != null && !coordinator.courses.isEmpty()) {
                this.courses = coordinator.courses.stream()
                        .map(CourseResponse::new)
                        .collect(Collectors.toList());
            }
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CourseResponse> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseResponse> courses) {
        this.courses = courses;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}