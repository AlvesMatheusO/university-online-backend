package br.edu.unifor.api.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
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
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.application.dto.request.subject.CreateSubjectRequest;
import br.edu.unifor.application.dto.request.subject.UpdateSubjectRequest;
import br.edu.unifor.application.service.SubjectService;
import br.edu.unifor.domain.entity.Subject;

/**
 * REST Controller para gerenciar disciplinas.
 * 
 */
@Path("/subjects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Subjects", description = "Gerenciamento de disciplinas")
public class SubjectController {

    @Inject
    SubjectService subjectService;

    @GET
    @Operation(summary = "Listar todas as disciplinas")
    @APIResponse(responseCode = "200", description = "Lista de disciplinas")
    public List<Subject> listAll() {
        return subjectService.getAllSubjects();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar disciplina por ID")
    @APIResponse(responseCode = "200", description = "Disciplina encontrada")
    @APIResponse(responseCode = "404", description = "Disciplina não encontrada")
    public Response findById(@PathParam("id") Long id) {
        Subject subject = subjectService.findById(id);
        return Response.ok(subject).build();
    }

    @GET
    @Path("/code/{code}")
    @Operation(summary = "Buscar disciplina por código")
    @APIResponse(responseCode = "200", description = "Disciplina encontrada")
    @APIResponse(responseCode = "404", description = "Disciplina não encontrada")
    public Response findByCode(
            @Parameter(description = "Código da disciplina", example = "MAT001") @PathParam("code") String code) {
        Subject subject = subjectService.findByCode(code);
        return Response.ok(subject).build();
    }

    @GET
    @Path("/credits")
    @Operation(summary = "Buscar disciplinas por créditos")
    @APIResponse(responseCode = "200", description = "Lista de disciplinas")
    public List<Subject> findByCredits(
            @Parameter(description = "Número de créditos") @QueryParam("value") Integer credits) {
        return subjectService.findByCredits(credits);
    }

    @POST
    @Operation(summary = "Criar nova disciplina")
    @APIResponse(responseCode = "201", description = "Disciplina criada")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Código já existe")
    public Response create(@Valid CreateSubjectRequest dto) {
        Subject created = subjectService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar disciplina")
    @APIResponse(responseCode = "200", description = "Disciplina atualizada")
    @APIResponse(responseCode = "404", description = "Disciplina não encontrada")
    @APIResponse(responseCode = "409", description = "Código já existe")
    public Response update(
            @PathParam("id") Long id,
            @Valid UpdateSubjectRequest dto) {
        Subject updated = subjectService.update(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remover disciplina", description = "Remove permanentemente uma disciplina. Não é permitido se a disciplina tiver turmas ativas.")
    @APIResponse(responseCode = "204", description = "Disciplina removida")
    @APIResponse(responseCode = "404", description = "Disciplina não encontrada")
    @APIResponse(responseCode = "409", description = "Disciplina possui turmas ativas")
    public Response delete(@PathParam("id") Long id) {
        subjectService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Contar total de disciplinas")
    @APIResponse(responseCode = "200", description = "Número de disciplinas")
    public Response count() {
        long count = subjectService.count();
        return Response.ok("{\"count\": " + count + "}").build();
    }
}