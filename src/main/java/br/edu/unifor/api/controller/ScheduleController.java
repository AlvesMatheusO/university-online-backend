package br.edu.unifor.api.controller;

import java.time.DayOfWeek;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.application.service.ScheduleService;
import br.edu.unifor.domain.entity.Schedule;
import br.edu.unifor.domain.entity.Schedule.Period;

@Path("/schedules")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Schedules", description = "Gerenciamento de horários")
public class ScheduleController {
    
    @Inject
    ScheduleService scheduleService;

    @GET
    @Operation(summary = "Listar horários")
    public List<Schedule> listAllSchedules() {
        return scheduleService.getAllSchedules();
    }

     @GET
    @Path("/{id}")
    @Operation(summary = "Buscar horário por ID")
    public Response findById(@PathParam("id") Long id) {
        try {
            Schedule schedule = scheduleService.findById(id);
            return Response.ok(schedule).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

     @GET
    @Path("/day/{dayOfWeek}")
    @Operation(summary = "Buscar horários por dia da semana")
    public Response findByDay(@PathParam("dayOfWeek") String dayOfWeek) {
        try {
            DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
            List<Schedule> schedules = scheduleService.getSchedulesByDay(day);
            return Response.ok(schedules).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dia inválido. Use: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY")
                    .build();
        }
    }

    @GET
    @Path("/period")
    @Operation(summary = "Buscar horários por período")
    public Response findByPeriod(@QueryParam("value") String period) {
        try {
            Period periodEnum = Period.valueOf(period.toUpperCase());
            List<Schedule> schedules = scheduleService.getSchedulesByPeriod(periodEnum);
            return Response.ok(schedules).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Período inválido. Use: MANHA, TARDE, NOITE")
                    .build();
        }
    }

     @GET
    @Path("/filter")
    @Operation(summary = "Buscar horários por dia e período")
    public Response findByDayAndPeriod(
            @QueryParam("day") String dayOfWeek,
            @QueryParam("period") String period) {
        try {
            DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
            Period periodEnum = Period.valueOf(period.toUpperCase());
            List<Schedule> schedules = scheduleService.getSchedulesByDayAndPeriod(day, periodEnum);
            return Response.ok(schedules).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parâmetros inválidos")
                    .build();
        }
    }

}
