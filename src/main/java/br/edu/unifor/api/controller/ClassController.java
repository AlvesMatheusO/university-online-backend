package br.edu.unifor.api.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.application.service.ClassService;
import br.edu.unifor.domain.entity.Class;

/**
 * REST Controller para gerenciar turmas/aulas.
 */
@Path("/classes")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Classes", description = "Gerenciamento de turmas/aulas")
public class ClassController {

    @Inject
    ClassService classService;

    @GET
    @Operation(summary = "Listar todas as turmas")
    public List<Class> listAll() {
        return classService.getAllClasses();
    }

    @GET
    @Path("/active")
    @Operation(summary = "Listar turmas ativas")
    public List<Class> listActive() {
        return classService.getActiveClasses();
    }

    @GET
    @Path("/available")
    @Operation(summary = "Listar turmas com vagas disponíveis")
    public List<Class> listAvailable() {
        return classService.findClassesWithAvailableSlots();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar turma por ID")
    public Response findById(@PathParam("id") Long id) {
        try {
            Class classEntity = classService.findById(id);
            return Response.ok(classEntity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/code/{code}")
    @Operation(summary = "Buscar turma por código")
    public Response findByCode(@PathParam("code") String code) {
        try {
            Class classEntity = classService.findByCode(code);
            return Response.ok(classEntity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/subject/{subjectId}")
    @Operation(summary = "Listar turmas por disciplina")
    public List<Class> findBySubject(@PathParam("subjectId") Long subjectId) {
        return classService.findBySubject(subjectId);
    }

    @GET
    @Path("/professor/{professorId}")
    @Operation(summary = "Listar turmas por professor")
    public List<Class> findByProfessor(@PathParam("professorId") Long professorId) {
        return classService.findByProfessor(professorId);
    }

    @GET
    @Path("/course/{courseId}")
    @Operation(summary = "Listar turmas por curso")
    public List<Class> findByCourse(@PathParam("courseId") Long courseId) {
        return classService.findByCourse(courseId);
    }

    @GET
    @Path("/course/{courseId}/active")
    @Operation(summary = "Listar turmas ativas de um curso")
    public List<Class> findActiveByCourse(@PathParam("courseId") Long courseId) {
        return classService.findActiveByCourse(courseId);
    }

    @GET
    @Path("/course/{courseId}/available")
    @Operation(summary = "Listar turmas de um curso com vagas")
    public List<Class> findByCourseWithSlots(@PathParam("courseId") Long courseId) {
        return classService.findClassesByCourseWithSlots(courseId);
    }

    @GET
    @Path("/semester")
    @Operation(summary = "Listar turmas por semestre")
    public List<Class> findBySemester(
            @Parameter(description = "Semestre (ex: 2024.1)") 
            @QueryParam("value") String semester) {
        return classService.findBySemester(semester);
    }

    @POST
    @Operation(summary = "Criar nova turma")
    @APIResponse(responseCode = "201", description = "Turma criada")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Conflito de horário do professor")
    public Response create(Class classEntity) {
        try {
            Class created = classService.createClass(classEntity);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar turma")
    public Response update(@PathParam("id") Long id, Class classEntity) {
        try {
            Class updated = classService.update(id, classEntity);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/cancel")
    @Operation(summary = "Cancelar turma")
    public Response cancel(@PathParam("id") Long id) {
        try {
            classService.cancel(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/complete")
    @Operation(summary = "Marcar turma como concluída")
    public Response complete(@PathParam("id") Long id) {
        try {
            classService.complete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remover turma")
    public Response delete(@PathParam("id") Long id) {
        try {
            classService.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    private String createErrorResponse(String message) {
        return String.format("{\"error\": \"%s\"}", message.replace("\"", "\\\""));
    }
}