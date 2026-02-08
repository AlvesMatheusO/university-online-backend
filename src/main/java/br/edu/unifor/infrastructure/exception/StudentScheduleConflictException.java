package br.edu.unifor.infrastructure.exception;

public class StudentScheduleConflictException extends RuntimeException {
    
    public StudentScheduleConflictException(String message) {
        super(message);
    }
    
    public StudentScheduleConflictException(String studentName, String scheduleInfo) {
        super(String.format(
            "Conflito de horário: O aluno '%s' já possui uma matrícula no horário %s. " +
            "Um aluno não pode ter 2 turmas simultaneamente.",
            studentName,
            scheduleInfo
        ));
    }
}