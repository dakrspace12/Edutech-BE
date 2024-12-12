package edutech.backend.service;

import edutech.backend.dto.ApiResponse;
import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.dto.UserDto;

import java.util.List;

public interface UserService {

    ApiResponse<Void> registerUser(SignupRequest signupRequest);

    String authenticateUser(LoginRequest loginRequest);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteUserById(Long id);
}
