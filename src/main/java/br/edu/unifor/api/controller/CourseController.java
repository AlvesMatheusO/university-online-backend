package br.edu.unifor.api.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.edu.unifor.domain.entity.Course;
import br.edu.unifor.domain.repository.CourseRepository;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Courses", description = "Gerenciamento de cursos")
public class CourseController {

    @Inject
    CourseRepository courseRepository;

    @GET
    @Operation(summary = "Listar cursos ativos")
    public List<Course> listActiveCourses() {
        return courseRepository.findAllActive();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar curso por ID")
    public Response findById(@PathParam("id") Long id) {
        return courseRepository.findByIdOptional(id) 
                .map(course -> Response.ok(course).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}