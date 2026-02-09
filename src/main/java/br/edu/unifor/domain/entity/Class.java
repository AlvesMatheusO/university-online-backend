package br.edu.unifor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Entidade Class (Turma/Matriz Curricular).
 */
@Entity
@Table(name = "classes")
public class Class extends PanacheEntity {

    @NotNull(message = "O código da turma é obrigatório")
    @Column(nullable = false, unique = true, length = 30)
    public String code;

    @NotNull(message = "A disciplina é obrigatória")
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    public Subject subject;

    @NotNull(message = "O professor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    public Professor professor;

    @NotNull(message = "O horário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    public Schedule schedule;

    /**
     * Curso ao qual esta turma pertence.
     * REGRA: Alunos só podem se matricular em turmas do seu curso.
     */
    @NotNull(message = "O curso é obrigatório")
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    public Course course;

    @Positive(message = "A capacidade deve ser positiva")
    @Column(name = "max_capacity", nullable = false)
    public Integer maxCapacity;

    @Column(name = "enrolled_students", nullable = false)
    public Integer enrolledStudents = 0;

    @Column(length = 10)
    public String semester;

    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public ClassStatus status = ClassStatus.ATIVA;

    public Class() {
    }

    public Class(String code, Subject subject, Professor professor,
            Schedule schedule, Course course, Integer maxCapacity, String semester) {
        this.code = code;
        this.subject = subject;
        this.professor = professor;
        this.schedule = schedule;
        this.course = course;
        this.maxCapacity = maxCapacity;
        this.semester = semester;
        this.status = ClassStatus.ATIVA;
    }

    public boolean hasAvailableSlots() {
        return enrolledStudents < maxCapacity;
    }

    public void incrementEnrollment() {
        if (!hasAvailableSlots()) {
            throw new IllegalStateException("Turma sem vagas disponíveis");
        }
        this.enrolledStudents++;
    }

    public void decrementEnrollment() {
        if (this.enrolledStudents > 0) {
            this.enrolledStudents--;
        }
    }

    public Integer getAvailableSlots() {
        return maxCapacity - enrolledStudents;
    }

    /**
     * Verifica se um curso está autorizado para esta turma.
     */
    public boolean isAuthorizedForCourse(Long courseId) {
        return this.course.id.equals(courseId);
    }

    public enum ClassStatus {
        ATIVA,
        CANCELADA,
        CONCLUIDA,
        SUSPENSA
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", subject=" + (subject != null ? subject.code : "null") +
                ", professor=" + (professor != null ? professor.name : "null") +
                ", course=" + (course != null ? course.code : "null") +
                ", maxCapacity=" + maxCapacity +
                ", enrolledStudents=" + enrolledStudents +
                ", status=" + status +
                '}';
    }
}