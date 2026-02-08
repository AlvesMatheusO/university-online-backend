package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando há conflito de horário do professor.
 * 
 * Regra de negócio: Um professor não pode ministrar 2 turmas no mesmo horário.
 */
public class ProfessorScheduleConflictException extends RuntimeException {
    
    public ProfessorScheduleConflictException(String message) {
        super(message);
    }
    
    public ProfessorScheduleConflictException(String professorName, String scheduleInfo) {
        super(String.format(
            "Conflito de horário: O professor '%s' já possui uma turma no horário %s. " +
            "Um professor não pode ministrar 2 turmas simultaneamente.",
            professorName,
            scheduleInfo
        ));
    }
}