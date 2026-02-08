package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando um coordenador não é encontrado no sistema.
 */
public class CoordinatorNotFoundException extends RuntimeException {
    
    public CoordinatorNotFoundException(String message) {
        super(message);
    }
    
    public CoordinatorNotFoundException(Long id) {
        super("Coordenador não encontrado com ID: " + id);
    }
}