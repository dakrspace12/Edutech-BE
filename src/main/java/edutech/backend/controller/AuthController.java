package edutech.backend.controller;



import edutech.backend.dto.ApiResponse;
import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            String token = authService.registerUser(signupRequest);
            if (token.equals("Username or Email is already in use")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, token, null));
            }
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", token));
        } catch (Exception e) {
            // Handle any errors that occur during registration
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Registration failed: " + e.getMessage(), null));
        }
    }

    // Endpoint for user authentication
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new ApiResponse(true, "User authenticated successfully", token));
        } catch (Exception e) {
            // Handle any authentication errors
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Authentication failed: " + e.getMessage(), null));
        }
    }
}
