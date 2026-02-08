package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando tenta-se remover um aluno que possui matrículas ativas em aulas.
 * 
 * Esta validação garante a integridade referencial e impede a perda de dados
 * de matrículas quando um aluno é removido do sistema.
 */
public class StudentHasActiveEnrollmentsException extends RuntimeException {
    
    /**
     * Construtor com mensagem customizada.
     */
    public StudentHasActiveEnrollmentsException(String message) {
        super(message);
    }
    
    /**
     * Construtor com ID do aluno e quantidade de matrículas.
     */
    public StudentHasActiveEnrollmentsException(Long studentId, long enrollmentCount) {
        super(String.format(
            "Não é possível remover o aluno (ID: %d) pois ele possui %d matrícula(s) ativa(s). " +
            "Inative as matrículas antes de remover o aluno ou use a opção de inativar ao invés de remover.",
            studentId,
            enrollmentCount
        ));
    }
    
    /**
     * Construtor com nome do aluno e quantidade de matrículas.
     */
    public StudentHasActiveEnrollmentsException(String studentName, long enrollmentCount) {
        super(String.format(
            "Não é possível remover o aluno '%s' pois ele possui %d matrícula(s) ativa(s). " +
            "Inative as matrículas antes de remover o aluno.",
            studentName,
            enrollmentCount
        ));
    }
}