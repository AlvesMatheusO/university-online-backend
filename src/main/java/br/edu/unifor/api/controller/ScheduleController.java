package br.edu.unifor.api.controller;

import java.time.DayOfWeek;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
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
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar horários")
    public List<Schedule> listAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Buscar horário por ID")
    public Response findById(@PathParam("id") Long id) {
        Schedule schedule = scheduleService.findById(id);
        return Response.ok(schedule).build();
    }

    @GET
    @Path("/day/{dayOfWeek}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Buscar horários por dia da semana")
    public List<Schedule> findByDay(@PathParam("dayOfWeek") DayOfWeek dayOfWeek) {
        return scheduleService.getSchedulesByDay(dayOfWeek);
    }

    @GET
    @Path("/period")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Buscar horários por período")
    public List<Schedule> findByPeriod(@QueryParam("value") Period period) {
        return scheduleService.getSchedulesByPeriod(period);
    }

    @GET
    @Path("/filter")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Buscar horários por dia e período")
    public List<Schedule> findByDayAndPeriod(
            @QueryParam("day") DayOfWeek dayOfWeek,
            @QueryParam("period") Period period) {
        return scheduleService.getSchedulesByDayAndPeriod(dayOfWeek, period);
    }

}
