package br.edu.unifor.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Entidade que representa uma matrícula de aluno em uma turma.
 * 
 * Regras de negócio implementadas:
 * - Aluno deve estar ativo para se matricular
 * - Turma deve estar ativa e com vagas disponíveis
 * - Aluno não pode se matricular 2x na mesma turma
 * - Aluno não pode ter 2 turmas no mesmo horário (conflito de horário)
 */
@Entity
@Table(name = "enrollments")
public class Enrollment extends PanacheEntity {

    /**
     * Aluno matriculado.
     * Relacionamento Many-to-One: um aluno pode ter várias matrículas.
     */
    @NotNull(message = "O aluno é obrigatório")
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false) 
    public Student student; 
    /**
     * Turma na qual o aluno está matriculado.
     * Relacionamento Many-to-One: uma turma pode ter várias matrículas.
     * 
     * IMPORTANTE: Aluno se matricula em TURMA, não em disciplina diretamente.
     */
    @NotNull(message = "A turma é obrigatória")
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false) 
    public Class classEntity;  

    /**
     * Data e hora da matrícula.
     * Preenchido automaticamente ao criar.
     */
    @Column(name = "enrollment_date", nullable = false)  
    public LocalDateTime enrollmentDate;  

    /**
     * Status da matrícula.
     */
    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public EnrollmentStatus status = EnrollmentStatus.ATIVA;

    /**
     * Nota final do aluno (0 a 10).
     * Opcional: será preenchida ao final do semestre.
     */
    @DecimalMin(value = "0.0", message = "A nota mínima é 0.0")
    @DecimalMax(value = "10.0", message = "A nota máxima é 10.0")
    @Column(name = "final_grade", precision = 4, scale = 2)  
    public BigDecimal finalGrade;  

    /**
     * Frequência do aluno (0 a 100%).
     * Opcional: será calculada durante o semestre.
     */
    @DecimalMin(value = "0.0", message = "A frequência mínima é 0%")
    @DecimalMax(value = "100.0", message = "A frequência máxima é 100%")
    @Column(precision = 5, scale = 2)
    public BigDecimal attendance;  

    /**
     * Data de cancelamento (se aplicável).
     */
    @Column(name = "cancellation_date")  
    public LocalDateTime cancellationDate;  

    /**
     * Motivo do cancelamento (se aplicável).
     */
    @Column(name = "cancellation_reason", length = 500)  
    public String cancellationReason;  

    /**
     * Construtor padrão (obrigatório para JPA)
     */
    public Enrollment() {
    }

    /**
     * Construtor simplificado
     */
    public Enrollment(Student student, Class classEntity) {
        this.student = student;
        this.classEntity = classEntity;
        this.status = EnrollmentStatus.ATIVA;
    }

    /**
     * Preenche a data de matrícula automaticamente antes de persistir.
     */
    @PrePersist
    public void prePersist() {
        if (this.enrollmentDate == null) {
            this.enrollmentDate = LocalDateTime.now();
        }
    }

    /**
     * Cancela a matrícula.
     * 
     * @param reason Motivo do cancelamento
     */
    public void cancel(String reason) {
        this.status = EnrollmentStatus.CANCELADA;
        this.cancellationDate = LocalDateTime.now();
        this.cancellationReason = reason;
    }

    /**
     * Marca matrícula como concluída (aluno passou).
     * 
     * @param grade Nota final
     * @param attendance Frequência
     */
    public void complete(BigDecimal grade, BigDecimal attendance) {
        this.status = EnrollmentStatus.CONCLUIDA;
        this.finalGrade = grade;
        this.attendance = attendance;
    }

    /**
     * Verifica se a matrícula está ativa.
     */
    public boolean isActive() {
        return this.status == EnrollmentStatus.ATIVA;
    }

    /**
     * Enum para status da matrícula
     */
    public enum EnrollmentStatus {
        ATIVA,       // Matrícula ativa
        CANCELADA,   // Matrícula cancelada pelo aluno ou coordenação
        CONCLUIDA,   // Semestre concluído (aprovado ou reprovado)
        TRANCADA     // Trancamento de matrícula
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", student=" + (student != null ? student.registration : "null") +
                ", class=" + (classEntity != null ? classEntity.code : "null") +
                ", enrollmentDate=" + enrollmentDate +
                ", status=" + status +
                ", finalGrade=" + finalGrade +
                ", attendance=" + attendance +
                '}';
    }
}