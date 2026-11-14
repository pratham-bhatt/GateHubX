package in.gatehubx.api_manager_service.exceptions;

import in.gatehubx.api_manager_service.dto.errorResponse.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Business Logic and Invalid Credentials Exceptions
    @ExceptionHandler({
            BusinessLogicException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomExceptions(RuntimeException ex){
        HttpStatus status;

        if (ex instanceof BusinessLogicException businessException) {
            status = businessException.getStatus();
        } else {
            status = ((InvalidCredentialsException) ex).getStatus();
        }

        return buildErrorResponse(
                status,
                ex.getMessage(),
                List.of()
        );
    }

    // 2. Handle DTO validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );
    }

    // 3. Handle constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation ->
                        violation.getPropertyPath() + ": "
                                + violation.getMessage())
                .toList();

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Constraint validation failed",
                errors
        );
    }

    // 4. Handle invalid JSON request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestBody(
            HttpMessageNotReadableException ex) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request body",
                List.of("Request body is missing or contains invalid JSON")
        );
    }

    // 5. Handle all unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex) {

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                List.of()
        );
    }



    // Common response builder
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            List<String> description) {

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message,
                Instant.now(),
                description
        );

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }
}
