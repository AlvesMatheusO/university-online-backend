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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.application.dto.request.coordinator.CreateCoordinatorRequest;
import br.edu.unifor.application.dto.request.coordinator.UpdateCoordinatorRequest;
import br.edu.unifor.application.service.CoordinatorService;
import br.edu.unifor.domain.entity.Coordinator;

/**
 * REST Controller para gerenciar coordenadores.
 * 
 * Controle de acesso:
 * - STUDENT: Visualização de informações públicas (nome, cursos coordenados)
 * - COORDINATOR: Visualização completa de outros coordenadores
 * - ADMIN: Acesso total (CRUD completo)
 * 
 * Endpoints disponíveis:
 * - GET /coordinators - Lista todos (público - apenas dados resumidos)
 * - GET /coordinators/active - Lista ativos (público)
 * - GET /coordinators/{id} - Busca por ID (autenticado)
 * - GET /coordinators/registration/{registration} - Busca por matrícula
 * (autenticado)
 * - GET /coordinators/email/{email} - Busca por email (admin)
 * - GET /coordinators/course/{courseId} - Lista coordenadores de um curso
 * (público)
 * - POST /coordinators - Cria coordenador (admin)
 * - POST /coordinators/{id}/courses/{courseId} - Adiciona curso (admin)
 * - PUT /coordinators/{id} - Atualiza coordenador (admin)
 * - DELETE /coordinators/{id}/courses/{courseId} - Remove curso (admin)
 * - PATCH /coordinators/{id}/inactivate - Inativa (admin)
 * - PATCH /coordinators/{id}/activate - Reativa (admin)
 * - DELETE /coordinators/{id} - Remove (admin)
 */

@Path("/coordinators")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Coordenadores", description = "Gerenciamento de coordenadores")
public class CoordinatorController {

    @Inject
    CoordinatorService coordinatorService;

    // ========== ENDPOINTS PARA ADMIN ==========

    @DELETE
    @Path("/{id}/courses/{courseId}")
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Remover curso da coordenação", description = "Remove vínculo entre coordenador e curso. Não permite se for o último coordenador ativo do curso.")
    @APIResponse(responseCode = "200", description = "Curso removido")
    @APIResponse(responseCode = "400", description = "Não pode remover (último coordenador)")
    public Response removeCourse(
            @PathParam("id") Long coordinatorId,
            @PathParam("courseId") Long courseId) {
        try {
            Coordinator coordinator = coordinatorService.removeCourse(coordinatorId, courseId);
            return Response.ok(coordinator).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Atualizar coordenador")
    public Response update(
            @PathParam("id") Long id,
            @Valid UpdateCoordinatorRequest dto) {

        Coordinator updated = coordinatorService.update(id, dto);
        return Response.ok(updated).build();
    }

    @PATCH
    @Path("/{id}/inactivate")
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Inativar coordenador")
    public Response inactivate(@PathParam("id") Long id) {
        try {
            coordinatorService.inactivate(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/activate")
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Reativar coordenador")
    public Response activate(@PathParam("id") Long id) {
        try {
            coordinatorService.activate(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Remover coordenador", description = "Remove permanentemente. Não permite se tiver cursos vinculados.")
    @APIResponse(responseCode = "204", description = "Coordenador removido")
    @APIResponse(responseCode = "409", description = "Tem cursos vinculados")
    public Response delete(@PathParam("id") Long id) {
        try {
            coordinatorService.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Lista todos os coordenadores")
    public List<Coordinator> getAllCoordinators() {
        return coordinatorService.getAllCoordinators();
    }

    @GET
    @Path("/active")
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Listar coordenadores ativos")
    public List<Coordinator> listActive() {
        return coordinatorService.getActiveCoordinators();
    }

    @POST
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Criar novo coordenador")
    @APIResponse(responseCode = "201", description = "Coordenador criado")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Email já cadastrado")
    public Response create(@Valid CreateCoordinatorRequest dto) {
        Coordinator created = coordinatorService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Path("/{id}/courses/{courseId}")
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Adicionar curso à coordenação", description = "Vincula um curso a um coordenador (Many-to-Many)")
    @APIResponse(responseCode = "200", description = "Curso adicionado")
    @APIResponse(responseCode = "404", description = "Coordenador ou curso não encontrado")
    public Response addCourse(
            @Parameter(description = "ID do coordenador") @PathParam("id") Long coordinatorId,
            @Parameter(description = "ID do curso") @PathParam("courseId") Long courseId) {
        try {
            Coordinator coordinator = coordinatorService.addCourse(coordinatorId, courseId);
            return Response.ok(coordinator).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // ========== ENDPOINTS COORDENAÇÃO ==========

    @GET
    @Path("/{id}")
    @RolesAllowed({ "ADMIN", "COORDINATOR" })
    @Operation(summary = "Buscar coordenador por ID")
    public Response findById(@PathParam("id") Long id) {
        Coordinator coordinator = coordinatorService.findById(id);
        return Response.ok(coordinator).build();
    }

    @GET
    @Path("/registration/{registration}")
    @RolesAllowed({ "ADMIN", "COORDINATOR" })
    @Operation(summary = "Buscar coordenador por matrícula")
    public Response findByRegistration(@PathParam("registration") String registration) {
        Coordinator coordinator = coordinatorService.findByRegistration(registration);
        return Response.ok(coordinator).build();
    }

    // ========== ENDPOINTS LIVRES QUANDO AUTENTICADO ==========

    @GET
    @Path("/email/{email}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Buscar coordenador por email")
    public Response findByEmail(@PathParam("email") String email) {
        Coordinator coordinator = coordinatorService.findByEmail(email);
        return Response.ok(coordinator).build();
    }

    @GET
    @Path("/course/{courseId}")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar coordenadores de um curso por ID")
    public List<Coordinator> findByCourse(@PathParam("courseId") Long courseId) {
        return coordinatorService.findByCourse(courseId);
    }

    @GET
    @Path("/course/{courseId}/active")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Listar coordenadores ativos de um curso")
    public List<Coordinator> findActiveByCourse(@PathParam("courseId") Long courseId) {
        return coordinatorService.findActiveByCourse(courseId);
    }

    @GET
    @Path("/{id}/courses/count")
    @RolesAllowed({ "ADMIN", "COORDINATOR", "STUDENT" })
    @Operation(summary = "Contar cursos de um coordenador")
    public Response countCourses(@PathParam("id") Long id) {
        long count = coordinatorService.countCourses(id);
        return Response.ok("{\"count\": " + count + "}").build();
    }

    private String createErrorResponse(String message) {
        return String.format("{\"error\": \"%s\"}", message);
    }

}
