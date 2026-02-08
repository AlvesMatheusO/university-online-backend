package br.edu.unifor.api.controller;

import java.util.List;

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
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.application.dto.request.CreateStudentRequest;
import br.edu.unifor.application.dto.request.UpdateStudentRequest;
import br.edu.unifor.application.service.StudentService;
import br.edu.unifor.domain.entity.Student;

/**
 * REST Controller para gerenciar alunos.
 */
@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Alunos", description = "Gerenciamento de alunos")
public class StudentController {

    @Inject
    StudentService studentService;

    /**
     * Lista todos os alunos (ativos e inativos).
     */
    @GET
    @Operation(summary = "Listar todos os alunos")
    public List<Student> listAll() {
        return studentService.getAllStudents();
    }

    /**
     * Lista apenas alunos ativos.
     */
    @GET
    @Path("/active")
    @Operation(summary = "Listar alunos ativos")
    public List<Student> listActive() {
        return studentService.getActiveStudents();
    }

    /**
     * Busca um aluno por ID.
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar aluno por ID")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(studentService.findById(id)).build();
    }

    /**
     * Busca um aluno por matrícula.
     */
    @GET
    @Path("/registration/{registration}")
    @Operation(summary = "Buscar aluno por matrícula")
    public Response findByRegistration(@PathParam("registration") String registration) {
        return Response.ok(studentService.findByRegistration(registration)).build();
    }

    /**
     * Busca um aluno por email.
     */
    @GET
    @Path("/email/{email}")
    @Operation(summary = "Buscar aluno por email")
    public Response findByEmail(@PathParam("email") String email) {
        return Response.ok(studentService.findByEmail(email)).build();
    }

    /**
     * Lista alunos de um curso específico.
     */
    @GET
    @Path("/course/{courseId}")
    @Operation(summary = "Listar alunos de um curso")
    public List<Student> findByCourse(@PathParam("courseId") Long courseId) {
        return studentService.findByCourse(courseId);
    }

    /**
     * Cria um novo aluno.
     */
    @POST
    @Operation(summary = "Criar novo aluno")
    @APIResponse(responseCode = "201", description = "Aluno criado com sucesso")
    public Response create(@Valid CreateStudentRequest dto) {
        Student created = studentService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    /**
     * Atualiza um aluno existente.
     */
    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar aluno")
    public Response update(
            @PathParam("id") Long id,
            @Valid UpdateStudentRequest dto) {

        Student updated = studentService.update(id, dto);
        return Response.ok(updated).build();
    }

    /**
     * Inativa um aluno (soft delete).
     */
    @PATCH
    @Path("/{id}/inactivate")
    @Operation(summary = "Inativar aluno")
    public Response inactivate(@PathParam("id") Long id) {
        studentService.inactivate(id);
        return Response.noContent().build();
    }

    /**
     * Reativa um aluno inativo.
     */
    @PATCH
    @Path("/{id}/activate")
    @Operation(summary = "Reativar aluno")
    public Response activate(@PathParam("id") Long id) {
        studentService.activate(id);
        return Response.noContent().build();
    }

    /**
     * Remove permanentemente um aluno.
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remover aluno")
    public Response delete(@PathParam("id") Long id) {
        studentService.delete(id);
        return Response.noContent().build();
    }

    /**
     * Conta alunos ativos em um curso.
     */
    @GET
    @Path("/course/{courseId}/count")
    @Operation(summary = "Contar alunos ativos de um curso")
    public Response countActiveByCourse(@PathParam("courseId") Long courseId) {
        long count = studentService.countActiveByCourse(courseId);
        return Response.ok("{\"count\": " + count + "}").build();
    }
}
