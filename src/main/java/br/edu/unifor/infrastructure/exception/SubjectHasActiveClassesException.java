package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando tenta remover disciplina com turmas ativas.
 */
public class SubjectHasActiveClassesException extends RuntimeException {
    
    public SubjectHasActiveClassesException(String message) {
        super(message);
    }
    
    public SubjectHasActiveClassesException(String subjectCode, long classesCount) {
        super(String.format(
            "Não é possível remover a disciplina '%s' pois ela possui %d turma(s) ativa(s). " +
            "Cancele todas as turmas antes de remover a disciplina.",
            subjectCode,
            classesCount
        ));
    }
}