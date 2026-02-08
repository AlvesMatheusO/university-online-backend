package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando tenta-se cadastrar um CPF que já existe.
 */
public class CpfAlreadyExistsException extends RuntimeException {
    
    public CpfAlreadyExistsException(String cpf) {
        super("CPF já cadastrado no sistema: " + cpf);
    }
}