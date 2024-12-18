package edutech.backend.exception;

public class CourseNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String status;

    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(String message, String errorCode, String status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getStatus() {
        return status;
    }
}
