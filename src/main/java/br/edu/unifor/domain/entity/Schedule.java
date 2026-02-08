package br.edu.unifor.domain.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Representa um horário disponível para aulas.
 * Exemplo: Segunda-feira 08:00-10:00 (MANHA)
 */

@Entity
@Table(name = "schedules")
public class Schedule extends PanacheEntity {

    /**
     * Dia da semana
     */
    @NotNull(message = "O dia da semana é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    public DayOfWeek dayOfWeek;

    /**
     * Horário de início da aula (ex: 08:00)
     */
    @NotNull(message = "O horário de início é obrigatório")
    @Column(name = "start_time", nullable = false)
    public LocalTime startTime;

    /**
     * Horário de término da aula (ex: 10:00)
     */
    @NotNull(message = "O horário de término é obrigatório")
    @Column(name = "end_time", nullable = false)
    public LocalTime endTime;

    /**
     * Período do dia (MANHA, TARDE, NOITE)
     * Calculado automaticamente baseado no startTime
     */
    @NotNull(message = "O período é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    public Period period;

    public Schedule() {
    }

    public Schedule(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.period = calculatePeriod(startTime);
    }

    /**
     * Calcula o período do dia baseado no horário de início
     */
    private Period calculatePeriod(LocalTime time) {
        int hour = time.getHour();
        if (hour < 12)
            return Period.MANHA;
        if (hour < 18)
            return Period.TARDE;
        return Period.NOITE;
    }

    /**
     * Enum para representar os períodos do dia
     */
    public enum Period {
        MANHA, // Até 11:59
        TARDE, // 12:00 - 17:59
        NOITE // 18:00+
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", period=" + period +
                '}';
    }
}