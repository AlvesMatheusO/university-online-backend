package br.edu.unifor.application.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Enrollment;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta simplificado para visualização de matrículas do aluno.
 *
 * Conforme requisito do PDF:
 * - Disciplina
 * - Professor
 * - Horário
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentEnrollmentResponse {

    private Long enrollmentId;

    // Disciplina
    private String subjectCode;
    private String subjectName;

    // Professor
    private String professorName;

    // Horário
    private String dayOfWeek;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private String period;

    // Turma
    private String classCode;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime enrollmentDate;

    private String status;

    public StudentEnrollmentResponse() {
    }

    public StudentEnrollmentResponse(Enrollment enrollment) {
        if (enrollment != null) {
            this.enrollmentId = enrollment.id;
            this.enrollmentDate = enrollment.enrollmentDate;
            this.status = enrollment.status != null ? enrollment.status.name() : null;

            if (enrollment.classEntity != null) {
                this.classCode = enrollment.classEntity.code;

                if (enrollment.classEntity.subject != null) {
                    this.subjectCode = enrollment.classEntity.subject.code;
                    this.subjectName = enrollment.classEntity.subject.name;
                }

                if (enrollment.classEntity.professor != null) {
                    this.professorName = enrollment.classEntity.professor.name;
                }

                if (enrollment.classEntity.schedule != null) {
                    this.dayOfWeek = enrollment.classEntity.schedule.dayOfWeek.name();
                    this.startTime = enrollment.classEntity.schedule.startTime;
                    this.endTime = enrollment.classEntity.schedule.endTime;
                    this.period = enrollment.classEntity.schedule.period.name();
                }
            }
        }
    }

    // ===== Getters & Setters =====

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
