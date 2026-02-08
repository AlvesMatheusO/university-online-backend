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

@Entity
@Table(name = "classes")
public class Class extends PanacheEntity {
    /**
     * Código identificador da turma.
     * Formato sugerido: CODIGO_DISCIPLINA-TURMA-SEMESTRE
     * Exemplo: MAT001-A-2024.1
     */
    @NotNull(message = "O código da turma é obrigatório")
    @Column(nullable = false, unique = true, length = 30)
    public String code;

    /**
     * Disciplina ministrada nesta turma.
     * Relacionamento Many-to-One: várias turmas podem ser da mesma disciplina.
     */
    @NotNull(message = "A disciplina é obrigatória")
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    public Subject subject;

    /**
     * Professor responsável pela turma.
     * Relacionamento Many-to-One: um professor pode ministrar várias turmas.
     * 
     * VALIDAÇÃO: Professor não pode ter 2 turmas no mesmo horário.
     */
    @NotNull(message = "O professor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    public Professor professor;

    /**
     * Horário da aula.
     * Relacionamento Many-to-One: várias turmas podem usar o mesmo horário.
     * 
     * VALIDAÇÃO: Não pode haver conflito de professor no mesmo horário.
     */
    @NotNull(message = "O horário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    public Schedule schedule;

    /**
     * Curso ao qual esta turma pertence.
     * Relacionamento Many-to-One: um curso tem várias turmas.
     */
    @NotNull(message = "O curso é obrigatório")
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    public Course course;

    /**
     * Capacidade máxima de alunos nesta turma.
     * Requisito do desafio: controlar vagas disponíveis.
     */
    @Positive(message = "A capacidade deve ser positiva")
    @Column(name = "max_capacity", nullable = false)
    public Integer maxCapacity;

    /**
     * Número atual de alunos matriculados.
     * Calculado automaticamente através das matrículas ativas.
     */
    @Column(name = "enrolled_students", nullable = false)
    public Integer enrolledStudents = 0;

    /**
     * Semestre letivo.
     * Formato: YYYY.S (ex: 2024.1, 2024.2)
     */
    @Column(length = 10)
    public String semester;

    /**
     * Status da turma.
     */
    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public ClassStatus status = ClassStatus.ATIVA;

    public Class() {
    }

    public Class(
            String code,
            Subject subject,
            Professor professor,
            Schedule schedule,
            Course course,
            Integer maxCapacity,
            String semester,
            ClassStatus status) {
        this.code = code;
        this.subject = subject;
        this.professor = professor;
        this.schedule = schedule;
        this.course = course;
        this.maxCapacity = maxCapacity;
        this.semester = semester;
        this.status = status;
    }

    /**
     * Verifica se a turma tem vagas disponíveis.
     * 
     * @return true se há vagas
     */
    public boolean hasAvailableSlots() {
        return enrolledStudents < maxCapacity;
    }

    /**
     * Incrementa o contador de alunos matriculados.
     * Chamado automaticamente ao criar matrícula.
     */
    public void incrementEnrollment() {
        if (!hasAvailableSlots()) {
            throw new IllegalStateException("Turma sem vagas disponíveis");
        }
        this.enrolledStudents++;
    }

    /**
     * Decrementa o contador de alunos matriculados.
     * Chamado automaticamente ao cancelar matrícula.
     */
    public void decrementEnrollment() {
        if (this.enrolledStudents > 0) {
            this.enrolledStudents--;
        }
    }

    /**
     * Calcula vagas disponíveis.
     */
    public Integer getAvailableSlots() {
        return maxCapacity - enrolledStudents;
    }

    /**
     * Enum para status da turma
     */
    public enum ClassStatus {
        ATIVA, // Aceita matrículas
        CANCELADA, // Turma cancelada
        CONCLUIDA, // Semestre finalizado
        SUSPENSA // Temporariamente suspensa
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", subject=" + (subject != null ? subject.code : "null") +
                ", professor=" + (professor != null ? professor.name : "null") +
                ", schedule=" + (schedule != null ? schedule.id : "null") +
                ", course=" + (course != null ? course.code : "null") +
                ", maxCapacity=" + maxCapacity +
                ", enrolledStudents=" + enrolledStudents +
                ", semester='" + semester + '\'' +
                ", status=" + status +
                '}';
    }

}
