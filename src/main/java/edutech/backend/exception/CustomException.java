package edutech.backend.exception;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // Constructor for message only
    public CustomException(String message) {
        super(message);  // Use the constructor from RuntimeException that accepts the message
    }

    // Constructor for message and cause
    public CustomException(String message, Throwable cause) {
        super(message, cause);  // Pass both message and cause to the superclass
    }
}

