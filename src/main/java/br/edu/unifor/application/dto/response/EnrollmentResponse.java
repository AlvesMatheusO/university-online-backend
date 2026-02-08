package br.edu.unifor.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Enrollment;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Enrollment (Matrícula).
 * 
 * Usado para retornar informações de matrículas ao cliente.
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentResponse {

    private Long id;
    private StudentResponse student;
    private ClassResponse classInfo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime enrollmentDate;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cancellationDate;

    private String cancellationReason;

    private BigDecimal finalGrade;
    private BigDecimal attendance;

    public EnrollmentResponse() {
    }

    public EnrollmentResponse(Enrollment enrollment) {
        if (enrollment != null) {
            this.id = enrollment.id;
            this.enrollmentDate = enrollment.enrollmentDate;
            this.status = enrollment.status != null ? enrollment.status.name() : null;
            this.cancellationDate = enrollment.cancellationDate;
            this.cancellationReason = enrollment.cancellationReason;
            this.finalGrade = enrollment.finalGrade;
            this.attendance = enrollment.attendance;

            if (enrollment.student != null) {
                this.student = new StudentResponse(enrollment.student);
            }

            if (enrollment.classEntity != null) {
                this.classInfo = new ClassResponse(enrollment.classEntity);
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

    public StudentResponse getStudent() {
        return student;
    }

    public void setStudent(StudentResponse student) {
        this.student = student;
    }

    public ClassResponse getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassResponse classInfo) {
        this.classInfo = classInfo;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public BigDecimal getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(BigDecimal finalGrade) {
        this.finalGrade = finalGrade;
    }

    public BigDecimal getAttendance() {
        return attendance;
    }

    public void setAttendance(BigDecimal attendance) {
        this.attendance = attendance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}