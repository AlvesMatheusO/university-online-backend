package br.edu.unifor.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;

import br.edu.unifor.application.dto.request.CreateCourseRequest;
import br.edu.unifor.domain.entity.Course;

@ApplicationScoped
public class CourseMapper {

    public Course toEntity(CreateCourseRequest dto) {
        return new Course(
            dto.code(),
            dto.name(),
            dto.department(),
            dto.duration(),
            dto.active()
        );
    }
}
