package in.gatehubx.api_manager_service.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends RuntimeException {

    private final HttpStatus status;

    public BusinessLogicException(HttpStatus status,String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
