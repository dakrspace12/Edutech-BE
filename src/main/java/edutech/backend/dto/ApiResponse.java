package edutech.backend.dto;


public class ApiResponse<T> {

    private boolean success;
    private String message;
    private String token;  // Optional, used for token-based responses like login/registration
    private T data;        // Generic field for the actual data in responses (e.g., user, course)

    // Constructor for success responses with a token (e.g., login, registration)
    public ApiResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    // Constructor for success responses with data (e.g., GET requests)
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Constructor for generic success responses without token or data (e.g., status updates)
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Static method for error responses (without data)
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // Static method for success responses with message and data
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // Static method for success responses with a token
    public static ApiResponse<String> success(String message, String token) {
        return new ApiResponse<>(true, message, token);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
