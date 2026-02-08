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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.application.service.StudentService;
import br.edu.unifor.domain.entity.Student;
import br.edu.unifor.infrastructure.exception.StudentHasActiveEnrollmentsException;
import br.edu.unifor.infrastructure.exception.StudentNotFoundException;

/**
 * REST Controller para gerenciar alunos.
 * 
 * Endpoints disponíveis:
 * - GET /students - Lista todos os alunos
 * - GET /students/active - Lista apenas alunos ativos
 * - GET /students/{id} - Busca aluno por ID
 * - GET /students/registration/{registration} - Busca por matrícula
 * - GET /students/email/{email} - Busca por email
 * - GET /students/course/{courseId} - Lista alunos de um curso
 * - POST /students - Cria novo aluno
 * - PUT /students/{id} - Atualiza aluno
 * - PATCH /students/{id}/inactivate - Inativa aluno
 * - PATCH /students/{id}/activate - Reativa aluno
 * - DELETE /students/{id} - Remove aluno
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
    @APIResponse(responseCode = "200", description = "Lista de alunos")
    public List<Student> listAll() {
        return studentService.getAllStudents();
    }

    /**
     * Lista apenas alunos ativos.
     */
    @GET
    @Path("/active")
    @Operation(summary = "Listar alunos ativos")
    @APIResponse(responseCode = "200", description = "Lista de alunos ativos")
    public List<Student> listActive() {
        return studentService.getActiveStudents();
    }

    /**
     * Busca um aluno por ID.
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar aluno por ID")
    @APIResponse(responseCode = "200", description = "Aluno encontrado")
    @APIResponse(responseCode = "404", description = "Aluno não encontrado")
    public Response findById(@PathParam("id") Long id) {
        try {
            Student student = studentService.findById(id);
            return Response.ok(student).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Busca um aluno por matrícula.
     */
    @GET
    @Path("/registration/{registration}")
    @Operation(summary = "Buscar aluno por matrícula")
    @APIResponse(responseCode = "200", description = "Aluno encontrado")
    @APIResponse(responseCode = "404", description = "Aluno não encontrado")
    public Response findByRegistration(
            @Parameter(description = "Matrícula do aluno", required = true) @PathParam("registration") String registration) {
        try {
            Student student = studentService.findByRegistration(registration);
            return Response.ok(student).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Busca um aluno por email.
     */
    @GET
    @Path("/email/{email}")
    @Operation(summary = "Buscar aluno por email")
    @APIResponse(responseCode = "200", description = "Aluno encontrado")
    @APIResponse(responseCode = "404", description = "Aluno não encontrado")
    public Response findByEmail(@PathParam("email") String email) {
        try {
            Student student = studentService.findByEmail(email);
            return Response.ok(student).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Lista alunos de um curso específico.
     */
    @GET
    @Path("/course/{courseId}")
    @Operation(summary = "Listar alunos de um curso")
    @APIResponse(responseCode = "200", description = "Lista de alunos do curso")
    public List<Student> findByCourse(@PathParam("courseId") Long courseId) {
        return studentService.findByCourse(courseId);
    }

    /**
     * Lista alunos ativos de um curso.
     */
    @GET
    @Path("/course/{courseId}/active")
    @Operation(summary = "Listar alunos ativos de um curso")
    public List<Student> findActiveByCourse(@PathParam("courseId") Long courseId) {
        return studentService.findActiveByCourse(courseId);
    }

    /**
     * Cria um novo aluno.
     */
    @POST
    @Operation(summary = "Criar novo aluno")
    @APIResponse(responseCode = "201", description = "Aluno criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Email ou CPF já cadastrado")
    public Response create(Student student) {
        try {
            Student created = studentService.createStudent(student);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Atualiza um aluno existente.
     */
    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar aluno")
    @APIResponse(responseCode = "200", description = "Aluno atualizado")
    @APIResponse(responseCode = "404", description = "Aluno não encontrado")
    public Response update(@PathParam("id") Long id, Student student) {
        try {
            Student updated = studentService.update(id, student);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Inativa um aluno (soft delete).
     */
    @PATCH
    @Path("/{id}/inactivate")
    @Operation(summary = "Inativar aluno")
    @APIResponse(responseCode = "204", description = "Aluno inativado")
    public Response inactivate(@PathParam("id") Long id) {
        try {
            studentService.inactivate(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Reativa um aluno inativo.
     */
    @PATCH
    @Path("/{id}/activate")
    @Operation(summary = "Reativar aluno")
    @APIResponse(responseCode = "204", description = "Aluno reativado")
    public Response activate(@PathParam("id") Long id) {
        try {
            studentService.activate(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Remove permanentemente um aluno.
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remover aluno", description = "Remove permanentemente um aluno. " +
            "Não é permitido se o aluno tiver matrículas ativas em aulas.")
    @APIResponse(responseCode = "204", description = "Aluno removido com sucesso")
    @APIResponse(responseCode = "404", description = "Aluno não encontrado")
    @APIResponse(responseCode = "409", description = "Aluno possui matrículas ativas")
    public Response delete(@PathParam("id") Long id) {
        try {
            studentService.delete(id);
            return Response.noContent().build();

        } catch (StudentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();

        } catch (StudentHasActiveEnrollmentsException e) {
            return Response.status(Response.Status.CONFLICT) // HTTP 409
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }

    private String createErrorResponse(String message) {
        return String.format("{\"error\": \"%s\"}", message);
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
