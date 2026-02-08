package br.edu.unifor.infrastructure.exception;

public class ClassNotFoundException extends RuntimeException {
    
    public ClassNotFoundException(String message) {
        super(message);
    }
    
    public ClassNotFoundException(Long id) {
        super("Turma não encontrada com ID: " + id);
    }
}