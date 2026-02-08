package br.edu.unifor.application.dto.request;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

import br.edu.unifor.domain.entity.Schedule.Period;

public class UpdateScheduleRequest {

    @NotNull
    public DayOfWeek dayOfWeek;

    @NotNull
    public LocalTime startTime;

    @NotNull
    public LocalTime endTime;

    @NotNull
    public Period period;
}
