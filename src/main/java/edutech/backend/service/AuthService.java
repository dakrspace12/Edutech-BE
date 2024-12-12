package edutech.backend.service;

import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;

public interface AuthService {
    String registerUser(SignupRequest signUpRequest);
    String authenticateUser(LoginRequest loginRequest);


}
