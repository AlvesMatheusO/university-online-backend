package br.edu.unifor.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.application.dto.request.CreateCourseRequest;
import br.edu.unifor.application.mapper.CourseMapper;
import br.edu.unifor.domain.entity.Course;
import br.edu.unifor.domain.repository.CourseRepository;
import br.edu.unifor.infrastructure.exception.BusinessException;

@ApplicationScoped
public class CourseService {

    @Inject
    CourseRepository repository;

    @Inject
    CourseMapper mapper;

    @Transactional
    public Course create(CreateCourseRequest dto) {
        if (repository.existsByCode(dto.code())) {
            throw new BusinessException("Curso já existe");
        }
        Course course = mapper.toEntity(dto);
        repository.persist(course);
        return course;
    }
}
