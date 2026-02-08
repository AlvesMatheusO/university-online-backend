package br.edu.unifor.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Student;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Student (Aluno).
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse {

    private Long id;
    private String registration;
    private String name;
    private String email;
    private Boolean active;
    private CourseResponse course;

    public StudentResponse() {
    }

    public StudentResponse(Student student) {
        if (student != null) {
            this.id = student.id;
            this.registration = student.registration;
            this.name = student.name;
            this.email = student.email;
            this.active = student.isActive;

            if (student.course != null) {
                this.course = new CourseResponse(student.course);
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

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
    }
}