package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando um aluno não é encontrado no sistema.
 */
public class StudentNotFoundException extends RuntimeException {
    
    public StudentNotFoundException(String message) {
        super(message);
    }
    
    public StudentNotFoundException(Long id) {
        super("Aluno não encontrado com ID: " + id);
    }
}