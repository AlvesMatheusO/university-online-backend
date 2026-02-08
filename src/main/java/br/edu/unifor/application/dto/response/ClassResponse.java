package br.edu.unifor.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Class;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Class (Aula/Matriz Curricular).
 * 
 * Contém informações completas da turma incluindo:
 * - Disciplina
 * - Professor
 * - Horário
 * - Cursos autorizados
 * - Quantidade de vagas e matriculados
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassResponse {

    private Long id;
    private String code;
    private SubjectResponse subject;
    private ProfessorResponse professor;
    private ScheduleResponse schedule;
    private CourseResponse course;
    private Integer maxCapacity;
    private Integer enrolledStudents;
    private Integer availableSlots;
    private String semester;
    private String status;

    public ClassResponse() {
    }

    public ClassResponse(Class classEntity) {
        if (classEntity != null) {
            this.id = classEntity.id;
            this.code = classEntity.code;
            this.semester = classEntity.semester;
            this.maxCapacity = classEntity.maxCapacity;
            this.enrolledStudents = classEntity.enrolledStudents;
            this.availableSlots = classEntity.maxCapacity - classEntity.enrolledStudents;
            this.status = classEntity.status != null ? classEntity.status.name() : null;

            if (classEntity.subject != null) {
                this.subject = new SubjectResponse(classEntity.subject);
            }

            if (classEntity.professor != null) {
                this.professor = new ProfessorResponse(classEntity.professor);
            }

            if (classEntity.schedule != null) {
                this.schedule = new ScheduleResponse(classEntity.schedule);
            }

            if (classEntity.course != null) {
                this.course = new CourseResponse(classEntity.course);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SubjectResponse getSubject() {
        return subject;
    }

    public void setSubject(SubjectResponse subject) {
        this.subject = subject;
    }

    public ProfessorResponse getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorResponse professor) {
        this.professor = professor;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleResponse schedule) {
        this.schedule = schedule;
    }

    public CourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(Integer enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}