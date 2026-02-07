package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando tenta-se cadastrar um email que já existe.
 */
public class EmailAlreadyExistException extends RuntimeException {
    
    /**
     * Construtor padrão com email
     */
    public EmailAlreadyExistException(String email) {
        super("Email já cadastrado no sistema: " + email);
    }
    
    /**
     * Construtor com mensagem customizada
     */
    public EmailAlreadyExistException(String message, boolean isCustomMessage) {
        super(message);
    }
    
    /**
     * Construtor com causa raiz
     */
    public EmailAlreadyExistException(String email, Throwable cause) {
        super("Email já cadastrado no sistema: " + email, cause);
    }
}