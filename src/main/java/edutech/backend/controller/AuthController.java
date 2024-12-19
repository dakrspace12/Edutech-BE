package edutech.backend.controller;



import edutech.backend.dto.ApiResponse;
import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.service.AuthService;
import edutech.backend.util.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            String token = authService.registerUser(signupRequest);
            if (token.equals(MessageConstant.USERNAME_OR_EMAIL_IS_ALREADY_IN_USE)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, token, null));
            }
            return ResponseEntity.ok(new ApiResponse(true, MessageConstant.USER_REGISTERED_SUCCESSFULLY, token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, MessageConstant.REGISTRATION_FAILED + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new ApiResponse(true, MessageConstant.USER_AUTHENTICATED_SUCCESSFULLY, token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, MessageConstant.AUTHENTICATION_FAILED  + e.getMessage(), null));
        }
    }
}
