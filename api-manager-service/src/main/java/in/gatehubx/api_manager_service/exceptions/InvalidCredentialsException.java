package in.gatehubx.api_manager_service.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends RuntimeException {

    private final HttpStatus status;

    public InvalidCredentialsException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
