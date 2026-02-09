package br.edu.unifor.api.controller;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
 * REST Controller para gerenciar turmas/aulas (Matriz Curricular).
 */
@Path("/classes")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Classes", description = "Gerenciamento de turmas/aulas")
public class ClassController {

    @Inject
    ClassService classService;

    // ========== ENDPOINTS PÚBLICOS (ou autenticados sem restrição) ==========

    @GET
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Listar todas as turmas")
    @APIResponse(responseCode = "200", description = "Lista de turmas")
    public List<Class> listAll() {
        return classService.getAllClasses();
    }

    @GET
    @Path("/active")
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Listar turmas ativas")
    public List<Class> listActive() {
        return classService.getActiveClasses();
    }

    @GET
    @Path("/available")
    @RolesAllowed({"STUDENT"})
    @Operation(summary = "Listar turmas com vagas disponíveis (apenas para alunos)")
    public List<Class> listAvailable() {
        return classService.findClassesWithAvailableSlots();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Buscar turma por ID")
    @APIResponse(responseCode = "200", description = "Turma encontrada")
    @APIResponse(responseCode = "404", description = "Turma não encontrada")
    public Response findById(@PathParam("id") Long id) {
        Class classEntity = classService.findById(id);
        return Response.ok(classEntity).build();
    }

    @GET
    @Path("/code/{code}")
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Buscar turma por código")
    public Response findByCode(@PathParam("code") String code) {
        Class classEntity = classService.findByCode(code);
        return Response.ok(classEntity).build();
    }

    @GET
    @Path("/subject/{subjectId}")
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Listar turmas por disciplina")
    public List<Class> findBySubject(@PathParam("subjectId") Long subjectId) {
        return classService.findBySubject(subjectId);
    }

    @GET
    @Path("/professor/{professorId}")
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    @Operation(summary = "Listar turmas por professor")
    public List<Class> findByProfessor(@PathParam("professorId") Long professorId) {
        return classService.findByProfessor(professorId);
    }

    @GET
    @Path("/course/{courseId}")
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Listar turmas por curso")
    public List<Class> findByCourse(@PathParam("courseId") Long courseId) {
        return classService.findByCourse(courseId);
    }

    @GET
    @Path("/course/{courseId}/active")
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Listar turmas ativas de um curso")
    public List<Class> findActiveByCourse(@PathParam("courseId") Long courseId) {
        return classService.findActiveByCourse(courseId);
    }

    @GET
    @Path("/course/{courseId}/available")
    @RolesAllowed({"STUDENT"})
    @Operation(summary = "Listar turmas de um curso com vagas")
    public List<Class> findByCourseWithSlots(@PathParam("courseId") Long courseId) {
        return classService.findClassesByCourseWithSlots(courseId);
    }

    @GET
    @Path("/semester")
    @RolesAllowed({"ADMIN", "COORDINATOR", "STUDENT"})
    @Operation(summary = "Listar turmas por semestre")
    public List<Class> findBySemester(
            @Parameter(description = "Semestre (ex: 2024.1)") 
            @QueryParam("value") String semester) {
        return classService.findBySemester(semester);
    }

    // ========== ENDPOINTS APENAS COORDINATOR/ADMIN ==========

    @POST
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    @Operation(summary = "Criar nova turma")
    @APIResponse(responseCode = "201", description = "Turma criada")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Conflito de horário do professor")
    public Response create(@Valid Class classEntity) {
        Class created = classService.createClass(classEntity);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    @Operation(summary = "Atualizar turma")
    @APIResponse(responseCode = "200", description = "Turma atualizada")
    @APIResponse(responseCode = "404", description = "Turma não encontrada")
    @APIResponse(responseCode = "409", description = "Conflito de horário")
    public Response update(@PathParam("id") Long id, @Valid Class classEntity) {
        Class updated = classService.update(id, classEntity);
        return Response.ok(updated).build();
    }

    @PATCH
    @Path("/{id}/cancel")
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    @Operation(summary = "Cancelar turma")
    @APIResponse(responseCode = "204", description = "Turma cancelada")
    @APIResponse(responseCode = "404", description = "Turma não encontrada")
    public Response cancel(@PathParam("id") Long id) {
        classService.cancel(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/complete")
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    @Operation(summary = "Marcar turma como concluída")
    @APIResponse(responseCode = "204", description = "Turma concluída")
    @APIResponse(responseCode = "404", description = "Turma não encontrada")
    public Response complete(@PathParam("id") Long id) {
        classService.complete(id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    @Operation(
        summary = "Remover turma",
        description = "Remove permanentemente uma turma. Não é permitido se houver matrículas ativas."
    )
    @APIResponse(responseCode = "204", description = "Turma removida")
    @APIResponse(responseCode = "404", description = "Turma não encontrada")
    @APIResponse(responseCode = "409", description = "Turma possui matrículas ativas")
    public Response delete(@PathParam("id") Long id) {
        classService.delete(id);
        return Response.noContent().build();
    }
}