package edutech.backend.service;

import edutech.backend.dto.*;

import java.util.List;

public interface UserService {
    ApiResponse<Void> registerUser(SignupRequest signupRequest);
    String authenticateUser(LoginRequest loginRequest);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    void deleteUserById(Long id);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
}