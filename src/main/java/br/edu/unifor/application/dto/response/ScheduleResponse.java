package br.edu.unifor.application.dto.response;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.unifor.domain.entity.Schedule;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO de resposta para Schedule (Horário).
 */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponse {

    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Schedule.Period period;

    public ScheduleResponse() {
    }

    public ScheduleResponse(Schedule schedule) {
        if (schedule != null) {
            this.id = schedule.id;
            this.dayOfWeek = schedule.dayOfWeek;
            this.startTime = schedule.startTime;
            this.endTime = schedule.endTime;
            this.period = schedule.period;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
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

    public Schedule.Period getPeriod() {
        return period;
    }

    public void setPeriod(Schedule.Period period) {
        this.period = period;
    }
}