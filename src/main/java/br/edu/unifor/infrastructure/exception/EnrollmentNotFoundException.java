package br.edu.unifor.infrastructure.exception;

public class EnrollmentNotFoundException extends RuntimeException {
    
    public EnrollmentNotFoundException(String message) {
        super(message);
    }
    
    public EnrollmentNotFoundException(Long id) {
        super("Matrícula não encontrada com ID: " + id);
    }
}