package br.edu.unifor.application.service;

import java.time.DayOfWeek;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.application.dto.request.schedule.CreateScheduleRequest;
import br.edu.unifor.application.dto.request.schedule.UpdateScheduleRequest;
import br.edu.unifor.domain.entity.Schedule;
import br.edu.unifor.domain.entity.Schedule.Period;
import br.edu.unifor.domain.repository.ScheduleRepository;

@ApplicationScoped
public class ScheduleService {

    @Inject
    ScheduleRepository scheduleRepository;

    /**
     * Lista todos os horários disponíveis
     */
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.listAll();
    }

    /**
     * Busca horários de um dia específico
     */
    public List<Schedule> getSchedulesByDay(DayOfWeek dayOfWeek) {
        return scheduleRepository.findByDayOfWeek(dayOfWeek);
    }

    /**
     * Busca horários por período (MANHA, TARDE, NOITE)
     */
    public List<Schedule> getSchedulesByPeriod(Period period) {
        return scheduleRepository.findByPeriod(period);
    }

    /**
     * Busca horários de um dia e período específicos
     * Exemplo: Segunda-feira à noite
     */
    public List<Schedule> getSchedulesByDayAndPeriod(DayOfWeek dayOfWeek, Period period) {
        return scheduleRepository.findByDayAndPeriod(dayOfWeek, period);
    }

    /**
     * Busca um horário por ID
     */
    public Schedule findById(Long id) {
        return scheduleRepository.findByIdOptional(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com ID: " + id));
    }

    /**
     * Verifica se um horário existe
     */
    public boolean exists(Long id) {
        return scheduleRepository.findByIdOptional(id).isPresent();
    }

    @Transactional
    public Schedule create(CreateScheduleRequest dto) {
        Schedule schedule = new Schedule();
        schedule.dayOfWeek = dto.dayOfWeek;
        schedule.startTime = dto.startTime;
        schedule.endTime = dto.endTime;
        schedule.period = dto.period;

        scheduleRepository.persist(schedule);
        return schedule;
    }

    @Transactional
    public Schedule update(Long id, UpdateScheduleRequest dto) {
        Schedule schedule = findById(id);
        schedule.dayOfWeek = dto.dayOfWeek;
        schedule.startTime = dto.startTime;
        schedule.endTime = dto.endTime;
        schedule.period = dto.period;

        scheduleRepository.persist(schedule);
        return schedule;
    }
}