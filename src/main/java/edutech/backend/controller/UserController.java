package edutech.backend.controller;


import edutech.backend.dto.ApiResponse;
import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.dto.UserDto;
import edutech.backend.service.UserService;
import edutech.backend.service.UserServiceImpl;
import edutech.backend.util.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
        return ResponseEntity.ok(new ApiResponse<>(true, MessageConstant.PASSWORD_RESET_LINK_SENT, null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok(new ApiResponse<>(true, MessageConstant.PASSWORD_RESET_SUCESSFULLY, null));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody SignupRequest signupRequest) {
        userService.registerUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, MessageConstant.USER_REGISTED_SUCESSFULLY, null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String token = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, MessageConstant.USER_AUTHENTICATED, token));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true,MessageConstant.USERS_RETREIVED,users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return  ResponseEntity.ok(new ApiResponse<>(true,MessageConstant.USER_RETREIVED, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(true, MessageConstant.USER_DELETED, null));
    }
}
