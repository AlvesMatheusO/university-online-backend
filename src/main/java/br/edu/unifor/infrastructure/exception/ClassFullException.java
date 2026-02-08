package br.edu.unifor.infrastructure.exception;

public class ClassFullException extends RuntimeException {
    
    public ClassFullException(String message) {
        super(message);
    }
    
    public ClassFullException(String classCode, int maxCapacity) {
        super(String.format(
            "A turma '%s' está com capacidade máxima (%d alunos). Não há vagas disponíveis.",
            classCode,
            maxCapacity
        ));
    }
}