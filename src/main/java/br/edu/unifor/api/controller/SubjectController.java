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
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.domain.entity.Subject;
import br.edu.unifor.domain.repository.SubjectRepository;

@Path("/subjects")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Subjects", description = "Gerenciamento de disciplinas")
public class SubjectController {
    @Inject
    SubjectRepository subjectRepository;

    @GET
    @Operation(summary = "Listar disciplinas")
    @APIResponse(
        responseCode = "200", 
        description = "Lista de disciplinas", 
        content = @Content(schema = @Schema(implementation = Subject.class)))
    public List<Subject> listSubjects() {
        return subjectRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar disciplina por ID")
    @APIResponse(
        responseCode = "200", 
        description = "Disciplina encontrada", 
        content = @Content(schema = @Schema(implementation = Subject.class)))
    @APIResponse(
        responseCode = "404", 
        description = "Disciplina não encontrada")
    public Response findById(@PathParam("id") Long id) {
        return subjectRepository.findByIdOptional(id)
                .map(subject -> Response.ok(subject).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/code/{code}")
    @Operation(summary = "Buscar disciplina por código")
    @APIResponse(
        responseCode = "200", 
        description = "Disciplina encontrada", 
        content = @Content(schema = @Schema(implementation = Subject.class)))
    @APIResponse(
        responseCode = "404", 
        description = "Disciplina não encontrada")
    public Response findByCode(
            @Parameter(description = "Código da disciplina (ex: MAT001)", required = true) @PathParam("code") String code) {

        return subjectRepository.findByCode(code)
                .map(subject -> Response.ok(subject).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Busca disciplinas por número de créditos.
     * 
     * @param credits Número de créditos
     * @return Lista de disciplinas com o número de créditos especificado
     */

    @GET
    @Path("/credits")
    @Operation(summary = "Buscar disciplinas por créditos", description = "Filtra disciplinas pelo número de créditos")
    public List<Subject> findByCredits(
            @Parameter(description = "Número de créditos", required = true) @QueryParam("value") Integer credits) {

        return subjectRepository.list("credits", credits);
    }

}
