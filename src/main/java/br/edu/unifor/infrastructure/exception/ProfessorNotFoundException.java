package br.edu.unifor.infrastructure.exception;

public class ProfessorNotFoundException extends RuntimeException {

    public ProfessorNotFoundException(String message) {
        super(message);
    }

    public ProfessorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfessorNotFoundException(Long id) {
        super("Professor não encontrado com ID: " + id);
    }
    
}
