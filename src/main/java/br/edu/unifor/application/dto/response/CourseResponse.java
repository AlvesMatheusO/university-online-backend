package br.edu.unifor.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Course;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Course (Curso).
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse {

    private Long id;
    private String code;
    private String name;
    private String department;
    private Integer duration;
    private Boolean active;

    public CourseResponse() {

    }

    public CourseResponse(Course course) {
        if (course != null) {
            this.id = course.id;
            this.code = course.code;
            this.name = course.name;
            this.department = course.department;
            this.duration = course.duration;
            this.active = course.active;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }       
    
    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}