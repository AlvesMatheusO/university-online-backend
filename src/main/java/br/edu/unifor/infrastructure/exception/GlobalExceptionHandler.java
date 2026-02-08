package br.edu.unifor.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Handler global de exceções para padronizar respostas de erro da API.
 * 
 * Mapeia todas as exceções para respostas HTTP apropriadas com mensagens claras.
 * 
 * @author [Seu Nome]
 * @version 1.0
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        
        // Determinar status HTTP e mensagem baseado no tipo de exceção
        ErrorResponse errorResponse;
        
        if (exception instanceof ConstraintViolationException) {
            errorResponse = handleConstraintViolation((ConstraintViolationException) exception);
            
        } else if (isNotFoundException(exception)) {
            errorResponse = createErrorResponse(404, exception.getMessage());
            
        } else if (isConflictException(exception)) {
            errorResponse = createErrorResponse(409, exception.getMessage());
            
        } else if (isBadRequestException(exception)) {
            errorResponse = createErrorResponse(400, exception.getMessage());
            
        } else {
            errorResponse = createErrorResponse(
                500, 
                "Erro interno do servidor: " + exception.getMessage()
            );
        }
        
        return Response.status(errorResponse.status)
                .entity(errorResponse)
                .build();
    }

    /**
     * Verifica se é uma exceção de "Not Found" (404)
     */
    private boolean isNotFoundException(Exception e) {
        return e instanceof StudentNotFoundException ||
               e instanceof ProfessorNotFoundException ||
               e instanceof SubjectNotFoundException ||
               e instanceof ClassNotFoundException ||
               e instanceof CoordinatorNotFoundException ||
               e instanceof CourseNotFoundException ||
               e instanceof EnrollmentNotFoundException;
    }

    /**
     * Verifica se é uma exceção de "Conflict" (409)
     */
    private boolean isConflictException(Exception e) {
        return e instanceof EmailAlreadyExistException ||
               e instanceof CpfAlreadyExistsException ||
               e instanceof AlreadyEnrolledException ||
               e instanceof ClassFullException ||
               e instanceof ProfessorScheduleConflictException ||
               e instanceof StudentScheduleConflictException ||
               e instanceof StudentHasActiveEnrollmentsException ||
               e instanceof ClassHasEnrollmentsException ||
               e instanceof CoordinatorHasActiveCoursesException ||
               e instanceof SubjectHasActiveClassesException;
    }

    /**
     * Verifica se é uma exceção de "Bad Request" (400)
     */
    private boolean isBadRequestException(Exception e) {
        return e instanceof IllegalArgumentException ||
               e instanceof IllegalStateException;
    }

    /**
     * Trata erros de validação do Bean Validation
     */
    private ErrorResponse handleConstraintViolation(ConstraintViolationException e) {
        Map<String, String> violations = new HashMap<>();
        
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            violations.put(propertyPath, message);
        }
        
        return new ErrorResponse(
            400,
            "Erro de validação",
            LocalDateTime.now(),
            violations
        );
    }

    /**
     * Cria resposta de erro padrão
     */
    private ErrorResponse createErrorResponse(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now(), null);
    }

    /**
     * Classe interna para padronizar estrutura de resposta de erro
     */
    public static class ErrorResponse {
        public int status;
        public String message;
        public LocalDateTime timestamp;
        public Map<String, String> violations;

        public ErrorResponse(int status, String message, LocalDateTime timestamp, 
                           Map<String, String> violations) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
            this.violations = violations;
        }

        // Getters para Jackson serialization
        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, String> getViolations() { return violations; }
    }
}