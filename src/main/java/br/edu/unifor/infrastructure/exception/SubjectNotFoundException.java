package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando uma disciplina não é encontrada no sistema.
 */
public class SubjectNotFoundException extends RuntimeException {
    
    public SubjectNotFoundException(String message) {
        super(message);
    }
    
    public SubjectNotFoundException(Long id) {
        super("Disciplina não encontrada com ID: " + id);
    }
}