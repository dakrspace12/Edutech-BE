package edutech.backend.exception;

public class CourseNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // You can add a custom error code or HTTP status here if needed
    private String errorCode;
    private String status;

    // Constructor with message only
    public CourseNotFoundException(String message) {
        super(message);
    }

    // Constructor with message and errorCode/status
    public CourseNotFoundException(String message, String errorCode, String status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    // Optional: Add a constructor to wrap another throwable exception
    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Optional: Getters for additional fields
    public String getErrorCode() {
        return errorCode;
    }

    public String getStatus() {
        return status;
    }
}
