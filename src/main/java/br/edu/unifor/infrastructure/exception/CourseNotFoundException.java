package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando um curso não é encontrado no sistema.
 */
public class CourseNotFoundException extends RuntimeException {
    
    public CourseNotFoundException(String message) {
        super(message);
    }
    
    public CourseNotFoundException(Long id) {
        super("Curso não encontrado com ID: " + id);
    }
}