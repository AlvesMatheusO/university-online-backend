package br.edu.unifor.api.controller;

import java.math.BigDecimal;
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

import br.edu.unifor.application.dto.request.enrollment.CompleteEnrollmentRequest;
import br.edu.unifor.application.dto.request.enrollment.CreateEnrollmentRequest;
import br.edu.unifor.application.service.EnrollmentService;
import br.edu.unifor.domain.entity.Enrollment;
import br.edu.unifor.domain.entity.Enrollment.EnrollmentStatus;

/**
 * REST Controller para gerenciar matrículas.
 * 
 * Endpoints disponíveis:
 * - GET /enrollments - Lista todas
 * - GET /enrollments/active - Lista ativas
 * - GET /enrollments/{id} - Busca por ID
 * - GET /enrollments/student/{studentId} - Matrículas de um aluno
 * - GET /enrollments/class/{classId} - Matrículas de uma turma
 * - GET /enrollments/course/{courseId} - Matrículas de um curso
 * - POST /enrollments - Cria matrícula
 * - PATCH /enrollments/{id}/cancel - Cancela matrícula
 * - PATCH /enrollments/{id}/complete - Marca como concluída
 * - PUT /enrollments/{id}/grade - Atualiza nota e frequência
 * - DELETE /enrollments/{id} - Remove matrícula
 */
@Path("/enrollments")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Enrollments", description = "Gerenciamento de matrículas")
public class EnrollmentController {

    @Inject
    EnrollmentService enrollmentService;

    // ========== ENDPOINTS ADMIN ==========

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        enrollmentService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Listar todas as matrículas")
    public List<Enrollment> listAll() {
        return enrollmentService.getAllEnrollments();
    }

    @GET
    @Path("/active")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Listar matrículas ativas")
    public List<Enrollment> listActive() {
        return enrollmentService.getActiveEnrollments();
    }

    @POST
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Criar nova matrícula")
    @APIResponse(responseCode = "201", description = "Matrícula criada")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Conflito (turma cheia, duplicada ou horário)")
    public Response create(@Valid CreateEnrollmentRequest dto) {
        Enrollment created = enrollmentService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PATCH
    @Path("/{id}/cancel")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Cancelar matrícula", description = "Cancela uma matrícula ativa. Decrementa contador da turma.")
    @APIResponse(responseCode = "204", description = "Matrícula cancelada")
    public Response cancel(
            @PathParam("id") Long id,
            @Parameter(description = "Motivo do cancelamento") @QueryParam("reason") String reason) {
        enrollmentService.cancel(id, reason);
        return Response.noContent().build();
    }

    // ========== ENDPOINTS CADASTRADO ==========

    @GET
    @Path("/{id}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Buscar matrícula por ID")
    public Response findById(@PathParam("id") Long id) {
        Enrollment enrollment = enrollmentService.findById(id);
        return Response.ok(enrollment).build();

    }

    @GET
    @Path("/student/{studentId}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar matrículas de um aluno")
    public List<Enrollment> findByStudent(@PathParam("studentId") Long studentId) {
        return enrollmentService.findByStudent(studentId);
    }

    @GET
    @Path("/student/{studentId}/active")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar matrículas ativas de um aluno")
    public List<Enrollment> findActiveByStudent(@PathParam("studentId") Long studentId) {
        return enrollmentService.findActiveByStudent(studentId);
    }

    @GET
    @Path("/class/{classId}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar matrículas de uma turma")
    public List<Enrollment> findByClass(@PathParam("classId") Long classId) {
        return enrollmentService.findByClass(classId);
    }

    @GET
    @Path("/class/{classId}/active")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar matrículas ativas de uma turma")
    public List<Enrollment> findActiveByClass(
            @PathParam("classId") Long classId) {
        return enrollmentService.findActiveByClass(classId);
    }

    @GET
    @Path("/course/{courseId}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar matrículas de um curso")
    public List<Enrollment> findByCourse(@PathParam("courseId") Long courseId) {
        return enrollmentService.findByCourse(courseId);
    }

    @GET
    @Path("/status")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar matrículas por status")
    public List<Enrollment> findByStatus(
            @Parameter(description = "Status (ATIVA, CANCELADA, CONCLUIDA, TRANCADA)") @QueryParam("value") String status) {
        EnrollmentStatus enrollmentStatus = EnrollmentStatus.valueOf(status.toUpperCase());
        return enrollmentService.findByStatus(enrollmentStatus);
    }

    @PATCH
    @Path("/{id}/complete")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    public Response complete(
            @PathParam("id") Long id,
            @Valid CompleteEnrollmentRequest dto) {
        enrollmentService.complete(id, dto.grade, dto.attendance);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/grade")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    public Enrollment updateGrade(
            @PathParam("id") Long id,
            @QueryParam("grade") BigDecimal grade,
            @QueryParam("attendance") BigDecimal attendance) {
        return enrollmentService.updateGradeAndAttendance(id, grade, attendance);
    }

    @GET
    @Path("/student/{studentId}/count")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Contar matrículas ativas de um aluno")
    public Response countByStudent(@PathParam("studentId") Long studentId) {
        long count = enrollmentService.countActiveByStudent(studentId);
        return Response.ok("{\"count\": " + count + "}").build();
    }

    @GET
    @Path("/class/{classId}/count")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Contar matrículas ativas de uma turma")
    public Response countByClass(@PathParam("classId") Long classId) {
        long count = enrollmentService.countActiveByClass(classId);
        return Response.ok("{\"count\": " + count + "}").build();
    }

    @GET
    @Path("/course/{courseId}/count")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Contar total de matrículas de um curso")
    public Response countByCourse(@PathParam("courseId") Long courseId) {
        long count = enrollmentService.countByCourse(courseId);
        return Response.ok("{\"count\": " + count + "}").build();
    }
}