package br.edu.unifor.api.controller;

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

import br.edu.unifor.application.service.ProfessorService;
import br.edu.unifor.domain.entity.Professor;

@Path("/professors")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Professors", description = "Gerenciamento de professores")
public class ProfessorController {

    @Inject
    ProfessorService professorService;

    /**
     * Lista todos os professores cadastrados.
     * 
     * @return Lista de professores
     */
    @GET
    @Operation(summary = "Listar professores")
    public List<Professor> listAll() {
        return professorService.getAllProfessors();
    }

     @GET
    @Path("/{id}")
    @Operation(summary = "Buscar professor por ID")
    public Response findById(@PathParam("id") Long id) {
        try {
            Professor professor = professorService.findById(id);
            return Response.ok(professor).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/registration/{registration}")
    @Operation(summary = "Buscar professor por matrícula")
    public Response findByRegistration(@PathParam("registration") String registration) {
        try {
            Professor professor = professorService.findByRegistration(registration);
            return Response.ok(professor).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/department")
    @Operation(summary = "Buscar professores por departamento")
    public List<Professor> findByDepartment(@QueryParam("name") String department) {
        return professorService.findByDepartment(department);
    }

     @GET
    @Path("/email/{email}")
    @Operation(summary = "Buscar professor por email")
    public Response findByEmail(@PathParam("email") String email) {
        try {
            Professor professor = professorService.findByEmail(email);
            return Response.ok(professor).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
