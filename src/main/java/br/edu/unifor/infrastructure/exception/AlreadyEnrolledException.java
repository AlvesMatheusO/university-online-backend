package br.edu.unifor.infrastructure.exception;

public class AlreadyEnrolledException extends RuntimeException {
    
    public AlreadyEnrolledException(String message) {
        super(message);
    }
    
    public AlreadyEnrolledException(String studentName, String className) {
        super(String.format(
            "O aluno '%s' já está matriculado na turma '%s'. " +
            "Não é permitido matrícula duplicada.",
            studentName,
            className
        ));
    }
}