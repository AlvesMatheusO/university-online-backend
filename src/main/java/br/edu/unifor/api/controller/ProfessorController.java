package br.edu.unifor.api.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.application.dto.request.professor.CreateProfessorRequest;
import br.edu.unifor.application.dto.request.professor.UpdateProfessorRequest;
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
        Professor professor = professorService.findById(id);
        return Response.ok(professor).build();
    }

    @GET
    @Path("/registration/{registration}")
    @Operation(summary = "Buscar professor por matrícula")
    public Response findByRegistration(@PathParam("registration") String registration) {
        Professor professor = professorService.findByRegistration(registration);
        return Response.ok(professor).build();
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
        Professor professor = professorService.findByEmail(email);
        return Response.ok(professor).build();
    }

    /**
     * Cria um novo professor.
     * A matrícula é gerada automaticamente pelo sistema.
     */
    @POST
    @Operation(summary = "Criar novo professor")
    @APIResponse(responseCode = "201", description = "Professor criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Email já cadastrado")
    public Response create(@Valid CreateProfessorRequest dto) {
        Professor created = professorService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    /**
     * Atualiza os dados de um professor.
     * A matrícula não pode ser alterada.
     */
    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar professor")
    @APIResponse(responseCode = "200", description = "Professor atualizado com sucesso")
    @APIResponse(responseCode = "404", description = "Professor não encontrado")
    @APIResponse(responseCode = "409", description = "Email já cadastrado")
    public Response update(
            @PathParam("id") Long id,
            @Valid UpdateProfessorRequest dto) {

        Professor updated = professorService.update(id, dto);
        return Response.ok(updated).build();
    }

    /**
     * Remove um professor do sistema.
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remover professor")
    @APIResponse(responseCode = "204", description = "Professor removido com sucesso")
    @APIResponse(responseCode = "404", description = "Professor não encontrado")
    public Response delete(@PathParam("id") Long id) {
        professorService.delete(id);
        return Response.noContent().build();
    }
}
