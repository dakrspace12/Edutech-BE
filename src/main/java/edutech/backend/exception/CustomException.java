package edutech.backend.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    // Optional: Add an error code for more specific error classification
    private String errorCode;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
