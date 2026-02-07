package br.edu.unifor.domain.repository;

import java.time.DayOfWeek;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.domain.entity.Schedule;
import br.edu.unifor.domain.entity.Schedule.Period;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ScheduleRepository implements PanacheRepository<Schedule> {

    /**
     * Busca horários por dia da semana
     */
    public List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek) {
        return list("dayOfWeek", dayOfWeek);
    }

    /**
     * Busca horários por período (MANHA, TARDE, NOITE)
     * Requisito do desafio: filtrar por período
     */
    public List<Schedule> findByPeriod(Period period) {
        return list("period", period);
    }

    /**
     * Busca horários de um dia específico em um período específico
     */
    public List<Schedule> findByDayAndPeriod(DayOfWeek dayOfWeek, Period period) {
        return list("dayOfWeek = ?1 and period = ?2", dayOfWeek, period);
    }
}