package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando tenta-se remover uma turma que possui matrículas ativas.
 * 
 * Esta validação garante a integridade referencial e impede a perda de dados
 * de matrículas quando uma turma é removida do sistema.
 */
public class ClassHasEnrollmentsException extends RuntimeException {
    
    /**
     * Construtor com mensagem customizada.
     */
    public ClassHasEnrollmentsException(String message) {
        super(message);
    }
    
    /**
     * Construtor com ID da turma e quantidade de matrículas.
     */
    public ClassHasEnrollmentsException(Long classId, int enrollmentsCount) {
        super(String.format(
            "Não é possível remover a turma (ID: %d) pois ela possui %d matrícula(s) ativa(s). " +
            "Cancele todas as matrículas antes de remover a turma ou use a opção de cancelar ao invés de remover.",
            classId,
            enrollmentsCount
        ));
    }
    
    /**
     * Construtor com código da turma e quantidade de matrículas.
     */
    public ClassHasEnrollmentsException(String classCode, int enrollmentsCount) {
        super(String.format(
            "Não é possível remover a turma '%s' pois ela possui %d matrícula(s) ativa(s). " +
            "Cancele todas as matrículas antes de remover a turma.",
            classCode,
            enrollmentsCount
        ));
    }
}