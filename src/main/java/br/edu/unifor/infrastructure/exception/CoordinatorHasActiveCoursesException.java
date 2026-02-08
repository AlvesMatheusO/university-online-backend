package br.edu.unifor.infrastructure.exception;

/**
 * Exceção lançada quando tenta-se remover um coordenador que ainda gerencia cursos ativos.
 */
public class CoordinatorHasActiveCoursesException extends RuntimeException {
    
    public CoordinatorHasActiveCoursesException(String message) {
        super(message);
    }
    
    public CoordinatorHasActiveCoursesException(Long coordinatorId, long coursesCount) {
        super(String.format(
            "Não é possível remover o coordenador (ID: %d) pois ele gerencia %d curso(s). " +
            "Remova os cursos vinculados ou use a opção de inativar.",
            coordinatorId,
            coursesCount
        ));
    }
}