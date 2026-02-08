package br.edu.unifor.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import br.edu.unifor.application.dto.request.subject.CreateSubjectRequest;
import br.edu.unifor.application.dto.request.subject.UpdateSubjectRequest;
import br.edu.unifor.domain.entity.Subject;
import br.edu.unifor.domain.repository.ClassRepository;
import br.edu.unifor.domain.repository.SubjectRepository;
import br.edu.unifor.infrastructure.exception.BusinessException;
import br.edu.unifor.infrastructure.exception.SubjectHasActiveClassesException;
import br.edu.unifor.infrastructure.exception.SubjectNotFoundException;;

/**
 * Service para gerenciar disciplinas.
 * 
 * @author [Seu Nome]
 * @version 1.0
 */
@ApplicationScoped
public class SubjectService {

    @Inject
    SubjectRepository subjectRepository;

    @Inject
    ClassRepository classRepository;

    /**
     * Cria uma nova disciplina.
     */
    @Transactional
    public Subject create(CreateSubjectRequest dto) {

        if (subjectRepository.existsByCode(dto.code)) {
            throw new BusinessException(
                    "Já existe uma disciplina com o código: " + dto.code);
        }

        Subject subject = new Subject(
                dto.code,
                dto.name,
                dto.workload,
                dto.credits,
                dto.description);

        subjectRepository.persist(subject);
        return subject;
    }

    /**
     * Lista todas as disciplinas.
     */
    public List<Subject> getAllSubjects() {
        return subjectRepository.listAll();
    }

    /**
     * Busca disciplina por ID.
     */
    public Subject findById(Long id) {
        return subjectRepository.findByIdOptional(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
    }

    /**
     * Busca disciplina por código.
     */
    public Subject findByCode(String code) {
        return subjectRepository.findByCode(code)
                .orElseThrow(() -> new SubjectNotFoundException(
                        "Disciplina não encontrada com código: " + code));
    }

    /**
     * Busca disciplinas por créditos.
     */
    public List<Subject> findByCredits(Integer credits) {
        return subjectRepository.findByCredits(credits);
    }

    /**
     * Atualiza disciplina.
     */
    @Transactional
    public Subject update(Long id, UpdateSubjectRequest dto) {
        Subject subject = findById(id);

        // Validar código se mudou
        if (!subject.code.equals(dto.code)) {
            if (subjectRepository.existsByCode(dto.code)) {
                throw new BusinessException(
                        "Já existe uma disciplina com o código: " + dto.code);
            }
        }

        subject.code = dto.code;
        subject.name = dto.name;
        subject.workload = dto.workload;
        subject.credits = dto.credits;
        subject.description = dto.description;

        return subject;
    }

    /**
     * Remove disciplina.
     */
    @Transactional
    public void delete(Long id) {
        Subject subject = findById(id);

        // Validar se tem turmas ativas
        long activeClasses = classRepository.countActiveBySubject(id);

        if (activeClasses > 0) {
            throw new SubjectHasActiveClassesException(subject.code, activeClasses);
        }

        subjectRepository.delete(subject);
    }

    /**
     * Verifica se existe.
     */
    public boolean exists(Long id) {
        return subjectRepository.findByIdOptional(id).isPresent();
    }

    /**
     * Conta total.
     */
    public long count() {
        return subjectRepository.count();
    }
}